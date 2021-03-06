package net.pixelstatic.novi.server;

import net.pixelstatic.novi.entities.Player;
import net.pixelstatic.novi.utils.InputType;

import com.badlogic.gdx.utils.Queue;

public class InputHandler{
	public Player player;
	public Queue<InputType> inputqueue = new Queue<InputType>(6);
	//private InputType lastinput;
	private boolean leftmousedown, rightmousedown;
	//private int mousehold = 0;
	
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
		//lastinput = type;
	}
	
	public boolean leftMouseDown(){
		return leftmousedown;
	}
	
	public boolean rightMouseDown(){
		return rightmousedown;
	}
	
	
	public void update(){
		if(player.isDead()) return;
		player.getShip().handleInput(player, this);
	}
}
