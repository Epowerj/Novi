package net.pixelstatic.novi.modules;

import java.util.ArrayList;

import net.pixelstatic.novi.Novi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Logger extends Module{
	static final String filename = "novi.log";
	ArrayList<String> lines = new ArrayList<String>();

	public Logger(Novi n){
		super(n);
	}

	public void logged(Object object){
		lines.add(String.valueOf(object));
		if(object instanceof Exception){
			Exception e = (Exception)object;
			for(StackTraceElement element : e.getStackTrace()){
				lines.add(element.toString());
			}
		}
	}

	public void writeFile(){
		FileHandle file = Gdx.files.local(filename);
		file.writeString("", false);
		for(String line : lines){
			file.writeString(line + "\n", true);
		}
		Novi.log("Log file written to " + file.file().getAbsolutePath());
	}

	@Override
	public void Update(){

	}

}
