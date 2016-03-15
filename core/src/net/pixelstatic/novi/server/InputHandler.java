package net.pixelstatic.novi.server;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.items.ProjectileType;
import net.pixelstatic.novi.utils.InputType;

import com.badlogic.gdx.utils.Queue;

public class InputHandler{
	public Player player;
	public Queue<InputType> inputqueue = new Queue<InputType>(6);
	InputType lastinput;
	boolean leftmousedown, rightmousedown;
	int mousehold = 0;
	
	public InputHandler(Player player){
		this.player = player;
	}
	
	public void inputEvent(InputType type){
		if(type == InputType.RIGHT_CLICK_DOWN){
			rightmousedown = true;
		}else if (type == InputType.RIGHT_CLICK_UP){
			rightmousedown = false;
		}
		if(type == InputType.LEFT_CLICK_DOWN){
			leftmousedown = true;
		}else if (type == InputType.LEFT_CLICK_UP){
			leftmousedown = false;
		}
		lastinput = type;
	}
	
	boolean leftMouseDown(){
		return leftmousedown;
	}
	
	boolean rightMouseDown(){
		return rightmousedown;
	}
	
	float reload2 = 120f;
	
	public void update(){
		if(player.isDead()) return;
		if(leftMouseDown() && player.reload <= 0 ){
			bullet(ProjectileType.bullet);
			player.reload = player.getShip().getShootspeed();
		}
		if(rightMouseDown() && player.altreload <= 0){
			bullet(ProjectileType.mine);
			player.altreload = reload2;
		}else if(rightMouseDown()){
			if(reload2 - player.altreload < 20 && ((int)((reload2 - player.altreload) % 4) == 0)){
				bullet(ProjectileType.mine);
			}
		}
	}
	
	public void bullet(ProjectileType type){
		Bullet b = new Bullet(type, player.rotation + 90);
		b.x = player.x;
		b.y = player.y;
		b.setShooter(player);
		b.AddSelf().SendSelf();
	}
}
