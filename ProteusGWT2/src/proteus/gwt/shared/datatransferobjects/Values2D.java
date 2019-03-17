package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 2-dimensional values, backed by two arrays for storing the data
 * 
 * @author jasleen
 * 
 */
public class Values2D extends Values1D  implements IsSerializable, Serializable {
	/**
	 * Array used to store y-values
	 */
	public double[] yValues;

	/**
	 * Default constructor, leaves the values uninitialized
	 */
	public Values2D() {
	}

	/**
	 * Constructor initializing values with supplied arrays. Note that it
	 * performs a shallow copy of the arrays. Therefore the input arrays should
	 * not be externally modified
	 * 
	 * @param values
	 *            Array used to store x-values
	 * @param values2
	 *            Array used to store y-values
	 */
	public Values2D(double[] values, double[] values2) {
		super(values);
		yValues = values2;
	}

	public boolean checkDimensions() {
		return (xValues != null) && (yValues != null)
				&& (xValues.length == yValues.length);
	}

	public String dimString() {
		return "(xValues != null) && (yValues != null)\n"
				+ "&& (xValues.length == yValues.length)";
	}

//	public Values2D clone() {
//		Values2D clone = (Values2D) super.clone();
//		return clone;
//	}
}
