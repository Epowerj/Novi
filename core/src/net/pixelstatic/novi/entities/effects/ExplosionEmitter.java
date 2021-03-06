package net.pixelstatic.novi.entities.effects;

import net.pixelstatic.novi.entities.Entity;

import com.badlogic.gdx.math.MathUtils;

public class ExplosionEmitter extends Entity{
	float radius, chance, lifetime, life, drag = 0.01f;
	
	public ExplosionEmitter(){
		
	}
	
	public ExplosionEmitter(float lifetime, float chance, float radius){
		this.lifetime = lifetime;
		this.chance = chance;
		this.radius = radius;
	}
	
	@Override
	public void Draw(){
	}
	
	@Override
	public void Update(){
		life += delta();
		if(life > lifetime) RemoveSelf();
	}

	@Override
	public void serverUpdate(){
		if(MathUtils.randomBoolean(chance)){
			new ExplosionEffect().setPosition(x+MathUtils.random(-radius, radius),  y+MathUtils.random(-radius, radius)).SendSelf();
		}
		chance -= drag;
	}

}
