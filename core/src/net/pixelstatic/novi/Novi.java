package net.pixelstatic.novi;

import java.util.HashMap;

import net.pixelstatic.novi.modules.Input;
import net.pixelstatic.novi.modules.Module;
import net.pixelstatic.novi.modules.Renderer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Novi extends ApplicationAdapter{
    HashMap<String, Module> modules = new HashMap<String, Module>();
    SpriteBatch batch;

    public void CreateModules(){
	CreateModule(Renderer.class);
	CreateModule(Input.class);
    }

    @Override
    public void create(){
	batch = new SpriteBatch();
	CreateModules();
	Gdx.input.setInputProcessor(GetModule(Input.class));
    }

    @Override
    public void render(){
	Gdx.gl.glClearColor(0, 0, 0, 1);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	batch.begin();
	for(Module m : modules.values()){
	    m.Update();
	}
	batch.end();
    }

    public <T extends Module>T GetModule(Class<T> c){
	return c.cast(modules.get(c.getClass().getSimpleName().toLowerCase()));
    }

    public Module GetModule(String name){
	return modules.get(name);
    }

    public void CreateModule(Class<? extends Module> c){
	try{
	    modules.put(c.getSimpleName().toLowerCase(), c.getConstructor(this.getClass()).newInstance(this));
	}catch(Exception e){
	    e.printStackTrace();
	}
    }
}
