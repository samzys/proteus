/**
 * 
 */
package proteus.gwt.shared.modelica;

import java.io.Serializable;

/**
 * @author Lei Ting 09 Oct 09
 */
public class Placement implements Serializable {
	public static class Transformation implements Serializable {
		public Transformation() {
			origin = new int[] {0,0};
			extent = new int[] {-100, 100, 100, -100};
			oldExtent = new int[] {-100, -100, 100, 100};
			rotation = 0;
		}
		public int[] origin;// {x1, y1}

		public int[] extent; // {x1, y1, x2, y2}
		// this is the old extent info with y inverted

		public int[] oldExtent;//{x1, y1, x2, y2}
		// this is the origial extent parsed from code.

		public int rotation;
	}
}
