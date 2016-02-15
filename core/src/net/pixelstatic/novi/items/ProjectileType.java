package net.pixelstatic.novi.items;

import net.pixelstatic.novi.entities.Bullet;

public enum ProjectileType{
	bullet;
	
	int getLifetime(){
		return 100;
	}
	
	void draw(Bullet bullet){
		
	}
}
