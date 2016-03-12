package net.pixelstatic.novi.entities;

public class DamageArea extends SolidEntity implements Damager{
	float lifetime =  100f, life;
	int damage = 2;
	
	public DamageArea(){
		
	}
	
	public DamageArea setDamage(int damage){
		this.damage = damage;
		return this;
	}
	
	public DamageArea(float lifetime, float size){
		setSize(size);
		this.lifetime = lifetime;
	}
	
	public DamageArea setSize(float width, float height){
		material.getRectangle().setSize(width, height);
		return this;
	}
	
	public DamageArea setSize(float size){
		return setSize(size,size);
	}
	
	@Override
	public void Update(){
		life += delta();
		if(life > lifetime)RemoveSelf();
	}

	@Override
	public void Draw(){
		
	}
	
	public boolean collides(SolidEntity other){
		return !(other instanceof Damager) && super.collides(other);
	}

	@Override
	public int damage(){
		return damage;
	}
}
