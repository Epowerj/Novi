package net.pixelstatic.novi.entities;



public class ExplosionEffect extends Effect{

	@Override
	public void Draw(){
		renderer.layer("explosion", x, y);
	}
}
