package net.pixelstatic.novi.items;

import net.pixelstatic.novi.entities.Bullet;
import net.pixelstatic.novi.modules.Renderer;

public enum ProjectileType{
	bullet;
	
	public int getLifetime(){
		return 100;
	}
	
	public void draw(Bullet bullet, Renderer renderer){
		renderer.layer("bullet", bullet.x, bullet.y).setLayer(0.5f).setRotation(bullet.velocity.angle() - 90);
	}
}
