package net.pixelstatic.novi.items;

public abstract class Ship extends Item {
	protected boolean spin; //whee!
	protected float speed;
	protected float turnspeed;
	protected float maxvelocity;
	protected float shootspeed;
	protected float kiteDebuffMultiplier;

	public Ship(String itemName, int itemId) {
		super(itemName, itemId);
	}
	
	public boolean getSpin(){
		return spin;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public float getTurnspeed(){
		return turnspeed;
	}
	
	public float getMaxvelocity(){
		return maxvelocity;
	}
	
	public float getShootspeed(){
		return shootspeed;
	}
	
	public float getKiteDebuffMultiplier(){
		return kiteDebuffMultiplier;
	}
}
