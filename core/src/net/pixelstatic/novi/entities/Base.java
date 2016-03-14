package net.pixelstatic.novi.entities;

import java.util.ArrayList;

import net.pixelstatic.novi.entities.effects.*;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.world.*;

import com.badlogic.gdx.math.*;

public class Base extends Enemy implements Syncable{
	public final int size = 10;
	private transient ArrayList<Block> blocklist = new ArrayList<Block>();
	private transient Rectangle rectangle = new Rectangle(0,0,Material.blocksize,Material.blocksize);
	public Block[][] blocks;
	public boolean[][] updated;
	public int spawned;

	{
		blocks = new Block[size][size];
		updated = new boolean[size][size];
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				blocks[x][y] = new Block(x, y, Material.air);
				if(Vector2.dst(x, y, size/2f, size/2f) < 4.5f) blocks[x][y].setMaterial(Material.ironblock);
				//if(Vector2.dst(x, y, size/2f, size/2f) <= 2) blocks[x][y].setMaterial(Material.turret);
			}
		}
		int o = 3;
		blocks[o][o].setMaterial(Material.turret);
		blocks[size - o][o].setMaterial(Material.turret);
		blocks[o][size - o].setMaterial(Material.turret);
		blocks[size - o][size - o].setMaterial(Material.turret);
		blocks[size/2][1].setMaterial(Material.dronemaker);
		blocks[size/2][size/2].setMaterial(Material.bigturret);
		
		material.getRectangle().setSize(size * (Material.blocksize+1), size * (Material.blocksize+1));
		updateHealth();
	}
	
	void updateHealth(){
		health = 0;
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(blocks[x][y].solid()) health += blocks[x][y].getMaterial().health();
			}
		}
	}

	@Override
	public boolean collides(SolidEntity other){
		if( !(other instanceof Damager) || (other instanceof Bullet && !(((Bullet)other).shooter instanceof Player))) return false;
		radiusBlocks(blockX(other.x), blockY(other.y));
		boolean collide = false;
		for(Block block : blocklist){
			rectangle.setPosition(worldex(block.x), worldey(block.y));
			if(other.collides(rectangle)){
				block.health -= ((Damager)other).damage();
				checkHealth(block);
				update(block.x, block.y);
				Effects.explosion(worldx(block.x), worldy(block.y));
				collide = true;
			}
		}
		return collide;
	}
	
	public void checkHealth(Block block){
		if(block.health < 0){
			block.getMaterial().destroyEvent(this, block.x, block.y);
			new ExplosionEmitter(10f, 1f, 14f).setPosition(worldx(block.x), worldy(block.y)).AddSelf();
		//	explosion(block.x,block.y);
		}
	}
	
	public void explosion(int cx, int cy){
		int rad = 4;
		for(int x = -rad; x <= rad; x ++){
			for(int y = -rad; y <= rad; y ++){
				int relx = cx+x, rely = cy+y;
				if(relx < 0 || rely < 0 || relx >= size || rely >= size) continue;
				float dist = Vector2.dst(x, y, 0, 0); 
				if(dist >= rad) continue;
				Block block = blocks[relx][rely];
				if(!block.solid())block.health -= (int)((1f-dist/rad+0.2f)*block.getMaterial().health());
				if(block.health < 0 ) block.setMaterial(Material.air);
				update(relx,rely);
			}
		}
	}
	
	//returns a list of solid blocks around the specified location
	public ArrayList<Block> radiusBlocks(int cx, int cy){
		int rad = 1;
		blocklist.clear();
		for(int x = -rad; x <= rad; x ++){
			for(int y = -rad; y <= rad; y ++){
				int relx = cx+x, rely = cy+y;
				if(relx < 0 || rely < 0 || relx >= size || rely >= size) continue;
				if(blocks[relx][rely].solid())
				blocklist.add(blocks[relx][rely]);
			}
		}
		return blocklist;
	}

	//updates a block at x,y so it gets synced
	public void update(int x, int y){
		updated[x][y] = true;
	}

	public Block getBlockAt(float x, float y){
		float relativex = x - this.x + size / 2f * Material.blocksize, relativey = y - this.y + size / 2f * Material.blocksize;
		int blockx = (int)(relativex / Material.blocksize), blocky = (int)(relativey / Material.blocksize);
		if(blockx < 0 || blocky < 0 || blockx >= size || blocky >= size) return null;
		return blocks[blockx][blocky];
	}
	
	public int blockX(float x){
		return (int)(( x - this.x + size / 2f * Material.blocksize) / Material.blocksize);
	}
	
	public int blockY(float y){
		return (int)(( y - this.y + size / 2f * Material.blocksize) / Material.blocksize);
	}

	public void deathEvent(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(!blocks[x][y].empty()) new BreakEffect("ironblock").setPosition(worldx(x), worldy(y)).SendSelf();
			}
		}
		new ExplosionEmitter(120, 1.1f, size * Material.blocksize / 2f).setPosition(x, y).AddSelf();
		new Shockwave().setPosition(x, y).SendSelf();
		Effects.shake(80f, 60f, x, y);
	}

	@Override
	public void Draw(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				Block block = blocks[x][y];
				if(block.empty()) continue;
				block.getMaterial().draw(block, this, x, y);
			}
		}
	}

	@Override
	public SyncBuffer writeSync(){
		ArrayList<BlockUpdate> updates = new ArrayList<BlockUpdate>();
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(!updated[x][y]) continue;
				Block block = blocks[x][y];
				updates.add(new BlockUpdate(block));
				updated[x][y] = false;
			}
		}
		return new BaseSyncBuffer(updates);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		for(BlockUpdate update : ((BaseSyncBuffer)buffer).updates){
			update.apply(blocks);
		}
	}

	@Override
	public void behaviorUpdate(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				Block block = blocks[x][y];
				if(block.empty()) continue;
				block.getMaterial().update(block, this);
			}
		}
	}
	
	float worldx(int i){
		return x + i * Material.blocksize - size / 2f * Material.blocksize + Material.blocksize / 2f;
	}
	
	float worldy(int i){
		return y + i * Material.blocksize - size / 2f * Material.blocksize + Material.blocksize / 2f;
	}
	
	float worldex(int i){
		return x + i * Material.blocksize - size / 2f * Material.blocksize;
	}
	
	float worldey(int i){
		return y + i * Material.blocksize - size / 2f * Material.blocksize;
	}

}
