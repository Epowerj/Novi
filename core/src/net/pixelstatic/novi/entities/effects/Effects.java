package net.pixelstatic.novi.entities.effects;

import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.network.packets.EffectPacket;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Server;

//static utility class for making flashy effects
//NOTE: do NOT use clientside - bad things will happen!
public class Effects{
	public static void shake(float duration, float intensity, float x, float y){
		server().sendToAllTCP(new EffectPacket().shake(duration, intensity, x, y));
	}

	public static void explosion(float x, float y){
		new ExplosionEffect().setPosition(x, y).SendSelf();
	}

	public static void explosionCluster(float x, float y, int amount, float radius){
		for(int i = 0;i < amount;i ++)
			explosion(x + MathUtils.random( -radius, radius), y + MathUtils.random( -radius, radius));
	}

	private static Server server(){
		return Entity.server.server;
	}
}
