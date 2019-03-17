package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;

import proteus.gwt.shared.util.Utils;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Class representing a range of values from a minimum to a maximum, and the
 * number of steps in between. Used by the plotter to determine the size of the
 * axes and the tick locations
 * 
 * @author jasleen
 * 
 */
public class Range implements IsSerializable, Serializable {
	/**
	 * Minimum value in this range of values
	 */
	public double min;

	/**
	 * Maximum value in this range of values
	 */
	public double max;

	/**
	 * Number of steps over this range of values
	 */
	public int steps;

	public Range() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor initializing the minimum and maximum values, with 10 as the
	 * default number of steps
	 * 
	 * @param min
	 *            Minimum value in this range of values
	 * @param max
	 *            Maximum value in this range of values
	 */
	public Range(double min, double max) {
		this(min, max, 10);
	}

	/**
	 * Constructor initializing the minimum, maximum and step values
	 * 
	 * @param min
	 *            Minimum value in this range of values
	 * @param max
	 *            Maximum value in this range of values
	 * @param step
	 *            Number of steps over this range of values
	 */
	public Range(double min, double max, int steps) {
		this.min = Utils.toPrecision(min, 100);
		this.max = Utils.toPrecision(max, 100);
		this.steps = steps;
	}

	/**
	 * Copies the range from the object passed in as argument
	 * 
	 * @param copy
	 *            <code>Range</code> object from which the values are to be
	 *            copied
	 */
	public Range(Range copy) {
		this.min = copy.min;
		this.max = copy.max;
		this.steps = copy.steps;
	}

	/**
//	 * Creates a clone of this object
//	 */
//	public Range clone() {
//		try {
//			return (Range) super.clone();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null; // Should not happen
//	}
}