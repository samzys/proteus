package com.infoscient.proteus.types;

public interface MutablePropertyComponent extends PropertyComponent {

	public void addProperty(Property prop);

	/**
	 * Removes a mutable property. Implementing classes should throw an
	 * IllegalArgumentException if isMutable() on a property returns false
	 * 
	 * @param prop
	 * @throws IllegalArgumentException
	 */
	public void removeProperty(Property prop) throws IllegalArgumentException;

	public boolean isMutable(Property prop);

	public void addMutablePropertyComponentListener(
			MutablePropertyComponentListener l);

	public void removeMutablePropertyComponentListener(
			MutablePropertyComponentListener l);
}
