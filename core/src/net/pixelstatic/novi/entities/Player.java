package net.pixelstatic.novi.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Player extends FlyingEntity{
	float speed = 0.2f;
	float turnspeed = 7f;
	float maxvelocity = 4f;
	float shootspeed = 5;
	public float reload;
	
	{
		drag = 0.01f;
	}

	@Override
	public void Update(){
		UpdateVelocity();
		if(reload > 0) reload -= delta();
	}
	
	public void move(float angle){
		MoveToward(velocity.angle(), angle);
	}

	public void accelerate(){
		//MoveToward(velocity.angle(), 90);
			if(velocity.isZero()) velocity.y = speed;
			if(velocity.len() < maxvelocity)velocity.setLength(velocity.len() + speed);
	}

	public void deccelerate(){
		velocity.setLength(velocity.len() * 0.95f);
	}

	public void moveLeft(){
		//MoveToward(velocity.angle(), 180);
		velocity.setAngle(velocity.angle() + turnspeed);
	}

	public void moveRight(){
		//MoveToward(velocity.angle(), 0);
		velocity.setAngle(velocity.angle() - turnspeed);
	}

	public void turn(float amount){
		velocity.setAngle(velocity.angle() + amount*turnspeed);
		if(velocity.isZero()) velocity.y = speed;
		if(velocity.len() < maxvelocity)velocity.setLength(velocity.len() + speed);
	}

	public void shoot(){
		Vector2 v = new Vector2(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - Gdx.graphics.getHeight() / 2);
		Bullet b = new Bullet();
		b.x = x;
		b.y = y;
		b.velocity.set(v.setLength(4f));
		b.AddSelf();
		reload = shootspeed;
	}

	float ForwardDistance(float angle1, float angle2){
		if(angle1 > angle2){
			return angle1 - angle2;
		}else{
			return angle2 - angle1;
		}
	}

	float BackwardDistance(float angle1, float angle2){
		return 360 - ForwardDistance(angle1, angle2);
	}

	void MoveToward(float angle, float to){
		if(angle > to){
			if(BackwardDistance(angle, to) > ForwardDistance(angle, to)){
				turn( -1);
			}else{
				turn(1);
			}
		}else{
			if(BackwardDistance(angle, to) < ForwardDistance(angle, to)){
				turn( -1);
			}else{
				turn(1);
			}
		}
	}

	@Override
	public void Draw(){
		renderer.layer("ship", x, y).setLayer(1).setRotation(velocity.angle() - 90);
	}

}
