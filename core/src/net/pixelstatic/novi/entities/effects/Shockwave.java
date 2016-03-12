package net.pixelstatic.novi.entities.effects;

import net.pixelstatic.novi.sprites.Layer;

import com.badlogic.gdx.graphics.Color;

public class Shockwave extends Effect{
	transient float scale = 0.001f, grow = 0.09f;
	
	{
		lifetime = 16;
	}
	
	@Override
	public void Draw(){
		Layer layer = renderer.layer("shockwave", x, y).setScale(scale);
		float scl = 2f;
		if(life > lifetime / scl) layer.setColor(new Color(1, 1f, 1f, 1f - (life - lifetime / 2f) / (lifetime / scl)));
		scale += grow * delta();
	}

}
