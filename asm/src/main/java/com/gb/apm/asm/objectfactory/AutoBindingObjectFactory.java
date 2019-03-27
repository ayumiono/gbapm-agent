package com.gb.apm.asm.objectfactory;


public interface AutoBindingObjectFactory {
	public Object createInstance(ObjectFactory objectFactory, ArgumentProvider... providers);
}
