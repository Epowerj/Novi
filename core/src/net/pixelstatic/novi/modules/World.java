package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;

import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class World extends Module{
	public TiledMap map;

	@Override
	public void Init(){
		map = new TmxMapLoader().load("maps/world1.tmx"); //load world 1 map
		//initialize renderer's tiled map renderer with this map
		GetModule(Renderer.class).maprenderer = new OrthogonalTiledMapRenderer(map, GetModule(Renderer.class).batch);
	}

	@Override
	public void Update(){
		
	}
	
	public World(Novi n){
		super(n);
	}
}
