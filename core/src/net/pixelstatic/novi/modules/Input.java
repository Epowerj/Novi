package net.pixelstatic.novi.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Player;

public class Input extends Module implements InputProcessor{
	Player player; //player object from ClientData module

	public Input(Novi n){
		super(n);
	}

	public void Init(){
		player = GetModule(ClientData.class).player;
	}

	@Override
	public void Update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		float angle = -9;
		
		if(Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.COMMA)) angle = 90;
		if(Gdx.input.isKeyPressed(Keys.A)) angle = 180;
		if(Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.O)) angle = 270;
		if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.E)) angle = 0;
		if(angle > -1)player.move(angle);
		
		/*
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();
		if(Gdx.input.isKeyPressed(Keys.W)) player.accelerate();
		if(Gdx.input.isKeyPressed(Keys.A)) player.moveLeft();
		if(Gdx.input.isKeyPressed(Keys.S)) player.deccelerate();
		if(Gdx.input.isKeyPressed(Keys.D)) player.moveRight();
		*/
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && player.reload <= 0){
			player.shoot();
		}
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
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		// TODO Auto-generated method stub
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
		GetModule(Renderer.class).zoom(amount / 10f);
		return false;
	}

}
