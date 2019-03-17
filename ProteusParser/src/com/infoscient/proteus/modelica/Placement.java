/**
 * 
 */
package com.infoscient.proteus.modelica;

import java.io.Serializable;

/**
 * @author Lei Ting
 * 09 Oct 09
 */
public class Placement implements Serializable{
	public static class Transformation implements Serializable{
		public int [] origin;
		
		public int [] extent;
		
		public int rotation;
	}
}
