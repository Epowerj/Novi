package net.pixelstatic.novi.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.math.Vector2;

public class Player extends FlyingEntity{
	//TODO should be constant or private
	public boolean shooting, valigned = true; //used for aligning the rotation after you shoot and let go of the mouse
	public float speed = 0.2f;
	public float turnspeed = 10f;
	public float maxvelocity = 4f;
	public float shootspeed = 5;
	public float rotation = 0;
	public float kiteDebuffMultiplier = 180f;
	public float reload;

	{
		drag = 0.01f;
	}

	@Override
	public void Update(){
		UpdateVelocity();
		velocity.limit(maxvelocity * kiteChange());
		if(reload > 0) reload -= delta();
	}

	public float kiteChange(){
		if( !shooting) return 1f;
		return 1f - angleDist(rotation, velocity.angle()) / kiteDebuffMultiplier;
	}

	public void move(float angle){
		velocity.add(new Vector2(1f, 1f).setAngle(angle).setLength(speed));
		//MoveToward(velocity.angle(), angle);
	}

	public void accelerate(){
		//MoveToward(velocity.angle(), 90);
		if(velocity.isZero()) velocity.y = speed;
		if(velocity.len() < maxvelocity) velocity.setLength(velocity.len() + speed);
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
		velocity.setAngle(velocity.angle() + amount * turnspeed);
		if(velocity.isZero()) velocity.y = speed;
		if(velocity.len() < maxvelocity) velocity.setLength(velocity.len() + speed);
	}

	public void shoot(){
		//Vector2 v = new Vector2(Gdx.input.getX() - Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - Gdx.input.getY() - Gdx.graphics.getHeight() / 2);
		Vector2 v = new Vector2(1f, 1f).setAngle(rotation);
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

	float angleDist(float a, float b){
		return Math.min(ForwardDistance(a, b), BackwardDistance(a, b));
	}

	@Override
	public void Draw(){
		float rotation = (!shooting && valigned) ? velocity.angle() - 90 : this.rotation - 90;
		renderer.layer("ship", x, y).setLayer(1).setRotation(rotation);
	}

}
