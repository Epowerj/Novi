package net.pixelstatic.novi.entities;

public class Player extends FlyingEntity{
	
	@Override
	public void Update(){
		UpdateVelocity();
	}
	
	public void accelerate(){
		y ++;
	}
	
	public void deccelerate(){
		y --;
	}
	
	public void turn(float amount){
		x -= amount;
	}
	
	public void shoot(){
		Bullet b = new Bullet();
		b.x = x;
		b.y = y;
		b.velocity.set(0f, 3f);
		b.AddSelf();
	}

	@Override
	public void Draw(){
		renderer.layer("ship", x, y).setLayer(1);
	}

}
