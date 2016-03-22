package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.world.NoviMapRenderer;

import com.badlogic.gdx.maps.tiled.*;

public class World extends Module{
	public static final int tileSize = 14;
	public static final int worldSize = 100 * tileSize;
	public TiledMap map;

	@Override
	public void Init(){
		map = new TmxMapLoader().load("maps/world1.tmx"); //load world 1 map
		//initialize renderer's tiled map renderer with this map
		getModule(Renderer.class).maprenderer = new NoviMapRenderer(map, getModule(Renderer.class).batch);
	}
	//returns world width in pixels
	public int worldWidthPixels(){
		return worldSize;
	}
	
	//returns world width in pixels
	public int worldHeightPixels(){
		return worldSize;
	}

	@Override
	public void Update(){
		
	}
	
	public World(Novi n){
		super(n);
	}
}
