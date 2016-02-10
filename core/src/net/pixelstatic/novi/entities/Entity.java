package net.pixelstatic.novi.entities;

import java.util.HashMap;

import net.pixelstatic.novi.modules.Renderer;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;

public abstract class Entity{
    static private long lastid;
    public static HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
    public static Renderer renderer; // renderer reference for drawing things
    private long id;
    public float x,y;
    
    abstract public void Update();
    abstract public void Draw();
    
    public void onRecieve(){
    	
    }
    
    public void setPosition(float x, float y){
    	this.x = x;
    	this.y = y;
    }
    
    public void SendSelf(Server server){
    	server.sendToAllTCP(this);
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
    	
    }
    
    public float delta(){
    	return Gdx.graphics.getDeltaTime() * 60f;
    }
    
    public Entity(){
    	id = lastid++;
    }
}
