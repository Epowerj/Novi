package net.pixelstatic.novi.entities;



public class Bullet extends FlyingEntity{
	{
		material.drag = 0;
	}
	@Override
	public void Update(){
		UpdateVelocity();
	}

	@Override
	public void Draw(){
		renderer.layer("bullet", x, y).setLayer(0.5f).setRotation(velocity.angle() - 90);
	}
	//don't want to hit players or other bullets
	public boolean collides(SolidEntity other){
		return super.collides(other) && !(other instanceof Player || other instanceof Bullet);
	}
	
	@Override
	public void collisionEvent(SolidEntity other){
		//spawn explosion and dissapear
		new ExplosionEffect().setPosition(x, y).SendSelf();
		server.removeEntity(this);
	}

}
