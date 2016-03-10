package net.pixelstatic.novi.sprites;

import net.pixelstatic.novi.modules.Renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Layer implements Comparable<Layer>{
	public Color color = Color.WHITE;
	public float layer, x, y, rotation, scale = 1f;
	public String region;
	public LayerType type = LayerType.SPRITE;
	public String text;

	public void Draw(Renderer renderer){
		renderer.batch.setColor(color);
		if(type == LayerType.SPRITE){
			if(rotation == 0){
				renderer.draw(region, x, y);
			}else{
				renderer.draw(region, x, y, rotation);
			}
		}else if (type == LayerType.TEXT){
			GlyphLayout glyphs = renderer.getBounds(text);
			renderer.font.setUseIntegerPositions(false);
			renderer.font.setColor(color);
			renderer.font.getData().setScale(scale);
			renderer.font.draw(renderer.batch, text, x - glyphs.width /2, y - glyphs.height / 2);
		}
	}

	public Layer(){

	}

	public Layer(String region, float x, float y){
		this.region = region;
		this.x = x;
		this.y = y;
	}

	public Layer setType(LayerType type){
		this.type = type;
		return this;
	}
	
	public Layer setPosition(float x, float y){
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Layer translate(float x, float y){
		return setPosition(this.x+x,this.y+y);
	}
	
	public Layer setText(String text){
		this.text = text;
		return this;
	}
	
	public Layer setScale(float scale){
		this.scale = scale;
		return this;
	}

	public Layer setLayer(float layer){
		this.layer = layer;
		return this;
	}

	public Layer setColor(Color c){
		color = c;
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
		color = Color.WHITE;
		type = LayerType.SPRITE;
		scale = 1f;
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
