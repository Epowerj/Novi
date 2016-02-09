package net.pixelstatic.novi.entities;

import java.util.HashMap;

public abstract class Entity{
    static long lastid;
    public static HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
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
    
    public Entity(){
    	id = lastid++;
    }
}
