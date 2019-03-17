/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author leiting
 * Created May 14, 2011
 */
public class ImageChartsVisualizationProviderTest {

	ImageChartsVisualizationProvider p = new ImageChartsVisualizationProvider();
	
	@Test
	public void testTruncateDouble() {
		assertEquals("0.1234", p.getTruncatedDouble(0.12345678, 4));
		assertEquals("-0.1234", p.getTruncatedDouble(-0.12345678, 4));
		assertEquals("10.12", p.getTruncatedDouble(10.12345678, 4));
		assertEquals("-10.12", p.getTruncatedDouble(-10.12345678, 4));
	}
}
