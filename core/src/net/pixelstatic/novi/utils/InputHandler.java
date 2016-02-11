package net.pixelstatic.novi.utils;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Queue;

public class InputHandler{
	public Player player;
	public Queue<InputType> inputqueue = new Queue<InputType>(6);
	InputType lastinput;
	boolean mousedown;
	int mousehold = 0;
	
	public InputHandler(Player player){
		this.player = player;
	}
	
	public void inputEvent(InputType type){
		if(type == InputType.CLICK_DOWN){
			mousedown = true;
		}else{
			mousedown = false;
		}
		lastinput = type;
		/*
		Vector2 v = new Vector2(1f, 1f).setAngle(rotation);
		Bullet b = new Bullet();
		b.x = x;
		b.y = y;
		b.velocity.set(v.setLength(4f));
		b.AddSelf().SendSelf();
		reload = ship.getShootspeed();
		*/
	}
	
	boolean mouseDown(){
		return lastinput == InputType.CLICK_DOWN;
	}
	
	public void update(){
		if(player.reload > 0)  player.reload -= Entity.server.delta();
		Novi.log(player.rotation);
		if(mouseDown() && player.reload <= 0){
			Vector2 v = new Vector2(1f, 1f).setAngle(player.rotation + 90);
			Bullet b = new Bullet();
			b.x = player.x;
			b.y = player.y;
			b.velocity.set(v.setLength(4f));
			b.AddSelf().SendSelf();
			player.reload = player.getShip().getShootspeed();
		}
		//while(inputqueue.size > 0){
		//	serverInput(inputqueue.removeFirst());
		//}
	}
}
