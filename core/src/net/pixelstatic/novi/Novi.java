package net.pixelstatic.novi;

import java.util.HashMap;

import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.modules.*;
import net.pixelstatic.novi.modules.Input;

import com.badlogic.gdx.*;

public class Novi extends ApplicationAdapter {
	public HashMap<String, Module> modules = new HashMap<String, Module>();
	
	@Override
	public void create() {
		createModule(Renderer.class);
		createModule(Input.class);
		createModule(Network.class);
		createModule(ClientData.class);
		createModule(World.class);
		Gdx.input.setInputProcessor(getModule(Input.class));
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
		return c.cast(modules.get(c.getSimpleName().toLowerCase()));
	}

	public Module getModule(String name) {
		return modules.get(name);
	}

	public void createModule(Class<? extends Module> module) {
		try {
			modules.put(module.getSimpleName().toLowerCase(), module.getConstructor(this.getClass()).newInstance(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
