package net.pixelstatic.novi.entities;

public abstract class Entity{
    static long lastid;
    long id;
    float x,y;
    
    public Entity(){
	id = lastid++;
    }
}
