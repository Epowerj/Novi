package net.pixelstatic.novi.network.packets;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.modules.*;
import net.pixelstatic.novi.utils.WorldUtils;

public class EffectPacket{
	public static final float shakerange = 600f;
	public EffectType type = EffectType.shake;
	public float[] params;
	
	public EffectPacket shake(float duration, float intensity, float x, float y){
		params = new float[4];
		params[0] = duration;
		params[1] = intensity;
		params[2] = x;
		params[3] = y;
		type = EffectType.shake;
		return this;
	}
	
	public void apply(Novi novi){
		if(type == EffectType.shake){
			float range = 230f * (params[1]/20f+0.5f);
			float dist = WorldUtils.wrappedDist(novi.getModule(ClientData.class).player.x, novi.getModule(ClientData.class).player.y, params[2], params[3]);
			float scl = 1f - dist / range;
			novi.getModule(Renderer.class).shakeCamera(params[0]*scl, params[1]*scl);
		}
	}
	
	public enum EffectType{
		shake
	}
}
