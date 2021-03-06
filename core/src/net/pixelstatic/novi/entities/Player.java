package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.entities.effects.*;
import net.pixelstatic.novi.items.*;
import net.pixelstatic.novi.modules.Network;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.network.packets.DeathPacket;
import net.pixelstatic.novi.server.*;
import net.pixelstatic.novi.sprites.Layer.LayerType;
import net.pixelstatic.novi.utils.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;

public class Player extends DestructibleEntity implements Syncable{
	public transient Connection connection;
	public transient boolean client = false;
	public String name;
	private float respawntime;

	public boolean shooting, valigned = true; //used for aligning the rotation after you shoot and let go of the mouse
	public float rotation = 0;
	public transient float reload, altreload = 0, ping;
	transient InterpolationData data = new InterpolationData();
	public transient InputHandler input;

	private Ship ship = Ship.arrowhead;

	{
		material.drag = 0.01f;
		material.getRectangle().setSize(7);
		health = ship.getMaxhealth();
	}

	@Override
	public void Update(){
		if(respawntime > 0){
			respawntime -= delta();
			if(respawntime <= 0){
				x = 0;
				y = 0;
				if(server != null) new Shockwave(20, 0.1f, 0.01f).setPosition(x, y).SendSelf();
			}
		}
		if(reload > 0) reload -= delta();
		if(altreload > 0) altreload -= delta();
		if(NoviServer.active) return; //don't want to do stuff like getting the mouse angle on the server, do we?
		if( !client) data.update(this);
		if(client || NoviServer.active)UpdateVelocity();
		//updateBounds();
		velocity.limit(ship.getMaxvelocity() * kiteChange());
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
		return respawntime <= 0 && super.collides(other) && !(other instanceof Player || (other instanceof Bullet && ((Bullet)other).shooter instanceof Player));
	}

	public Ship getShip(){
		return ship;
	}

	@Override
	public void serverUpdate(){
		input.update();
		if(frame() % 120 == 0) connection.updateReturnTripTime();
	}

	public Player(){
		if(NoviServer.active) input = new InputHandler(this);
	}

	public float kiteChange(){
		if( !shooting){
			return 1f;
		}else{
			return 1f - Angles.angleDist(rotation, velocity.angle()) / (180f * 1f / ship.getKiteDebuffMultiplier());
		}
	}

	public void move(float angle){
		velocity.add(new Vector2(1f, 1f).setAngle(angle).setLength(ship.getSpeed()));
	}

	public float getSpriteRotation(){
		return ( !shooting && valigned) ? velocity.angle() - 90 : this.rotation - 90;
	}
	
	public boolean loaded(float playerx, float playery){
		return true;
	}

	@Override
	public void Draw(){
		if(respawntime > 0) return;
		if(!client){
			renderer.layer("ship", x, y).setLayer(1).setRotation(client ? getSpriteRotation() : rotation).addShadow();
			renderer.layer(x, y + 14).setScale(0.2f).setColor(Color.GOLD).setLayer(2f).setType(LayerType.TEXT).setText(name); //draw player name
		}else{
			renderer.layer("ship", x, y).setLayer(1).setRotation(client ? getSpriteRotation() : rotation).addShadow();
		}
	}

	//dying is currently disabled
	@Override
	public void deathEvent(){
		if(server != null){
			new Shockwave(9f, 0.001f, 0.04f).setPosition(x, y).SendSelf();
			new BreakEffect("ship", 3.5f).setPosition(x, y).SendSelf();
			Effects.explosionCluster(x, y, 6, 16);
			Effects.shake(50f, 50f, x, y);
			health = ship.getMaxhealth();
			connection.sendTCP(new DeathPacket());
		}
		velocity.set(0,0);
		respawntime = 150;
	}
	
	//returns whether or not enemies should target this player
	public boolean isVisible(){
		return respawntime <= 0;
	}
	
	public boolean isDead(){
		return respawntime > 0;
	}

	//don't want the player entity getting removed
	@Override
	public boolean removeOnDeath(){
		return false;
	}
	
	public void bullet(ProjectileType type){
		Bullet b = new Bullet(type, rotation + 90);
		b.x = predictedX();
		b.y = predictedY();
		b.setShooter(this);
		b.AddSelf().SendSelf();
	}
	
	public float pingInFrames(){
		if(!NoviServer.active) return 0;
		return ((Network.ping*2f + connection.getReturnTripTime()) / 1000f) * delta() * 60f+1f;
	}
	
	public float predictedX(){
		return velocity.x * pingInFrames() + x;
	}
	
	public float predictedY(){
		return velocity.y * pingInFrames() + y;
	}
	
	public int connectionID(){
		return connection.getID();
	}

	@Override
	public SyncBuffer writeSync(){
		return new PlayerSyncBuffer(GetID(), x, y, rotation, respawntime, pingInFrames(), velocity);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		PlayerSyncBuffer sync = (PlayerSyncBuffer)buffer;
		velocity = sync.velocity;
		this.respawntime = sync.respawntime;
		this.ping = sync.ping;
		data.push(this, sync.x, sync.y, sync.rotation);
	}
}
