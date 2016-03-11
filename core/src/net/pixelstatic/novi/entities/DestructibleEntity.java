package net.pixelstatic.novi.entities;

//an entity that can be hit and/or destroyed by bullets
public abstract class DestructibleEntity extends FlyingEntity{
	public int health = 100;
	
	public void collisionEvent(SolidEntity other){
		if(!(other instanceof Bullet)) return;
		health -= ((Bullet)other).type.damage();
		hitEvent((Bullet)other);
		if(health < 0){
			deathEvent();
			if(this.removeOnDeath())server.removeEntity(this);
		}
	}
	
	//when the entity dies
	public void deathEvent(){
		
	}
	
	//when the entity is hit
	public void hitEvent(Bullet bullet){
		
	}
	
	//whether to remove the entity when it dies
	public boolean removeOnDeath(){
		return true;
	}
}
