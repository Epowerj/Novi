package net.pixelstatic.novi.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.Player;
import net.pixelstatic.novi.sprites.Layer;
import net.pixelstatic.novi.sprites.LayerList;
import net.pixelstatic.novi.sprites.NoviAtlas;

public class Renderer extends Module {
	SpriteBatch batch; //novi's batch
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
	}
	
	public void Init(){
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight()/ scale);
		player = GetModule(ClientData.class).player;
	}
	
	@Override
	public void Update() {
		updateCamera();
		batch.setProjectionMatrix(camera.combined); //make the batch use the camera projection
		clearScreen();
		batch.begin();
		drawWorld();
		drawLayers();
		batch.end();
	}
	
	void clearScreen(){
		Gdx.gl.glClearColor(0, 0, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	void drawWorld(){
		layer("ship", player.x, player.y);
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
		camera.setToOrtho(true, width / scale, height/ scale); //resize camera
	}
	
	
	public Layer layer(String region, float x, float y){
		return layers.addLayer().set(region, x, y);
	}
	
	//utility/shortcut draw method
	public void draw(String region, float x, float y){
		batch.draw(atlas.findRegion(region), x - atlas.RegionWidth(region) / 2, y - atlas.RegionHeight(region) / 2);
	}

}
