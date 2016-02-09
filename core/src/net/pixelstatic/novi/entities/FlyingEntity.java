package net.pixelstatic.novi.entities;

import com.badlogic.gdx.math.Vector2;

public abstract class FlyingEntity extends Entity{
	float mass = 1f;
	float drag = 0.08f;
	Vector2 velocity = new Vector2();
	
	//updates velocity and position
	void UpdateVelocity(){
		x += velocity.x;
		y += velocity.y;
		velocity.scl(1f - drag);
	}
}
