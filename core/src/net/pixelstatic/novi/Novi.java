package net.pixelstatic.novi;

import java.util.HashMap;

import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.modules.*;

import com.badlogic.gdx.ApplicationAdapter;

public class Novi extends ApplicationAdapter {
	public HashMap<Class<? extends Module>, Module> modules = new HashMap<Class<? extends Module>, Module>();
	
	@Override
	public void create() {
		Entity.novi = this;
		createModule(Renderer.class);
		createModule(Input.class);
		createModule(Network.class);
		createModule(ClientData.class);
		createModule(World.class);
		for(Module m : modules.values()) m.Init(); //initialize modules
	}

	@Override
	public void render() {
		//update all entities
		for(Entity e : Entity.entities.values()){
			e.Update();
			e.Draw();
		}
		//update all modules
		for (Module m : modules.values()) {
			m.Update();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		getModule(Renderer.class).onResize(width, height); //pass on to Renderer
	}
	
	public static void log(Object o){
		System.out.println(o);
	}

	public <T extends Module> T getModule(Class<T> c) {
		return c.cast(modules.get(c));
	}

	public void createModule(Class<? extends Module> module) {
		try {
			modules.put(module, module.getConstructor(this.getClass()).newInstance(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
