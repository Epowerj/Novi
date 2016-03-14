package net.pixelstatic.novi.items;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.entities.effects.*;
import net.pixelstatic.novi.modules.Renderer;
import net.pixelstatic.novi.utils.Colors;

import com.badlogic.gdx.graphics.Color;

public enum ProjectileType{
	bullet{
		public void draw(Bullet bullet, Renderer renderer){
			renderer.layer("bullet", bullet.x, bullet.y).setLayer(0.5f).setRotation(bullet.velocity.angle() - 90);
		}
		
		public int getLifetime(){
			return 30;
		}
		
		public float getSpeed(){
			return 7;
		}
	},
	redbullet{
		public void draw(Bullet bullet, Renderer renderer){
			renderer.layer("dronebullet", bullet.x, bullet.y).setLayer(0.5f).setRotation(bullet.velocity.angle() - 90);
		}
		
		public int getLifetime(){
			return 60;
		}
		
		public float getSpeed(){
			return 4;
		}
		
		public int damage(){
			return 3;
		}
	},
	explosivebullet{
		public void draw(Bullet bullet, Renderer renderer){
			renderer.layer("explosivebullet", bullet.x, bullet.y).setLayer(0.5f).setRotation(bullet.velocity.angle() - 90);
		}
		
		public int getLifetime(){
			return 100;
		}
		
		public float getSpeed(){
			return 3;
		}
		
		public void destroyEvent(Bullet bullet){
			new Shockwave(8f, 0.001f, 0.02f).setPosition(bullet.x, bullet.y).SendSelf();
			new ExplosionEffect().setPosition(bullet.x, bullet.y).SendSelf();
			new DamageArea(30f, 16f).setPosition(bullet.x, bullet.y).AddSelf();
			Effects.shake(20f, 10f, bullet.x, bullet.y);
		}
		
		public void setup(Bullet bullet){
			bullet.material.getRectangle().setSize(5f);
		}
		
		public int damage(){
			return 15;
		}
	},
	mine{
		public void draw(Bullet bullet, Renderer renderer){
			renderer.layer("mine", bullet.x, bullet.y).setLayer(0.5f).setRotation(bullet.velocity.angle() - 90);
			renderer.layer("minecenter",bullet.x,bullet.y).setLayer(0.51f).setRotation(bullet.velocity.angle() - 90)
			.setColor(Colors.mix(Color.GOLD, Color.RED, bullet.life()/getLifetime()));
		}
		
		public int getLifetime(){
			return 200;
		}
		
		public float getSpeed(){
			return 0;
		}
		
		public void destroyEvent(Bullet bullet){
			//new Shockwave(8f, 0.001f, 0.02f).setPosition(bullet.x, bullet.y).SendSelf();
			//new ExplosionEffect().setPosition(bullet.x, bullet.y).SendSelf();
			new DamageArea(30f, 16f).setDamage(15).setPosition(bullet.x, bullet.y).AddSelf();
			Effects.explosionCluster(bullet.x, bullet.y, 4, 5f);
			Effects.shake(20f, 10f, bullet.x, bullet.y);
		}
		
		public void setup(Bullet bullet){
			bullet.material.getRectangle().setSize(10f);
		}
		
		public int damage(){
			return 5;
		}
		
		public boolean collideWithBases(){
			return false;
		}
	};
	
	public void setup(Bullet bullet){
		
	}
	
	public float getSpeed(){
		return 4;
	}
	
	public int getLifetime(){
		return 100;
	}
	
	public int damage(){
		return 1;
	}
	
	public boolean collideWithOtherProjectiles(){
		return false;
	}
	
	public boolean collide(){
		return true;
	}
	
	public boolean collideWithBases(){
		return true;
	}
	
	public void destroyEvent(Bullet bullet){
		
	}
	
	public void draw(Bullet bullet, Renderer renderer){
		renderer.layer(name(), bullet.x, bullet.y).setLayer(0.5f).setRotation(bullet.velocity.angle() - 90);
	}
}
