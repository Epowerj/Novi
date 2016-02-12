package net.pixelstatic.novi.network;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.items.*;
import net.pixelstatic.novi.network.packets.*;
import net.pixelstatic.novi.utils.InputType;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;

public class Registrator{
	public static void register(Kryo k){
		k.register(ConnectPacket.class);
		k.register(DataPacket.class);
		k.register(PositionPacket.class);
		k.register(WorldUpdatePacket.class);
		k.register(EntityRemovePacket.class);
		k.register(InputPacket.class);
		k.register(SyncBuffer.class);
		k.register(PlayerSyncBuffer.class);
		k.register(Ship.class);
		k.register(ArrowheadShip.class);
		k.register(Item.class);
		k.register(InputType.class);
		k.register(Entity.class);
		k.register(FlyingEntity.class);
		k.register(Bullet.class);
		k.register(ExplosionEffect.class);
		k.register(Target.class);
		k.register(Player.class);
		k.register(Vector2.class);
		k.register(HashMap.class);
		k.register(ConcurrentHashMap.class);
		k.register(Long.class);
	}
}
