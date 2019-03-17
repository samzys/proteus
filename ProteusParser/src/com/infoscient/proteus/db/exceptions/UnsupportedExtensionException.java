package com.infoscient.proteus.db.exceptions;

/**
 * Exception type thrown when the extension file extension handler doesn't
 * support the input extension
 * 
 * @author jasleen
 * 
 */
public class UnsupportedExtensionException extends Exception {
	/**
	 * Constructor
	 * 
	 * @param ext
	 *            Unsupported extension string
	 */
	public UnsupportedExtensionException(String ext) {
		super("Do not understand extension: " + ext);
	}
}
