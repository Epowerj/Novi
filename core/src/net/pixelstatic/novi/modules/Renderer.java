package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.sprites.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;

public class Renderer extends Module {
	public SpriteBatch batch; //novi's batch
	public BitmapFont font; //a font for displaying text
	public OrthogonalTiledMapRenderer maprenderer; //used for rendering the map
	public Matrix4 matrix; // matrix used for rendering gui and other things
	GlyphLayout layout; // used for getting font bounds
	public OrthographicCamera camera; //a camera, seems self explanatory
	NoviAtlas atlas; //texture atlas
	LayerList layers;
	int scale = 5; //camera zoom/scale
	int pixelscale = 1; // pixelation scale
	public Player player; //player object from ClientData module
	World world; // world module
	FrameBuffer buffer;
	
	public Renderer(Novi novi) {
		super(novi);
		matrix = new Matrix4();
		batch = new SpriteBatch();
		atlas = new NoviAtlas(Gdx.files.internal("sprites/Novi.pack"));
		layers = new LayerList();
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		layout = new GlyphLayout();
		buffer = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth() / pixelscale, Gdx.graphics.getHeight() / pixelscale, false);
		buffer.getColorBufferTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		Entity.renderer = this;
	}
	
	public void Init(){
		camera = new OrthographicCamera(Gdx.graphics.getWidth() / scale, Gdx.graphics.getHeight()/ scale);
		player = getModule(ClientData.class).player;
		world = getModule(World.class);
	}
	
	@Override
	public void Update() {
		updateCamera();
		batch.setProjectionMatrix(camera.combined); //make the batch use the camera projection
		drawWorld();
		clearScreen();
		doRender();
		updateCamera();
	}
	
	void doRender(){
		clearScreen();
		maprenderer.setView(camera);
		maprenderer.render();
		batch.begin();
		drawLayers();
		batch.end();
		batch.setColor(Color.WHITE);
	}
	
	void clearScreen(){
		Color clear = Color.SKY.cpy().sub(0.1f, 0.1f, 0.1f, 0f);
		Gdx.gl.glClearColor(clear.r, clear.g, clear.b, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	void drawWorld(){

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
		limitCamera();
		camera.update();
	}
	
	void limitCamera(){
		//limit camera position, snap to sides
		if(camera.position.x - camera.viewportWidth/2*camera.zoom < 0)
			camera.position.x = camera.viewportWidth/2*camera.zoom;
		if(camera.position.y - camera.viewportHeight/2*camera.zoom < 0)
			camera.position.y = camera.viewportHeight/2*camera.zoom;
		if(camera.position.x + camera.viewportWidth/2*camera.zoom > world.worldWidthPixels())
			camera.position.x = world.worldWidthPixels() - camera.viewportWidth/2*camera.zoom;
		if(camera.position.y + camera.viewportHeight/2*camera.zoom > world.worldHeightPixels())
			camera.position.y = world.worldHeightPixels() - camera.viewportHeight/2*camera.zoom;
	}
	
	public void onResize(int width, int height) {
		matrix.setToOrtho2D(0, 0, width, height);
		camera.setToOrtho(false, width / scale, height/ scale); //resize camera
	}
	
	
	public Layer layer(String region, float x, float y){
		return layers.addLayer().set(region, x, y);
	}
	
	public Layer layer(float x, float y){
		return layers.addLayer().setPosition(x, y);
	}
	
	public void zoom(float amount){
		if(camera.zoom + amount < 0 || (camera.zoom   + amount) * camera.viewportWidth > world.worldWidthPixels() ) return;
		
		camera.zoom += amount;
	}
	
	public GlyphLayout getBounds(String text){
		layout.setText(font, text);
		return layout;
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
