package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.entities.effects.ExplosionEffect;
import net.pixelstatic.novi.items.ProjectileType;
import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.utils.InterpolationData;

import com.badlogic.gdx.math.*;

public abstract class Enemy extends DestructibleEntity implements Syncable{
	private static final float targettime = 40;
	private float targetcount = 0;
	public Player target;
	public int targetrange = 100;
	transient InterpolationData data = new InterpolationData();
	
	
	public void targetPlayers(int range){
		Player nearest = null;
		float neardist = Float.MAX_VALUE;
		for(Entity entity : entities.values()){
			if(entity instanceof Player){
				float dist = Vector2.dst(entity.x, entity.y, x, y);
				if(dist < neardist){
					neardist = dist;
					nearest = (Player)entity;
				}
			}
		}
		target = nearest;
	}
	
	public void tryRetarget(){
		if(targetcount > 0){
			targetcount -= delta();
		}else{
			targetPlayers(targetrange);
			targetcount = targettime;
		}
	}
	
	public boolean collides(SolidEntity other){
		return !(other instanceof Enemy) && super.collides(other) && !((other instanceof Bullet && ((Bullet)other).shooter instanceof Enemy));
	}
	
	public void deathEvent(){
		int radius = 20;
		for(int i = 0;i < 10;i ++){
			new ExplosionEffect().setPosition(x + MathUtils.random( -radius, radius), y + MathUtils.random( -radius, radius)).SendSelf();
		}
	}
	
	public void shoot(ProjectileType type, float angle){
		Bullet bullet = new Bullet(type, angle);
		bullet.setPosition(x, y);
		bullet.setShooter(this);
		bullet.AddSelf().SendSelf();
	}
	
	public void Update(){
		UpdateVelocity();
	}

	@Override
	public void serverUpdate(){
		tryRetarget();
		behaviorUpdate();
	}
	
	@Override
	public SyncBuffer writeSync(){
		return new EnemySyncBuffer(GetID(), x, y, velocity);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		EnemySyncBuffer sync = (EnemySyncBuffer)buffer;
		velocity = sync.velocity;
		data.push(this, sync.x, sync.y, 0);
	}
	
	abstract public void behaviorUpdate();
}
