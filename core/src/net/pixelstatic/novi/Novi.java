package net.pixelstatic.novi;

import java.util.HashMap;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import net.pixelstatic.novi.entities.Player;
import net.pixelstatic.novi.modules.Input;
import net.pixelstatic.novi.modules.Module;
import net.pixelstatic.novi.modules.Renderer;

public class Novi extends ApplicationAdapter {
	public HashMap<String, Module> modules = new HashMap<String, Module>();
	public SpriteBatch batch;
	public Player player;

	@Override
	public void create() {
		player = new Player();
		batch = new SpriteBatch();
		createModule(Renderer.class);
		createModule(Input.class);
		Gdx.input.setInputProcessor(getModule(Input.class));
		for(Module m : modules.values()) m.Init(); //initialize modules
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		
		//update all modules
		for (Module m : modules.values()) {
			m.Update();
		}

		batch.end();
	}
	
	@Override
	public void resize(int width, int height) {
		if(batch != null)
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
