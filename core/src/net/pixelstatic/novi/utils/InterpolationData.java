package net.pixelstatic.novi.utils;

import net.pixelstatic.novi.entities.*;

import com.badlogic.gdx.math.*;

public class InterpolationData{
	static final float correctrange = 100f;
	static Vector2 temp1 = new Vector2();
	static Vector2 temp2 = new Vector2();
	long lastupdate = -1;
	float updateframes;
	float lastx, lasty, lastrotation;

	public void push(Entity e, float x, float y, float rotation){
		if(lastupdate != -1) updateframes = ((System.currentTimeMillis() - lastupdate) / 1000f) * 60f;
		lastupdate = System.currentTimeMillis();
		lastx = x - e.x;
		lasty = y - e.y;
		lastrotation = rotation;
		if(Math.abs(e.x - x) > correctrange || Math.abs(e.y - y) > correctrange){
		//	e.setPosition(x, y);
		}
	}
	
	public void update(Entity entity){
		temp1.set(entity.x, entity.y);
		temp2.set(lastx + entity.x,lasty + entity.y);
		if(entity instanceof Player){
			Player player =(Player)entity;
			if(player.velocity != null)
			temp2.add(player.ping*player.velocity.x, player.ping*player.velocity.y);
		}
		temp1.interpolate(temp2, 0.5f, Interpolation.linear);
		entity.setPosition(temp1.x, temp1.y);
		if(entity instanceof Player)((Player)entity).rotation = MathUtils.lerpAngleDeg(((Player)entity).rotation, lastrotation, 0.25f);
	}
	
	public float elapsed(){
		return Math.min((((System.currentTimeMillis() - lastupdate) / 1000f) * 60f) / updateframes, 1.0f);
	}
}
