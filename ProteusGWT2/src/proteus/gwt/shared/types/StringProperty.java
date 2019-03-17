package proteus.gwt.shared.types;

import com.google.gwt.user.client.rpc.IsSerializable;

import emu.java.beans.PropertyChangeEvent;
import emu.java.beans.PropertyChangeListener;
import emu.java.beans.PropertyChangeSupport;

public class StringProperty extends Property implements PropertyChangeListener,
		IsSerializable {

	/**
	 * Parent object containing the property
	 */
	private String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		String oldValue = this.value;
		this.value = value;
		// Fires a property change event
		pcs.firePropertyChange("value", oldValue, value);
	}

	/**
	 * List of listeners interested in being informed when the value changes
	 */

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public StringProperty(String name) {
		value = name;
		pcs.addPropertyChangeListener(this);
	}
	
	public StringProperty(Object obj, String name) {
		value = name;
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
//		System.out.println("Name      = " + evt.getPropertyName());
//		System.out.println("Old Value = " + evt.getOldValue());
//		System.out.println("New Value = " + evt.getNewValue());
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringProperty mystring = new StringProperty("name");
		mystring.setValue("test");
	}
}
