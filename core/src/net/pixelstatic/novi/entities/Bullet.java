package net.pixelstatic.novi.entities;


public class Bullet extends FlyingEntity{
	{
		drag = 0;
	}
	@Override
	public void Update(){
		UpdateVelocity();
	}

	@Override
	public void Draw(){
		renderer.layer("bullet", x, y).setLayer(0.5f).setRotation(velocity.angle() - 90);
	}

}
