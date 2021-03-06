package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Player;
import net.pixelstatic.novi.network.packets.InputPacket;
import net.pixelstatic.novi.utils.InputType;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

public class Input extends Module implements InputProcessor{
	Player player; //player object from ClientData module

	public Input(Novi n){
		super(n);
	}

	public void Init(){
		player = getModule(ClientData.class).player;
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void Update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyJustPressed(Keys.G)) getModule(LogModule.class).writeFile();
		if(player.isDead()) return;
		float angle = -9;
		if(up()) angle = 90;
		if(left()) angle = 180;
		if(down()) angle = 270;
		if(right()) angle = 0;
		if(up() && right()) angle = 45;
		if(up() && left()) angle = 135;
		if(down() && right()) angle = 315;
		if(down() && left()) angle = 225;
		if(angle > -1) player.move(angle);
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && !Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
			player.shooting = true;
		}else{
			player.shooting = false;
		}
	}

	boolean left(){
		return Gdx.input.isKeyPressed(Keys.A);
	}

	boolean right(){
		return Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.E);
	}

	boolean up(){
		return Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.COMMA);
	}

	boolean down(){
		return Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.O);
	}

	void SendInput(InputType type){
		InputPacket input = new InputPacket();
		input.input = type;
		getModule(Network.class).client.sendTCP(input);
	}

	@Override
	public boolean keyDown(int keycode){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
	//		new BreakEffect("titanship").setPosition(player.x+30,player.y+30).AddSelf();
		player.rotation = player.velocity.angle();
		player.valigned = false;
		SendInput(button == Buttons.LEFT ? InputType.LEFT_CLICK_DOWN : InputType.RIGHT_CLICK_DOWN);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		SendInput(button == Buttons.LEFT ? InputType.LEFT_CLICK_UP : InputType.RIGHT_CLICK_UP);
		if(player.velocity.isZero(0.01f)){
			player.velocity.x = 0.01f;
			player.velocity.setAngle(player.rotation);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY){
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount){
		getModule(Renderer.class).zoom(amount / 10f);
		return false;
	}

	float ForwardDistance(float angle1, float angle2){
		if(angle1 > angle2){
			return angle1 - angle2;
		}else{
			return angle2 - angle1;
		}
	}

}
