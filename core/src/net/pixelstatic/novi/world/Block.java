package net.pixelstatic.novi.world;

public class Block{
	private Material material = Material.air;
	public final int x,y;
	public int health;
	
	public Block(int x, int y, Material material){
		setMaterial(material);
		this.x = x;
		this.y = y;
	}
	
	public Block(){
		this.x = 0;
		this.y = 0;
	}
	
	public void setMaterial(Material mat){
		material = mat;
		health = material.health();
	}
	
	public Material getMaterial(){
		return material;
	}
	
	public float healthfrac(){
		return health / (float)material.health();
	}
	
	public boolean solid(){
		return material.solid();
	}
	
	public boolean empty(){
		return material == Material.air;
	}
}
