package com.infoscient.proteus.types;

public interface MutablePropertyComponentListener {

	public void propertyAdded(MutablePropertyComponent comp, Property prop);

	public void propertyRemoved(MutablePropertyComponent comp, Property prop);
}
