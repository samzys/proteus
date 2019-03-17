package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 1-dimensional values, backed by one array for storing the data
 * 
 * @author jasleen
 * 
 */
public class Values1D extends Values  implements IsSerializable, Serializable {

	/**
	 * Array used to store x-values
	 */
	public double[] xValues;

	/**
	 * Default constructor, leaves the values uninitialized
	 */
	public Values1D() {
	}

	/**
	 * Constructor initializing values with supplied array. Note that it
	 * performs a shallow copy of the array. Therefore the input array should
	 * not be externally modified
	 * 
	 * @param values
	 *            Array used to store x-values
	 */
	public Values1D(double[] values) {
		this.xValues = values;
	}

	public boolean checkDimensions() {
		return xValues != null;
	}

	public String dimString() {
		return "values != null";
	}

//	public Values1D clone() {
//		Values1D clone = null;
//		try {
//			clone = (Values1D) super.clone();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return clone;
//	}
}
