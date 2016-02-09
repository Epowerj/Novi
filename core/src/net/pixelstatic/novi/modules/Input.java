package net.pixelstatic.novi.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Player;

public class Input extends Module implements InputProcessor {
	Player player; //player object from ClientData module
	
	public Input(Novi n) {
		super(n);
	}
	
	public void Init(){
		player = GetModule(ClientData.class).player;
	}

	@Override
	public void Update() {
		if(Gdx.input.isKeyPressed(Keys.W)) player.accelerate();
		if(Gdx.input.isKeyPressed(Keys.A)) player.turn(1f);
		if(Gdx.input.isKeyPressed(Keys.S)) player.deccelerate();
		if(Gdx.input.isKeyPressed(Keys.D)) player.turn(-1f);
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
