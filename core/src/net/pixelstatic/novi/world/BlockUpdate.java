package net.pixelstatic.novi.world;

public class BlockUpdate{
	public int x, y, health;
	public Material material;

	public BlockUpdate(){

	}

	public BlockUpdate(Material material, int x, int y, int damage){
		this.x = x;
		this.y = y;
		this.health = damage;
		this.material = material;
	}
	
	public void apply(Block[][] blocks){
		Block block = blocks[x][y];
		block.setMaterial(material);
		block.health = health;
	}
}
