/**
 * 
 */
package proteus.gwt.shared.graphics;

/**
 * @author Lei Ting
 *
 */
public class BasicStroke  {

	public final static int CAP_BUTT = 0;
	public final static int CAP_ROUND = 1;
	public final static int CAP_SQUARE = 2;
	public final static int JOIN_BEVEL = 0;
	public final static int JOIN_MITER = 1;
	public final static int JOIN_ROUND = 2;

	public final float width;
	private final int endCap;
	private final int lineJoin;
	private final float miterLimit;
	private final float[] dashArray;
	private final float dashPhase;

	public BasicStroke(float width) {
		this(width, CAP_BUTT, JOIN_BEVEL, 0, null, 0);
	}

	public BasicStroke(float width, int cap, int join, float miterlimit,
			float[] dash, float dash_phase) {
		this.width = width;
		this.endCap = cap;
		this.lineJoin = join;
		this.miterLimit = miterlimit;
		this.dashArray = dash;
		this.dashPhase = dash_phase;
	}

	/**
	 * @return the endCap
	 */
	public int getEndCap() {
		return endCap;
	}

	/**
	 * @return the lineJoin
	 */
	public int getLineJoin() {
		return lineJoin;
	}

	/**
	 * @return the miterLimit
	 */
	public float getMiterLimit() {
		return miterLimit;
	}

	/**
	 * @return the dashArray
	 */
	public float[] getDashArray() {
		return dashArray;
	}

	/**
	 * @return the dashPhase
	 */
	public float getDashPhase() {
		return dashPhase;
	}
	
}
