package net.pixelstatic.novi.entities.effects;

import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.network.packets.EffectPacket;

//static utility class for making flashy effects
//NOTE: do NOT use clientside - bad things will happen!
public class Effects{
	public static void shake(float duration, float intensity, float x, float y){
		Entity.server.server.sendToAllTCP(new EffectPacket().shake(duration, intensity, x, y));
	}
}
