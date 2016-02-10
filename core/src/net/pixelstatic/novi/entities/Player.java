package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.server.NoviServer;
import net.pixelstatic.novi.utils.Angles;

import com.badlogic.gdx.math.Vector2;

public class Player extends FlyingEntity implements Syncable{
	public static final boolean spin = false; //whee!
	public static final float speed = 0.2f;
	public static final float turnspeed = 10f;
	public static final float maxvelocity = 4f;
	public static final float shootspeed = 5;
	public static final float kiteDebuffMultiplier = 0.7f;
	public transient int connectionid;
	public transient boolean client = false;
	public boolean shooting, valigned = true; //used for aligning the rotation after you shoot and let go of the mouse
	public float rotation = 0;
	public float reload;

	{
		drag = 0.01f;
	}

	@Override
	public void Update(){
		UpdateVelocity();
		if(NoviServer.active) return; //don't want to do stuff like getting the mouse angle on the server, do we?
		velocity.limit(maxvelocity * kiteChange());
		if(reload > 0) reload -= delta();
		if(rotation > 360f && !spin) rotation -= 360f;
		if(rotation < 0f && !spin) rotation += 360f;
		if(shooting){
			rotation = Angles.MoveToward(rotation, Angles.mouseAngle(), turnspeed);
		}else{
			//align player rotation to velocity rotation
			if( !valigned) rotation = Angles.MoveToward(rotation, velocity.angle(), turnspeed); 
		}
	}

	public float kiteChange(){
		if( !shooting) return 1f;
		return 1f - Angles.angleDist(rotation, velocity.angle()) / (180f  * 1f/ kiteDebuffMultiplier);
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
	
	public float getSpriteRotation(){
		return ( !shooting && valigned) ? velocity.angle() - 90 : this.rotation - 90;
	}

	@Override
	public void Draw(){
		renderer.layer("ship", x, y).setLayer(1).setRotation(client ? getSpriteRotation() : rotation);
	}

	@Override
	public SyncBuffer writeSync(){
		return new PlayerSyncBuffer(GetID(), x, y, getSpriteRotation(), velocity);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		PlayerSyncBuffer sync = (PlayerSyncBuffer)buffer;
		x = sync.x;
		y = sync.y;
		rotation = sync.rotation;
		velocity = sync.velocity;
	}

}
