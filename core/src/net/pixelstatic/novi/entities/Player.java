package net.pixelstatic.novi.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Player extends FlyingEntity{
	float speed = 0.2f;
	float turnspeed = 3f;
	float maxvelocity = 2f;
	{
		drag = 0.02f;
	}
	
	@Override
	public void Update(){
		UpdateVelocity();
		
	}
	
	public void accelerate(){
		if(velocity.isZero()) velocity.y = speed;
		if(velocity.len() < maxvelocity)velocity.setLength(velocity.len() + speed);
	}
	
	public void deccelerate(){
		//velocity.setLength(velocity.len() * 0.5f);
	}
	
	public void turn(float amount){
		velocity.setAngle(velocity.angle() + amount*turnspeed);
	}
	
	public void shoot(){
		Vector2 v = new Vector2(Gdx.input.getX() - Gdx.graphics.getWidth() / 2 ,Gdx.graphics.getHeight() - Gdx.input.getY() -  Gdx.graphics.getHeight()/2);
		
		Bullet b = new Bullet();
		b.x = x;
		b.y = y;
		b.velocity.set(v.setLength(4f));
		b.AddSelf();
	}

	@Override
	public void Draw(){
		
		renderer.layer("ship", x, y).setLayer(1).setRotation(velocity.angle() - 90);
	}

}
