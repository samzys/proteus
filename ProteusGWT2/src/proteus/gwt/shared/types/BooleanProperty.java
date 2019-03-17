package proteus.gwt.shared.types;

import com.google.gwt.user.client.rpc.IsSerializable;

import emu.java.beans.PropertyChangeEvent;
import emu.java.beans.PropertyChangeListener;
import emu.java.beans.PropertyChangeSupport;

public class BooleanProperty extends Property implements
		PropertyChangeListener, IsSerializable {

	/**
	 * Parent object containing the property
	 */
	private boolean value;


	/**
	 * List of listeners interested in being informed when the value changes
	 */

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public BooleanProperty(boolean value) {
		this.value = value;
		pcs.addPropertyChangeListener(this);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		pcs.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		pcs.removePropertyChangeListener(l);
	}


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
	}

	/**
	 * @return the value
	 */
	public boolean isValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(boolean value) {
		boolean oldValue = this.value;
		this.value = value;
		pcs.firePropertyChange("value", oldValue, value);
	}
	
}
