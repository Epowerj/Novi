package net.pixelstatic.novi.entities;

import static net.pixelstatic.novi.utils.WorldUtils.bound;
import static net.pixelstatic.novi.utils.WorldUtils.relative3;

import java.awt.Point;
import java.util.ArrayList;

import net.pixelstatic.novi.entities.effects.*;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.network.Syncable.GlobalSyncable;
import net.pixelstatic.novi.utils.Angles;
import net.pixelstatic.novi.utils.InterpolationData;
import net.pixelstatic.novi.world.Block;
import net.pixelstatic.novi.world.BlockUpdate;
import net.pixelstatic.novi.world.Material;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

@GlobalSyncable
public class Base extends Enemy implements Syncable{
	public final int size = 10;
	private transient ArrayList<Block> blocklist = new ArrayList<Block>();
	private transient Rectangle rectangle = new Rectangle(0, 0, Material.blocksize, Material.blocksize);
	public float rotation;
	public Block[][] blocks;
	public boolean[][] updated;
	public int spawned;
	private String texture= "titanship";
	transient private InterpolationData data = new InterpolationData();

	{	
		velocity = new Vector2(0,1);
		blocks = new Block[size][size];
		updated = new boolean[size][size];
		generateBlocks();
		material.getRectangle().setSize(size * (Material.blocksize + 1), size * (Material.blocksize + 1));
		updateHealth();
	}

	void generateBlocks(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				blocks[x][y] = new Block(x, y, Material.air);
				//if(Vector2.dst(x, y, size / 2f, size / 2f) < 4.5f) blocks[x][y].setMaterial(Material.ironblock);
			}
		}
		int o = 3;
		blocks[o-1][o].setMaterial(Material.turret);
		blocks[size - o+1][o].setMaterial(Material.turret);
		blocks[o][size - o-1].setMaterial(Material.turret);
		blocks[size - o][size - o-1].setMaterial(Material.turret);
		blocks[size / 2][2].setMaterial(Material.dronemaker);
		blocks[size / 2][size / 2-1].setMaterial(Material.bigturret);
	}

	void updateHealth(){
		health = 0;
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if(blocks[x][y].solid()) health += blocks[x][y].getMaterial().health();
			}
		}
	}
	//HNNNGGGGG
	@Override
	public boolean collides(SolidEntity other){
		if( !(other instanceof Damager) || (other instanceof Bullet && !(((Bullet)other).shooter instanceof Player))) return false;
		Point point = blockPosition(other.x, other.y);
		radiusBlocks(point.x, point.y);
		boolean collide = false;
		for(Block block : blocklist){
			Vector2 vector = world(block.x, block.y);
			rectangle.setCenter(bound(vector.x), bound(vector.y));
			if(other.collides(rectangle)){
				block.health -= ((Damager)other).damage();
				checkHealth(block, vector);
				update(block.x, block.y);
				Effects.explosion(vector.x, vector.y);
				collide = true;
			}
		}
		if(collide) updateHealth();
		return collide;
	}

	public void checkHealth(Block block, Vector2 pos){
		if(block.health < 0){
			block.getMaterial().destroyEvent(this, block.x, block.y);
			new ExplosionEmitter(10f, 1f, 14f).setPosition(pos.x, pos.y).AddSelf();
			//	explosion(block.x,block.y);
		}
	}

	public void explosion(int cx, int cy){
		int rad = 4;
		for(int x = -rad;x <= rad;x ++){
			for(int y = -rad;y <= rad;y ++){
				int relx = cx + x, rely = cy + y;
				if(relx < 0 || rely < 0 || relx >= size || rely >= size) continue;
				float dist = Vector2.dst(x, y, 0, 0);
				if(dist >= rad) continue;
				Block block = blocks[relx][rely];
				if( !block.solid()) block.health -= (int)((1f - dist / rad + 0.2f) * block.getMaterial().health());
				if(block.health < 0) block.setMaterial(Material.air);
				update(relx, rely);
			}
		}
	}

	//returns a list of solid blocks around the specified location
	public ArrayList<Block> radiusBlocks(int cx, int cy){
		int rad = 1;
		blocklist.clear();
		for(int x = -rad;x <= rad;x ++){
			for(int y = -rad;y <= rad;y ++){
				int relx = cx + x, rely = cy + y;
				if(relx < 0 || rely < 0 || relx >= size || rely >= size) continue;
				if(blocks[relx][rely].solid()) blocklist.add(blocks[relx][rely]);
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

	public Point blockPosition(float x, float y){
		x = relative3(x, this.x);
		y = relative3(y, this.y);
		Vector2 v = Angles.rotate(x, y, -rotation);
		float relx = (v.x - Material.blocksize / 2f + unitSize() / 2f) / Material.blocksize;
		float rely = (v.y - Material.blocksize / 2f + unitSize() / 2f) / Material.blocksize;
		return new Point((int)relx, (int)rely);
	}

	public void deathEvent(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if( !blocks[x][y].empty()){
					Vector2 v = world(x, y);
					new BreakEffect("ironblock").setPosition(v.x, v.y).SendSelf();
				}
			}
		}
		//if(texture != null)new BreakEffect(texture).setPosition(x, y).SendSelf();
		new ExplosionEmitter(120, 1.1f, size * Material.blocksize / 2f).setPosition(x, y).AddSelf();
		new Shockwave().setPosition(x, y).SendSelf();
		Effects.shake(80f, 60f, x, y);
	}

	@Override
	public void Draw(){
		data.update(this);
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				Block block = blocks[x][y];
				if(block.empty()) continue;
				block.getMaterial().draw(block, this, x, y);
			}
		}
		if(texture != null) renderer.layer(texture, x , y).setLayer( -2).setRotation(rotation).addShadow();
	}

	@Override
	public SyncBuffer writeSync(){
		ArrayList<BlockUpdate> updates = new ArrayList<BlockUpdate>();
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				if( !updated[x][y]) continue;
				Block block = blocks[x][y];
				updates.add(new BlockUpdate(block));
			}
		}
		return new BaseSyncBuffer(updates, rotation, x, y);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		this.rotation = ((BaseSyncBuffer)buffer).rotation;
		data.push(this, buffer.x, buffer.y, 0f);
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
				updated[x][y] = false;
				block.getMaterial().update(block, this);
			}
		}
		
		velocity.setLength(1f);
		velocity.rotate(0.1f);
		rotation = velocity.angle()-90;
	}

	public void setTexture(String texture){
		this.texture = texture;
	}

	public float unitSize(){
		return Material.blocksize * size;
	}

	public Vector2 world(int x, int y){
		float relx = (x * Material.blocksize - size / 2f * Material.blocksize);
		float rely = (y * Material.blocksize - size / 2f * Material.blocksize);
		Vector2 v = Angles.rotate(relx, rely, rotation);
		v.add(this.x, this.y);
		return v;
	}

}
