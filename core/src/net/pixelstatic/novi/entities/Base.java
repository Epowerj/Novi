package net.pixelstatic.novi.entities;

import java.util.ArrayList;

import net.pixelstatic.novi.entities.effects.*;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.world.*;

import com.badlogic.gdx.math.Vector2;

public class Base extends Enemy implements Syncable{
	public final int size = 10;
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
		material.getRectangle().setSize(size * Material.blocksize, size * Material.blocksize);
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
		if( !(other instanceof Bullet) || !(((Bullet)other).shooter instanceof Player)) return false;
		Bullet bullet = (Bullet)other;
		Block block = getBlockAt(other.x, other.y);
		if(block == null) return false;
		boolean collide = block != null && !block.empty() && block.solid();
		if(collide){
			block.health --;
		}
		if(block.health < 0){
			block.getMaterial().destroyEvent(this, block.x, block.y);
			new ExplosionEmitter(10f, 1f, 14f).setPosition(other.x, other.y).AddSelf();
		//	explosion(block.x,block.y);
		}
		if(collide) update(block.x, block.y);
		return collide;
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

	public void deathEvent(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(!blocks[x][y].empty()) new BreakEffect("ironblock").setPosition(worldx(x), worldy(y)).SendSelf();
			}
		}
		new ExplosionEmitter(120, 1.1f, size * Material.blocksize / 2f).setPosition(x, y).AddSelf();
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

}
