package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Abstract superclass for values used in the plot data. Specifies interfaces to
 * be implemented by subclasses to check validity of supplied arrays
 * 
 * @author jasleen
 * 
 */
public abstract class Values implements IsSerializable, Serializable{
	/**
	 * Checks validity of stored data
	 * 
	 * @return true if data is valid and satisfies all constraints, false
	 *         otherwise
	 */
	public abstract boolean checkDimensions();

	/**
	 * Returns a human readable string specifying constraints put by this class.
	 * Used to notify the user (through the UI) in case of an error in the data
	 * provided
	 * 
	 * @return Human readable string specifying constraints put by this class
	 */
	public abstract String dimString();
}
