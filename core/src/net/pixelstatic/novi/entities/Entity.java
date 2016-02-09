package net.pixelstatic.novi.entities;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;

import net.pixelstatic.novi.modules.Renderer;

public abstract class Entity{
    static long lastid;
    public static HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
    public static Renderer renderer; // renderer reference for drawing things
    private long id;
    public float x,y;
    
    abstract public void Update();
    abstract public void Draw();
    
    public void AddSelf(){
    	entities.put(id, this);
    }
    
    public void RemoveSelf(){
    	entities.remove(this.id);
    }
    
    public long GetID(){
    	return id;
    }
    
    public float delta(){
    	return Gdx.graphics.getDeltaTime() * 60f;
    }
    
    public Entity(){
    	id = lastid++;
    }
}
