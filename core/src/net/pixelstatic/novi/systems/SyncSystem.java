package net.pixelstatic.novi.systems;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.network.Syncable.GlobalSyncable;
import net.pixelstatic.novi.network.packets.WorldUpdatePacket;

public class SyncSystem extends EntitySystem{

	@Override
	public void update(Entity entity){
		Player player = (Player)entity;
		WorldUpdatePacket worldupdate = new WorldUpdatePacket();
		worldupdate.health = player.health;
		for(Entity other : Entity.entities.values()){
			if(other.equals(player) || !(other instanceof Syncable) || (other instanceof TimedSyncable && !((TimedSyncable)other).sync()) || (!other.getClass().isAnnotationPresent(GlobalSyncable.class) && !other.loaded(player.x, player.y))) continue;
			Syncable sync = (Syncable)other;
			worldupdate.updates.put(other.GetID(), sync.writeSync());
		}
		Entity.server.server.sendToTCP(player.connectionID(), worldupdate);
	}
	
	public boolean accept(Entity entity){
		return entity instanceof Player;
	}

}
