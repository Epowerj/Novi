package net.pixelstatic.novi.entities;

import com.badlogic.gdx.graphics.Color;



public class ExplosionEffect extends Effect{
	{
		lifetime = 10;
	}
	@Override
	public void Draw(){
		renderer.layer("explosion", x, y).setColor(life >= lifetime /2 ? Color.BLACK : Color.WHITE);
	}
}
