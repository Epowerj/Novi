package net.pixelstatic.novi.server;

import java.util.HashSet;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.network.packets.WorldUpdatePacket;

public class NoviUpdater{
	NoviServer server;
	private boolean isRunning = true;
	final int maxfps = 60;
	int frameid;
	float delta = 1f;
	long lastFpsTime;
	HashSet<Long> collided = new HashSet<Long>(); //used for storing collisions each frame so entities don't collide twice

	void Loop(){
		for(Entity entity : Entity.entities.values()){
			entity.Update();
			entity.serverUpdate();
			if(entity instanceof SolidEntity){
				checkCollisions((SolidEntity)entity);
			}
			if(entity instanceof Player){
				sendSync((Player)entity);
				if(frameid % 120 == 0) pingPlayer((Player)entity);
			}
		}
	}

	void checkCollisions(SolidEntity entity){
		for(Entity other : Entity.entities.values()){
			if(!inRange(entity,other,10 + entity.material.getRectangle().width) || other.equals(entity)  || !(other instanceof SolidEntity)) continue;
			if(!collided.contains(other.GetID())){
				SolidEntity othersolid = (SolidEntity)other;
				if(othersolid.collides(entity) && entity.collides(othersolid)){
					collisionEvent(entity, othersolid);
					collided.add(entity.GetID());
				}
			}
		}
	}
	
	boolean inRange(Entity a, Entity b, float rad){
		return Math.abs(a.x - b.x) < rad && Math.abs(a.y - b.y) < rad;
	}

	void collisionEvent(SolidEntity entitya, SolidEntity entityb){
		entitya.collisionEvent(entityb);
		entityb.collisionEvent(entitya);
	}

	void sendSync(Player player){
		WorldUpdatePacket worldupdate = new WorldUpdatePacket();
		worldupdate.health = player.health;
		for(Entity other : Entity.entities.values()){
			if(other.equals(player) || !(other instanceof Syncable) || (other instanceof TimedSyncable && !((TimedSyncable)other).sync()) || !other.loaded(player.x, player.y)) continue;
			Syncable sync = (Syncable)other;
			worldupdate.updates.put(other.GetID(), sync.writeSync());
		}
		server.server.sendToTCP(player.connectionID(), worldupdate);
	}
	
	public void pingPlayer(Player player){
		player.connection.updateReturnTripTime();
	}

	public float delta(){
		return delta;
	}

	public void run(){
		int fpsmillis = 1000 / maxfps;
		while(isRunning){
			long start = System.currentTimeMillis();
			Loop();
			frameid ++;
			//if(frame % 60 == 0)Novi.log(delta + " | " + Entity.entities.size());
			long end = System.currentTimeMillis();
			
			try{
				if(end - start <= fpsmillis) Thread.sleep(fpsmillis - (end - start));
			}catch(Exception e){
				e.printStackTrace();
			}
			long sleepend = System.currentTimeMillis();
			delta = (sleepend - start) / 1000f * 60f;
		}

	}

	public NoviUpdater(NoviServer server){
		this.server = server;
	}
}
