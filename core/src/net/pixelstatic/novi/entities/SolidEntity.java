package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.utils.MaterialData;

import com.badlogic.gdx.math.Rectangle;

//a solid entity is an entity that collides with things
//this class does not have velocity; see FlyingEntity
public abstract class SolidEntity extends Entity{
	transient public MaterialData material = new MaterialData(this, 6,6);
	
	//returns whether this entity collides with the other solid entity
	public boolean collides(SolidEntity other){
		if(other instanceof Bullet && this.equals(((Bullet)other).shooter)) return false;
		material.updateHitbox();
		other.material.updateHitbox();
		return material.collides(other.material);
	}
	
	public boolean collides(Rectangle rekt){
		material.updateHitbox();
		return material.getRectangle().overlaps(rekt);
	}
	
	//called when this entity hits another one - overriding is optional
	public void collisionEvent(SolidEntity other){
		
	}
}
