package net.pixelstatic.novi.entities;

public class Target extends SolidEntity{
	
	public Target(){
		material.getRectangle().setSize(20);
	}

	@Override
	public void Update(){
		
	}

	@Override
	public void Draw(){
		renderer.layer("tile", x, y);
	}

}
