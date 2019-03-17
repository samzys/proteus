package proteus.gwt.shared.datatransferobjects;


import java.io.Serializable;

import proteus.gwt.shared.types.IllegalDimensionsException;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Class for plot data of dimensionality 2. Constrains its values to be of type
 * <code>Values2D</code>
 * 
 * @author jasleen
 * 
 * @param <VALUES2D>
 *            Template variable specifying the sub-type of Values2D class used
 *            by this plot
 */
public class PlotDataDTO  implements IsSerializable, Serializable {

	protected String name;
	protected String title;
	protected Values2D values;
	protected boolean visible;
	protected String xLabel, yLabel;
	protected Range xRange, yRange;
	/**
	 * Constructor with name, title and axes labels initialized. The name is
	 * marked as final and can be set only in the constructor
	 * 
	 * @param type
	 *            Name of the plot, as a <code>String</code>
	 * @param title
	 *            Title of the plot, as a <code>String</code>
	 * @param xLabel
	 *            String to be displayed along the x-axis
	 * @param yLabel
	 *            String to be displayed along the y-axis
	 */
	public PlotDataDTO() {
		// TODO Auto-generated constructor stub
	}
	public PlotDataDTO(String name, String title, String xLabel, String yLabel) {
		this.name = name;
		this.title = title;
		this.visible = true;
		
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}
	/**
	 * Gets the name of the plot
	 * 
	 * @return Name of the plot, as a <code>String</code>
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the title of the plot
	 * 
	 * @return Title of the plot, as a <code>String</code>
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Returns the values contained in this plot data
	 * 
	 * @return an object of class <code>Values2D</code> containing plot values
	 */
	public Values2D getValues() {
		return values;
	}

	/**
	 * @return the xLabel
	 */
	public String getxLabel() {
		return xLabel;
	}

	/**
	 * Gets the string displayed along the x-axis
	 * 
	 * @return string displayed along the x-axis
	 */
	public String getXLabel() {
		return xLabel;
	}

	/**
	 * @return the xRange
	 */
	public Range getxRange() {
		return xRange;
	}

	/**
	 * Gets range of values in the x-dimension
	 * 
	 * @return a <code>Range</code> object for the x-dimension
	 */
	public Range getXRange() {
		return xRange;
	}

	/**
	 * @return the yLabel
	 */
	public String getyLabel() {
		return yLabel;
	}
	/**
	 * Gets the string displayed along the y-axis
	 * 
	 * @return string displayed along the y-axis
	 */
	public String getYLabel() {
		return yLabel;
	}

	/**
	 * @return the yRange
	 */
	public Range getyRange() {
		return yRange;
	}

	/**
	 * Gets range of values in the y-dimension
	 * 
	 * @return a <code>Range</code> object for the y-dimension
	 */
	public Range getYRange() {
		return yRange;
	}

	/**
	 * If this plot is visible or not
	 * 
	 * @return true if plot is visible, false otherwise
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the title of the plot
	 * 
	 * @return Title of the plot, as a <code>String</code>
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Sets the values contained in this plot data
	 * 
	 * @param values
	 *            an object of class <code>Values2D</code> containing plot
	 *            values
	 * @throws IllegalDimensionsException
	 *             thrown when the supplied values object does not meet the
	 *             implicit constraints
	 */
	public void setValues(Values2D values) throws IllegalDimensionsException {
		this.values = values;
		if (!values.checkDimensions()) {
			throw new IllegalDimensionsException(values);
		}
	}

	/**
	 * Sets if this plot is visible or not
	 * 
	 * @param visible
	 *            true if plot is visible, false otherwise
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @param xLabel the xLabel to set
	 */
	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	/**
	 * Sets the string to be displayed along the x-axis
	 * 
	 * @return label string to be displayed along the x-axis
	 */
	public void setXLabel(String label) {
		xLabel = label;
	}

	/**
	 * @param xRange the xRange to set
	 */
	public void setxRange(Range xRange) {
		this.xRange = xRange;
	}
	
	/**
	 * Sets range of values in the y-dimension
	 * 
	 * @param range
	 *            a <code>Range</code> object for the y-dimension
	 */
	public void setXRange(Range range) {
		xRange = range;
	}

	/**
	 * @param yLabel the yLabel to set
	 */
	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}

	/**
	 * Sets the string to be displayed along the y-axis
	 * 
	 * @return label string to be displayed along the y-axis
	 */
	public void setYLabel(String label) {
		yLabel = label;
	}

	/**
	 * @param yRange the yRange to set
	 */
	public void setyRange(Range yRange) {
		this.yRange = yRange;
	}

	/**
	 * Sets range of values in the y-dimension
	 * 
	 * @param range
	 *            a <code>Range</code> object for the y-dimension
	 */
	public void setYRange(Range range) {
		yRange = range;
	}

}
