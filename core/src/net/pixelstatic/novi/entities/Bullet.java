package net.pixelstatic.novi.entities;


public class Bullet extends FlyingEntity{

	@Override
	public void Update(){
		UpdateVelocity();
	}

	@Override
	public void Draw(){
		renderer.layer("bullet", x, y);
	}

}
