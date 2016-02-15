package net.pixelstatic.novi.entities;

import net.pixelstatic.novi.network.*;
import net.pixelstatic.novi.utils.InterpolationData;

import com.badlogic.gdx.math.*;

public class Target extends DestructibleEntity implements Syncable{
	transient InterpolationData data = new InterpolationData();

	public Target(){
		material.getRectangle().setSize(20);
	}

	@Override
	public void Update(){
		data.update(this);
	}

	@Override
	public void serverUpdate(){
		x += Math.sin(y / 10f + 2) * delta();
		y += Math.sin(x / 10f + 2) * delta();
		Vector2 v = new Vector2(1f, 1f).setToRandomDirection();
		Bullet b = new Bullet();
		b.x = x;
		b.y = y;
		b.setShooter(this);
		b.velocity.set(v.setLength(4f));
		b.AddSelf().SendSelf();
	}

	public void deathEvent(){
		int radius = 30;
		for(int i = 0;i < 30;i ++){
			new ExplosionEffect().setPosition(x + MathUtils.random( -radius, radius), y + MathUtils.random( -radius, radius)).SendSelf();
		}
		new Target().setPosition(x + MathUtils.random( -radius*2, radius*2), y + MathUtils.random( -radius*2, radius*2)).AddSelf().SendSelf();
	}

	@Override
	public void Draw(){
		renderer.layer("tile", x, y);
	}

	@Override
	public SyncBuffer writeSync(){
		return new SyncBuffer(GetID(), x, y);
	}

	@Override
	public void readSync(SyncBuffer buffer){
		data.push(this, buffer.x, buffer.y, 0);
	}

}
