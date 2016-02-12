package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.utils.MaterialData;

//a solid entity is an entity that collides with things
//this class does not have velocity; see FlyingEntity
public abstract class SolidEntity extends Entity{
	transient MaterialData material = new MaterialData(this, 6,6);
	
	//returns whether this entity collides with the other solid entity
	public boolean collides(SolidEntity other){
		material.updateHitbox();
		other.material.updateHitbox();
		return material.collides(other.material);
	}
	
	//called when this entity hits another one - overriding is optional
	public void collisionEvent(SolidEntity other){
		
	}
}
