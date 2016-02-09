package net.pixelstatic.novi.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.sprites.NoviAtlas;

public class Renderer extends Module {
	SpriteBatch batch; //novi's batch
	BitmapFont font; //a font for displaying text
	OrthographicCamera camera; //a camera, seems self explanatory
	NoviAtlas atlas; //texture atlas
	int scale = 5; //camera zoom/scale
	
	
	public Renderer(Novi novi) {
		super(novi);
		batch = novi.batch;
		atlas = new NoviAtlas(Gdx.files.internal("sprites/Novi.pack"));
	}
	
	void drawWorld(){
		draw("tile", 0, 0);
		draw("ship", novi.player.x, novi.player.y);
	}
	
	@Override
	public void Update() {
		updateCamera();
		batch.setProjectionMatrix(camera.combined); //make the batch use the camera projection
		drawWorld();
	}
	
	public void Init(){
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight()/ scale);
	}
	
	void updateCamera(){
		camera.position.set(novi.player.x, novi.player.y, 0f);
		camera.update();
	}
	
	public void onResize(int width, int height) {
		camera.setToOrtho(true, width / scale, height/ scale); //resize camera
	}
	
	//utility/shortcut draw method
	void draw(String region, float x, float y){
		batch.draw(atlas.findRegion(region), x - atlas.RegionWidth(region) / 2, y - atlas.RegionHeight(region) / 2);
	}

}
