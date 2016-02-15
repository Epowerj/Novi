package net.pixelstatic.novi.entities;



public class Bullet extends FlyingEntity{
	int life, lifetime = 100;
	public Entity shooter;
	
	{
		material.drag = 0;
		material.getRectangle().setSize(2);
	}
	@Override
	public void Update(){
		life += delta();
		if(life >= lifetime) RemoveSelf();
		UpdateVelocity();
	}

	@Override
	public void Draw(){
		renderer.layer("bullet", x, y).setLayer(0.5f).setRotation(velocity.angle() - 90);
	}
	
	public Bullet setShooter(Entity entity){
		shooter = entity;
		return this;
	}
	
	//don't want to hit players or other bullets
	public boolean collides(SolidEntity other){
		return super.collides(other) && !((other instanceof Player && shooter instanceof Player) || other instanceof Bullet || other.equals(shooter));
	}
	
	@Override
	public void collisionEvent(SolidEntity other){
		//spawn explosion and dissapear
		new ExplosionEffect().setPosition(x, y).SendSelf();
		server.removeEntity(this);
	}

}
