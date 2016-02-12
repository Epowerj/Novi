package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.sprites.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class Renderer extends Module {
	public SpriteBatch batch; //novi's batch
	BitmapFont font; //a font for displaying text
	OrthographicCamera camera; //a camera, seems self explanatory
	NoviAtlas atlas; //texture atlas
	LayerList layers;
	int scale = 5; //camera zoom/scale
	Player player; //player object from ClientData module
	
	
	public Renderer(Novi novi) {
		super(novi);
		batch = new SpriteBatch();
		atlas = new NoviAtlas(Gdx.files.internal("sprites/Novi.pack"));
		layers = new LayerList();
		Entity.renderer = this;
	}
	
	public void Init(){
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight()/ scale);
		player = GetModule(ClientData.class).player;
	}
	
	@Override
	public void Update() {
		updateCamera();
		batch.setProjectionMatrix(camera.combined); //make the batch use the camera projection
		drawWorld();
		clearScreen();
		batch.begin();
		drawLayers();
		batch.end();
		updateCamera();
	}
	
	void clearScreen(){
		Color clear = Color.SKY.cpy().sub(0.1f, 0.1f, 0.1f, 0f);
		Gdx.gl.glClearColor(clear.r, clear.g, clear.b, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	void drawWorld(){
		//TODO draw world / background
		layer("error", 0, 0);
	}
	
	//sorts layer list, draws all layers and clears it
	void drawLayers(){
		layers.sort();
		for(int i = 0; i < layers.count; i ++){
			Layer layer = layers.layers[i];
			layer.Draw(this);
		}
		layers.clear();
	}
	
	void updateCamera(){
		camera.position.set(player.x, player.y, 0f);
		camera.update();
	}
	
	public void onResize(int width, int height) {
		camera.setToOrtho(false, width / scale, height/ scale); //resize camera
	}
	
	
	public Layer layer(String region, float x, float y){
		return layers.addLayer().set(region, x, y);
	}
	
	public void zoom(float amount){
		if(camera.zoom + amount < 0) return;
		camera.zoom += amount;
	}
	
	//utility/shortcut draw method
	public void draw(String region, float x, float y){
		batch.draw(atlas.findRegion(region), x - atlas.RegionWidth(region) / 2, y - atlas.RegionHeight(region) / 2);
	}
	
	public void draw(String region, float x, float y, float rotation){
		batch.draw(atlas.findRegion(region), 
				x - atlas.RegionWidth(region) / 2, 
				y - atlas.RegionHeight(region) / 2, 
				atlas.RegionWidth(region) / 2, 
				atlas.RegionHeight(region) / 2, 
				atlas.RegionWidth(region), 
				atlas.RegionHeight(region), 1f, 1f, rotation);
	}

}
