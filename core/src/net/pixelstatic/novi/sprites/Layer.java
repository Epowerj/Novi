package net.pixelstatic.novi.sprites;

import net.pixelstatic.novi.modules.Renderer;

public class Layer implements Comparable<Layer>{
	float layer, x, y, rotation;
	String region;
	
	public void Draw(Renderer renderer){
		if(rotation == 0){
			renderer.draw(region, x, y);
		}else{
			renderer.draw(region, x, y, rotation);
		}
	}
	
	public Layer(){
		
	}
	
	public Layer(String region, float x, float y){
		this.region = region;
		this.x = x;
		this.y = y;
	}
	
	public Layer setLayer(float layer){
		this.layer = layer;
		return this;
	}
	
	public Layer setRotation(float rotation){
		this.rotation = rotation;
		return this;
	}
	
	public Layer set(String region, float x, float y){
		this.region = region;
		this.x = x;
		this.y = y;
		return this;
	}
	
	public void clear(){
		region = "";
		layer = 0;
		x = 0;
		y = 0;
		rotation = 0;
	}

	@Override
	public int compareTo(Layer s){
		if(s.layer == this.layer){
			return 0;
		}else if(s.layer > this.layer){
			return -1;
		}else{
			return 1;
		}
	}
}
