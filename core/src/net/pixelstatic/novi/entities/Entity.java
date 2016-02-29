package net.pixelstatic.novi.entities;

import java.util.concurrent.ConcurrentHashMap;

import net.pixelstatic.novi.Novi;
import net.pixelstatic.novi.modules.*;
import net.pixelstatic.novi.server.NoviServer;

import com.badlogic.gdx.Gdx;

public abstract class Entity{
	static public Novi novi;
	static public NoviServer server;
    static private long lastid;
    public static ConcurrentHashMap<Long, Entity> entities = new ConcurrentHashMap<Long, Entity>();
    public static Renderer renderer; // renderer reference for drawing things
    private long id;
    public float x,y;
    
    abstract public void Update();
    abstract public void Draw();
    
    //used to make entities not fly off the map
	public void updateBounds(){
		if(x < 0) x = 0;
		if(y < 0) y = 0;
		if(x > World.worldSize) x = World.worldSize;
		if(y > World.worldSize) y = World.worldSize;
	}
    
    public void onRecieve(){
    	
    }
    
    public Entity setPosition(float x, float y){
    	this.x = x;
    	this.y = y;
    	return this;
    }
    
    public void SendSelf(){
    	server.server.sendToAllTCP(this);
    }
    
    public Entity AddSelf(){
    	entities.put(id, this);
    	return this;
    }
    
    public void RemoveSelf(){
    	entities.remove(this.id);
    }
    
    public void resetID(long newid){
    	RemoveSelf();
    	this.id = newid;
    	AddSelf();
    	lastid = id+1;
    }
    
    public long GetID(){
    	return id;
    }
    
    public static Entity getEntity(long id){
    	return entities.get(id);
    }
    
    public static boolean entityExists(long id){
    	return entities.get(id) != null;
    }
    
    public void serverUpdate(){
    	//do nothing
    }
    
    public float delta(){
    	return server == null ? (Gdx.graphics.getDeltaTime() * 60f) : server.delta();
    }
    
    public Entity(){
    	id = lastid++;
    }
}
