package net.pixelstatic.novi.world;

import net.pixelstatic.novi.entities.*;
import net.pixelstatic.novi.entities.effects.BreakEffect;
import net.pixelstatic.novi.entities.enemies.Drone;
import net.pixelstatic.novi.items.ProjectileType;
import net.pixelstatic.novi.sprites.Layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public enum Material{
	air, ironblock{
		public boolean solid(){
			return false;
		}
	},
	dronemaker{
		static final float buildtime = 200;
		static final int maxspawn = 30;
		
		public boolean solid(){
			return true;
		}

		public void draw(Block block, Base base, int x, int y){
			defaultDraw("dronemaker", block, base, x, y, false);
			defaultDraw("drone", block, base, x, y, false).setColor(new Color(1,1,1,block.reload/buildtime)).setRotation(180f).setLayer(-0.45f).translate(0,-1);
			Layer bar = defaultDraw("dronemakerbar", block, base, x, y, false).setLayer(-0.4f);
			bar.setPosition(bar.x, bar.y + (int)(Math.sin(Gdx.graphics.getFrameId()/10f)*6f-1f));
		}
		
		public void update(Block block, Base base){
			if(base.target == null || base.spawned > maxspawn) return;
			block.reload += Entity.delta();
			base.update(block.x,block.y);
			if(block.reload >= buildtime){
				Drone drone = (Drone)new Drone().setPosition(world(base,block.x),world(base,block.y));
				drone.velocity.y = -3;
				drone.AddSelf().SendSelf();
				drone.base = base;
				block.reload = 0;
				base.spawned ++;
			}
		}
	},
	turret{
		static final float reloadtime = 40;

		public boolean solid(){
			return true;
		}

		public void update(Block block, Base base){
			if(base.target != null){
				block.rotation = base.autoPredictTargetAngle(world(base, block.x), world(base, block.y), 5f) + 90;
				base.update(block.x, block.y);
				block.reload += Entity.delta();
				if(block.reload >= reloadtime){
					base.getShoot(ProjectileType.redbullet, block.rotation + 90).setPosition(world(base, block.x), world(base, block.y)).translate(3, 5).AddSelf().SendSelf();;					
					base.getShoot(ProjectileType.redbullet, block.rotation + 90).setPosition(world(base, block.x), world(base, block.y)).translate(-3, 5).AddSelf().SendSelf();;					
					
					block.reload = 0;
				}
			}
		}

		public void draw(Block block, Base base, int x, int y){
			defaultDraw("ironblock", block, base, x, y, false);
			defaultDraw("turret", block, base, x, y, false).setRotation(block.rotation).setLayer( -0.5f);
		}
	};

	public void update(Block block, Base base){

	}

	static public final int blocksize = 14;

	public void destroyEvent(Base base, int x, int y){
		new BreakEffect(name(), 1.5f).setPosition(world(base,x), world(base,y)).SendSelf();
		base.blocks[x][y].setMaterial(Material.ironblock);
	}

	public void draw(Block block, Base base, int x, int y){
		defaultDraw(name(), block, base, x, y);
	}

	public Layer defaultDraw(String region, Block block, Base base, int x, int y){
		return Entity.renderer.layer(region, world(base, x), world(base, y)).setLayer( -1f).setColor(new Color(block.healthfrac() + 0.3f, block.healthfrac() + 0.3f, block.healthfrac() + 0.3f, 1f));
	}

	public Layer defaultDraw(String region, Block block, Base base, int x, int y, boolean damage){
		Layer layer = Entity.renderer.layer(region, world(base, x), world(base, y)).setLayer( -1f);
		if(damage) layer.setColor(new Color(block.healthfrac() + 0.3f, block.healthfrac() + 0.3f, block.healthfrac() + 0.3f, 1f));
		return layer;
	}

	float world(Base base, int i){
		return base.y + i * blocksize - base.size / 2f * blocksize + blocksize / 2f;
	}

	public boolean solid(){
		return false;
	}

	public int health(){
		return 10;
	}
}
