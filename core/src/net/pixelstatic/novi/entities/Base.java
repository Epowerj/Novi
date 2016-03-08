package net.pixelstatic.novi.entities;

import java.util.ArrayList;

import net.pixelstatic.novi.entities.effects.ExplosionEmitter;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.world.*;

public class Base extends DestructibleEntity implements TimedSyncable{
	public final int size = 10;
	Block[][] blocks;
	ArrayList<BlockUpdate> updates = new ArrayList<BlockUpdate>();

	{
		blocks = new Block[size][size];
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				blocks[x][y] = new Block(x,y,Material.ironblock);
				health += blocks[x][y].health;
			}
		}
		material.getRectangle().setSize(size * Material.blocksize, size * Material.blocksize);
	}

	@Override
	public void Update(){

	}

	@Override
	public boolean collides(SolidEntity other){
		if( !(other instanceof Bullet)) return false;

		Block block = getBlockAt(other.x, other.y);
		if(block == null) return false;
		boolean collide = block != null && !block.empty();
		if(collide){
			block.health --;
		} 
		if(block.health < 0){
			block.setMaterial(Material.air);
			new ExplosionEmitter(10f, 1f, 7f).setPosition(other.x, other.y).AddSelf();
		}
		if(collide)updateBlock(block.x, block.y);
		return collide;
	}
	
	public void updateBlock(int x, int y){
		Block block = blocks[x][y];
		updates.add(new BlockUpdate(block.getMaterial(), block.x, block.y, block.health));
	}

	public Block getBlockAt(float x, float y){
		float relativex = x - this.x + size / 2f * Material.blocksize, relativey = y - this.y + size / 2f * Material.blocksize;
		int blockx = (int)(relativex / Material.blocksize), blocky = (int)(relativey / Material.blocksize);
		if(blockx < 0 || blocky < 0 || blockx >= size || blocky >= size) return null;
		return blocks[blockx][blocky];
	}

	public void deathEvent(){
		new ExplosionEmitter(60, 1f, size * Material.blocksize / 2f).setPosition(x, y).AddSelf();
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
		@SuppressWarnings("unchecked")
		ArrayList<BlockUpdate> clone = (ArrayList<BlockUpdate>)updates.clone();
		updates.clear();
		return new BaseSyncBuffer(clone);
	}

	@Override
	public void readSync(SyncBuffer buffer){
    	for(BlockUpdate update : ((BaseSyncBuffer)buffer).updates){
    		update.apply(blocks);
    	}
	}

	@Override
	public boolean sync(){
		return !updates.isEmpty();
	}

}
