package net.pixelstatic.novi.modules;

import net.pixelstatic.novi.Novi;

public abstract class Module {
	Novi novi;

	public abstract void Update();

	public Module(Novi n) {
		this.novi = n;
	}

	public <T extends Module> T GetModule(Class<T> c) {
		return novi.GetModule(c);
	}

	public Module GetModule(String name) {
		return novi.GetModule(name);
	}
}
