/**
 * 
 */
package emu.java.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author Lei Ting
 * Created Apr 9, 2011
 */
public class HashtableTest {

	String [] keys = new String [] {
			"N", 
			"T", 
			"U", 
	};
	String [] values = new String [] {
			"Nanyang", 
			"Technological", 
			"University"
	};
	
	@Test
	public void TestHashtable() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		
		for (int i = 0; i < keys.length; ++i) {
			hashtable.put(keys[i], values[i]);
			assertEquals(values[i], hashtable.get(keys[i]));
		}
	}
}
