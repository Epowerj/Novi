package net.pixelstatic.novi.systems;

import java.util.HashSet;

import net.pixelstatic.novi.entities.*;

public class CollisionSystem extends EntitySystem{
	HashSet<Long> collided = new HashSet<Long>(); //used for storing collisions each frame so entities don't collide twice
	
	@Override
	public void update(Entity aentity){
		SolidEntity entity = (SolidEntity)aentity;
		for(Entity other : Entity.entities.values()){
			if( !entity.inRange(other, 10 + entity.material.getRectangle().width) || other.equals(entity) || !(other instanceof SolidEntity)) continue;
			if( !collided.contains(other.GetID())){
				SolidEntity othersolid = (SolidEntity)other;
				if(othersolid.collides(entity) && entity.collides(othersolid)){
					collisionEvent(entity, othersolid);
					collided.add(entity.GetID());
				}
			}
		}
	}
	
	private void collisionEvent(SolidEntity entitya, SolidEntity entityb){
		entitya.collisionEvent(entityb);
		entityb.collisionEvent(entitya);
	}

	public boolean accept(Entity entity){
		return entity instanceof SolidEntity;
	}
}
