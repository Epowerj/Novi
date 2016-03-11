package net.pixelstatic.novi.entities.effects;

import net.pixelstatic.novi.sprites.*;
import net.pixelstatic.novi.sprites.Layer.LayerType;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Disposable;

public class BreakEffect extends Effect implements Disposable{
	public int chunkamount = 7;
	private String regionName;
	private transient TextureRegion region;
	private transient boolean init;
	private transient Chunk[] chunks;
	private float velocityscl = 3f;
	private Vector2 offset = new Vector2(0,0);

	{
		lifetime = 120 + MathUtils.random(400);
	}

	class Chunk{
		public Vector2 velocity = new Vector2();
		float x, y, drag = 0.02f, rotation;
		int dir = MathUtils.randomSign();
		private Texture texture;
		private Pixmap pixmap;

		public Chunk(Vector2 velocity, Pixmap pixmap){
			this.pixmap = pixmap;
			this.velocity = velocity;
			texture = new Texture(pixmap);
		}

		public void draw(BreakEffect effect){
			Layer layer = renderer.layer(x + effect.x, y + effect.y).setType(LayerType.TEXTURE).setLayer(1.5f).setRotation(rotation).setTexture(texture);
			float scl = 4f;
			if(effect.life > effect.lifetime/scl)layer.setColor(new Color(1, 1, 1, 1f - (effect.life-effect.lifetime/2f) / (effect.lifetime/scl)));
			x += velocity.x * delta();
			y += velocity.y * delta();
			velocity.scl((float)Math.pow(1f - drag, delta()));
			rotation += dir*delta()*0.1f;
		}

		public void dispose(){
			texture.dispose();
			pixmap.dispose();
		}
	}

	public BreakEffect(String region){
		this.regionName = region;
	}
	
	public BreakEffect(String region, float velocity){
		this.regionName = region;
		velocityscl =velocity;
	}
	
	public BreakEffect(String region, Vector2 offset, float velocity){
		this.regionName = region;
		velocityscl = velocity;
		this.offset = offset;
	}

	public BreakEffect(){
	};

	@Override
	public void Draw(){
		if( !init){
			init();
			init = true;
		}
		for(Chunk chunk : chunks){
			chunk.draw(this);
		}
	}

	@Override
	public void removeEvent(){
		this.dispose();
	}

	public void init(){
		chunkamount = 2+MathUtils.random(5);
		region = renderer.atlas.findRegion(regionName);
		Pixmap regionpixmap = renderer.atlas.getPixmapOf(region);
		chunks = new Chunk[chunkamount];
		float[] angles = new float[chunkamount];
		float last = 0f;
		for(int i = 0;i < chunkamount;i ++){
			last = last + MathUtils.random(360f / chunkamount/2) + 360f / chunkamount/2f;
			angles[i] = last;
		}
		for(int i = 0;i < chunkamount;i ++){
			Pixmap pixmap = new Pixmap(region.getRegionWidth(), region.getRegionHeight(), Format.RGBA8888);

			float angle = angles[i];
			float lastangle = i == 0 ? angles[chunkamount - 1] : angles[i - 1];

			for(int x = region.getRegionX();x < region.getRegionX() + region.getRegionWidth();x ++){
				for(int y = region.getRegionY();y < region.getRegionY() + region.getRegionHeight();y ++){
					float relx = x - region.getRegionX(), rely = y - region.getRegionY();
					float rawangle = MathUtils.radDeg * MathUtils.atan2(rely - region.getRegionHeight() / 2f, relx - region.getRegionWidth() / 2f);
					float pixangle = rawangle < 0 ? rawangle + 360f : rawangle;
					if((pixangle > lastangle && pixangle < angle) || (i == 0 && pixangle > lastangle+90)) pixmap.drawPixel(x - region.getRegionX(), y - region.getRegionY(), regionpixmap.getPixel(x, y));
				}
			}
			chunks[i] = new Chunk(new Vector2(MathUtils.random(velocityscl)+offset.x, MathUtils.random(velocityscl)+offset.y).setAngle(MathUtils.random(-40f, 40f) +0.5f * (angle + lastangle)), pixmap);
		}
	}

	@Override
	public void dispose(){
		for(Chunk chunk : chunks){
			chunk.dispose();
		}
	}
}
