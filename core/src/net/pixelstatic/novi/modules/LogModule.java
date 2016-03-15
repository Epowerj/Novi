package net.pixelstatic.novi.modules;

import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

import net.pixelstatic.novi.Novi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.esotericsoftware.minlog.*;
import com.esotericsoftware.minlog.Log.Logger;

public class LogModule extends Module{
	static final String filename = "novi.log";
	CopyOnWriteArrayList<String> lines = new CopyOnWriteArrayList<String>();

	public LogModule(Novi n){
		super(n);
		Log.DEBUG();
		Log.setLogger(new Loggy());
	}

	class Loggy extends Logger{
		public void log(int level, String category, String message, Throwable ex){
			StringBuilder builder = new StringBuilder(256);
			builder.append('[');
			builder.append(category);
			builder.append("] ");
			builder.append(message);
			if(ex != null){
				StringWriter writer = new StringWriter(256);
				ex.printStackTrace(new PrintWriter(writer));
				builder.append('\n');
				builder.append(writer.toString().trim());
			}
			Novi.log(builder.toString());
		}
	}

	public void logged(Object object){
		lines.add(String.valueOf(object));
		if(object instanceof Exception){
			Exception e = (Exception)object;
			for(StackTraceElement element : e.getStackTrace()){
				lines.add(element.toString());
			}
			if(e.getCause() != null){
				logged("Caused by:");
				logged(e.getCause());
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
