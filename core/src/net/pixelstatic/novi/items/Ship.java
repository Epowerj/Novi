package net.pixelstatic.novi.items;

import net.pixelstatic.novi.entities.Player;
import net.pixelstatic.novi.server.InputHandler;

public enum Ship{
	arrowhead{
		final float reload2 = 120f;
		
		{
			speed = 0.2f;
			turnspeed = 10f;
			maxvelocity = 4f;
			shootspeed = 5;
			kiteDebuffMultiplier = 0.5f;
			maxHealth = 300;
		}
		
		public void handleInput(Player player, InputHandler input){
			if(input.leftMouseDown() && player.reload <= 0){
				player.bullet(ProjectileType.bullet);
				player.reload = player.getShip().getShootspeed();
			}
			if(input.rightMouseDown() && player.altreload <= 0){
				player.bullet(ProjectileType.mine);
				player.altreload = reload2;
			}else if(input.rightMouseDown()){
				if(reload2 - player.altreload < 20 && ((int)((reload2 - player.altreload) % 4) == 0)){
					player.bullet(ProjectileType.mine);
				}
			}

		}
	};

	protected boolean spin;
	protected float speed;
	protected float turnspeed;
	protected float maxvelocity;
	protected float shootspeed;
	protected float kiteDebuffMultiplier;
	protected int maxHealth = 100;

	public void handleInput(Player player, InputHandler input){

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

	public int getMaxhealth(){
		return maxHealth;
	}

	public float getKiteDebuffMultiplier(){
		return kiteDebuffMultiplier;
	}
}
