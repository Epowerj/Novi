package net.pixelstatic.novi.server;

import java.util.HashSet;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.network.Syncable;
import net.pixelstatic.novi.network.packets.WorldUpdatePacket;

public class NoviUpdater{
	NoviServer server;
	private boolean isRunning = true;
	final int maxfps = 30;
	float delta = 1f;
	long lastFpsTime;
	HashSet<Long> collided = new HashSet<Long>(); //used for storing collisions each frame so entities don't collide twice

	void Loop(){
		collided.clear();
		for(Entity entity : Entity.entities.values()){
			entity.Update();
			entity.serverUpdate();
			if(entity instanceof SolidEntity){
				checkCollisions((SolidEntity)entity);
			}
			if(entity instanceof Player){
				sendSync((Player)entity);
			}
		}
	}

	void checkCollisions(SolidEntity entity){
		for(Entity other : Entity.entities.values()){
			if( !other.equals(entity) && other instanceof SolidEntity && !collided.contains(other.GetID())){
				SolidEntity othersolid = (SolidEntity)other;
				if(othersolid.collides(entity)){
					collisionEvent(entity, othersolid);
					collided.add(entity.GetID());
				}
			}
		}
	}

	void collisionEvent(SolidEntity entitya, SolidEntity entityb){
		entitya.collisionEvent(entityb);
		entityb.collisionEvent(entitya);
	}

	void sendSync(Player player){
		WorldUpdatePacket worldupdate = new WorldUpdatePacket();
		for(Entity other : Entity.entities.values()){
			if(other.equals(player) || !(other instanceof Syncable)) continue;
			Syncable sync = (Syncable)other;
			worldupdate.updates.put(other.GetID(), sync.writeSync());
		}
		server.server.sendToTCP(player.connectionid, worldupdate);
	}

	public float delta(){
		return delta;
	}

	public void run(){
		int fpsmillis = 1000 / maxfps;
		while(isRunning){
			long start = System.currentTimeMillis();
			Loop();
			long end = System.currentTimeMillis();
			delta = (fpsmillis - (end - start)) / 1000f * 60f;
			try{
				if(end - start <= fpsmillis) Thread.sleep(fpsmillis - (end - start));
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	public NoviUpdater(NoviServer server){
		this.server = server;
	}
}
