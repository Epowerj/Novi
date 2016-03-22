package net.pixelstatic.novi.utils;

import net.pixelstatic.novi.entities.SolidEntity;

import com.badlogic.gdx.math.Rectangle;

//stores mass, hitbox size, calculating collisions..
public class MaterialData{
	public float mass = 1f;
	public float drag = 0.08f;
	public float maxvelocity = -1;
	private SolidEntity entity;
	private Rectangle rectangle;
	
	public boolean collides(MaterialData other){
		return updateHitbox().overlaps(other.rectangle) || updateHitboxWrap().overlaps(other.rectangle);
	}
	
	public Rectangle getRectangle(){
		return rectangle;
	}
	
	public MaterialData(SolidEntity entity, int hitwidth, int hitheight){
		this.entity = entity;
		rectangle = new Rectangle(0, 0, hitwidth, hitheight);
	}
	
	public Rectangle updateHitbox(){
		return rectangle.setCenter(entity.predictedX(), entity.predictedY());
	}
	
	public Rectangle updateHitboxWrap(){
		return rectangle.setCenter(WorldUtils.wrap(entity.predictedX()), WorldUtils.wrap(entity.predictedY()));
	}
}
