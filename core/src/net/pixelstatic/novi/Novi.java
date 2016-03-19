package net.pixelstatic.novi;

import java.util.HashMap;

import net.pixelstatic.novi.entities.Entity;
import net.pixelstatic.novi.modules.*;
import net.pixelstatic.novi.systems.EntityLoadedSystem;

import com.badlogic.gdx.ApplicationAdapter;

public class Novi extends ApplicationAdapter{
	static final boolean logtrace = false;
	private static LogModule logger;
	public HashMap<Class<? extends Module>, Module> modules = new HashMap<Class<? extends Module>, Module>();

	@Override
	public void create(){
		Entity.novi = this;
		
		createModule(Renderer.class);
		createModule(Input.class);
		createModule(Network.class);
		createModule(ClientData.class);
		createModule(World.class);
		createModule(LogModule.class);
		logger = getModule(LogModule.class);
		
		Entity.setBaseSystem(new EntityLoadedSystem(getModule(ClientData.class).player));
		
		for(Module m : modules.values())
			m.Init(); //initialize modules
	}

	@Override
	public void render(){
		//update all entities
		Entity.updateAll();
		
		//update all modules
		for(Module m : modules.values()){
			m.Update();
		}
	}

	@Override
	public void resize(int width, int height){
		getModule(Renderer.class).onResize(width, height); //pass on to Renderer
	}

	public static void log(Object o){
		if(logger != null) logger.logged(o);
		if(o instanceof Exception){
			((Exception)o).printStackTrace();
			return;
		}
		if( !logtrace){
			System.out.println(o);
		}else{
			logtrace(o);
		}

	}

	public static void logtrace(Object o){
		StackTraceElement element = Thread.currentThread().getStackTrace()[2];
		System.out.println("[" + element.getMethodName() + "() @ " + element.getFileName().replace(".java", "") + "]: " + o);
	}

	public <T extends Module>T getModule(Class<T> c){
		return c.cast(modules.get(c));
	}

	public void createModule(Class<? extends Module> module){
		try{
			modules.put(module, module.getConstructor(this.getClass()).newInstance(this));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
