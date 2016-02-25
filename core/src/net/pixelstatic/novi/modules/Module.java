package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;

public abstract class Module {
	Novi novi;

	public abstract void Update();
	
	public void Init(){}

	public Module(Novi n) {
		this.novi = n;
	}

	public <T extends Module> T getModule(Class<T> c) {
		return novi.getModule(c);
	}
}
