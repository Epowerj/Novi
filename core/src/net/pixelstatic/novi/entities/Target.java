package net.pixelstatic.novi.entities;

public class Target extends Entity{

	@Override
	public void Update(){
		
	}

	@Override
	public void Draw(){
		renderer.layer("error", x, y);
	}

}
