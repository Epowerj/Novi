package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.items.*;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.server.NoviServer;
import net.pixelstatic.novi.sprites.LayerType;
import net.pixelstatic.novi.utils.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Player extends FlyingEntity implements Syncable{
	public transient int connectionid;
	public transient boolean client = false;
	public String name;
	
	public boolean shooting, valigned = true; //used for aligning the rotation after you shoot and let go of the mouse
	public float rotation = 0;
	public float reload;
	transient InterpolationData data = new InterpolationData();
	public transient InputHandler input;
	
	private transient Ship ship = new ArrowheadShip();

	{
		material.drag = 0.01f;
	}

	@Override
	public void Update(){
		if(NoviServer.active) return; //don't want to do stuff like getting the mouse angle on the server, do we?
		if(!client)data.update(this);
		
		UpdateVelocity();
		velocity.limit(ship.getMaxvelocity() * kiteChange());
		if(reload > 0) reload -= delta();
		if(rotation > 360f && !ship.getSpin()) rotation -= 360f;
		if(rotation < 0f && !ship.getSpin()) rotation += 360f;
		
		if(shooting){
			rotation = Angles.MoveToward(rotation, Angles.mouseAngle(), ship.getTurnspeed());
		}else{
			//align player rotation to velocity rotation
			if( !valigned) rotation = Angles.MoveToward(rotation, velocity.angle(), ship.getTurnspeed());
		}
	}
	
	//don't want to hit other players or other bullets
	public boolean collides(SolidEntity other){
		return super.collides(other) && !(other instanceof Player || other instanceof Bullet);
	}
	
	
	public Ship getShip(){
		return ship;
	}

	@Override
	public void serverUpdate(){
		input.update();
	}
	
	public Player(){
		if(NoviServer.active) input = new InputHandler(this);
	}

	public float kiteChange(){
		if(!shooting){
			return 1f;
		}else{
			return 1f - 
					Angles.angleDist(rotation, velocity.angle()) / 
					(180f * 1f / ship.getKiteDebuffMultiplier());
		}
	}

	public void move(float angle){
		velocity.add(new Vector2(1f, 1f).setAngle(angle).setLength(ship.getSpeed()));
	}
	
	public void shoot(){
		Vector2 v = new Vector2(1f, 1f).setAngle(rotation);
		Bullet b = new Bullet();
		b.x = x;
		b.y = y;
		b.velocity.set(v.setLength(4f));
		b.AddSelf();
		reload = ship.getShootspeed();
	}

	public float getSpriteRotation(){
		return ( !shooting && valigned) ? velocity.angle() - 90 : this.rotation - 90;
	}

	@Override
	public void Draw(){
		renderer.layer("ship", x, y).setLayer(1).setRotation(client ? getSpriteRotation() : rotation);
		if(!client) renderer.layer(x, y +14 ).setScale(0.2f).setColor(Color.GOLD).setLayer(2f).setType(LayerType.TEXT).setText(name); //draw player name
	}

	@Override
	public SyncBuffer writeSync(){
		return new PlayerSyncBuffer(GetID(), x, y, rotation, velocity);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		PlayerSyncBuffer sync = (PlayerSyncBuffer)buffer;
		velocity = sync.velocity;
		data.push(this, sync.x, sync.y, sync.rotation);
	}

}
