package net.pixelstatic.novi.entities.effects;

import net.pixelstatic.novi.sprites.Layer.LayerType;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;

public class BreakParticle extends Effect implements Disposable{
	private String regionName;
	private transient TextureRegion region;
	private transient boolean init;
	private transient Texture texture;
	private transient Pixmap pixmap;
	
	{
		lifetime = 100;
	}
	
	public BreakParticle(String region){
		this.regionName = region;
	}
	
	public BreakParticle(){};
	
	@Override
	public void Draw(){
		if(!init){
			init();
			init = true;
		}
		renderer.layer(x, y).setType(LayerType.TEXTURE).setTexture(texture);
	}
	
	public void removeEvent(){
		this.dispose();
	}
	
	public void init(){
		region = renderer.atlas.findRegion(regionName);
		Pixmap regionpixmap = renderer.atlas.getPixmapOf(region);
		pixmap = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Format.RGBA8888);
		for(int x = region.getRegionX(); x < region.getRegionX() + region.getRegionWidth(); x ++){
			for(int y = region.getRegionY(); y < region.getRegionY() + region.getRegionHeight(); y ++){
				pixmap.drawPixel(x - region.getRegionX(), y - region.getRegionY(),
						regionpixmap.getPixel(x, y));
			}
		}
		texture = new Texture(pixmap);
	}

	@Override
	public void dispose(){
		texture.dispose();
		pixmap.dispose();
	}
}
