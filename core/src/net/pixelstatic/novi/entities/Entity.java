package net.pixelstatic.novi.entities;

import java.util.HashMap;

public abstract class Entity{
    static long lastid;
    static HashMap<Long, Entity> entities = new HashMap<Long, Entity>();
    private long id;
    public float x,y;
    
    abstract void Update();
    abstract void Draw();
    
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
