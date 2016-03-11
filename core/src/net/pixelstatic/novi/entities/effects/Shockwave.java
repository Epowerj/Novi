package net.pixelstatic.novi.entities.effects;

public class Shockwave extends Effect{
	float scale = 0.01f, grow = 0.02f;
	
	{
		lifetime = 100;
	}
	
	@Override
	public void Draw(){
		renderer.layer("shockwave", x, y).setScale(scale);
		scale += grow * delta();
	}

}
