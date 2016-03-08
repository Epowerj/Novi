package net.pixelstatic.novi.world;

import net.pixelstatic.novi.entities.*;

import com.badlogic.gdx.graphics.Color;

public enum Material{
	air, ironblock;

	static public final int blocksize = 14;

	public void draw(Block block, Base base, int x, int y){
		defaultDraw(name(), block, base, x, y);
	}

	public void defaultDraw(String region, Block block, Base base, int x, int y){
		Entity.renderer.layer(region,  base.y + x * blocksize - base.size / 2f * blocksize+ blocksize/2f, base.x + y * blocksize - base.size / 2f * blocksize + blocksize/2f)
		.setLayer(-1f)
		.setColor(new Color(block.healthfrac()+0.3f, block.healthfrac()+0.3f, block.healthfrac()+0.3f, 1f));
		
	}
	
	public boolean solid(){
		return true;
	}
	
	public int health(){
		return 10;
	}
}
