package com.infoscient.proteus;

import java.io.File;
import java.io.IOException;

import com.infoscient.proteus.db.exceptions.UnsupportedExtensionException;

/**
 * Interface for a class that knows how to handle (read/write) a file with a
 * particular extension (*.mo, *.moe etc)
 * 
 * @author jasleen
 * 
 */
public interface FileExtHandler {

	/**
	 * Return the extension that can be handled (without any preceding symbols,
	 * e.g. "doc", "png" etc.)
	 * 
	 * @return Extension that can be handled
	 */
	public String getExtension();

	/**
	 * Specifies whether a file with a particular extension can be read or not
	 * 
	 * @param ext
	 *            Extension of the file to be read
	 * @return True if file with this extension can be read, false otherwise
	 */
	public boolean canRead(String ext);

	/**
	 * Specifies whether a file with a particular extension can be written or
	 * not
	 * 
	 * @param ext
	 *            Extension of the file to be written
	 * @return True if file with this extension can be written, false otherwise
	 */
	public boolean canWrite(String ext);

	/**
	 * Short description of the type of file being handled
	 * 
	 * @return Short string description
	 */
	public String getDescription();

	/**
	 * Reads the file and returns success value
	 * 
	 * @param file
	 *            File to be read
	 * @param args
	 *            List of extra arguments that may help in reading the file
	 * @return True if file read successfully, false otherwise
	 * @throws ParseException
	 *             Error in file format or syntax
	 * @throws UnsupportedExtensionException
	 *             File extension not supported
	 * @throws IllegalArgumentException
	 *             One or more of supplied arguments are illegal
	 * @throws IOException
	 *             File cannot be read
	 */
	public boolean read(File file, Object... args) throws ParseException,
			UnsupportedExtensionException, IllegalArgumentException,
			IOException;

	/**
	 * Writes the file and returns success value
	 * 
	 * @param file
	 *            File to be written
	 * @param args
	 *            List of extra arguments that may help in writing the file
	 * @return True if file written successfully, false otherwise
	 * @throws ParseException
	 *             Error in file format or syntax
	 * @throws UnsupportedExtensionException
	 *             File extension not supported
	 * @throws IllegalArgumentException
	 *             One or more of supplied arguments are illegal
	 * @throws IOException
	 *             File cannot be written
	 */
	public boolean write(File file, Object... args) throws ParseException,
			UnsupportedExtensionException, IllegalArgumentException,
			IOException;
}
