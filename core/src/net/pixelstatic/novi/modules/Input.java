package net.pixelstatic.novi.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import net.pixelstatic.novi.Novi;

public class Input extends Module implements InputProcessor {
	public Input(Novi n) {
		super(n);
	}

	@Override
	public void Update() {
		if(Gdx.input.isKeyPressed(Keys.W)) novi.player.accelerate();
		if(Gdx.input.isKeyPressed(Keys.A)) novi.player.turn(1f);
		if(Gdx.input.isKeyPressed(Keys.S)) novi.player.deccelerate();
		if(Gdx.input.isKeyPressed(Keys.D)) novi.player.turn(-1f);
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
