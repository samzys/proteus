/**
 * 
 */
package proteus.gwt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;

import proteus.gwt.server.db.MOFileParser;
import proteus.gwt.server.util.ServerContext;

/**
 * @author Gao Lei
 * 
 */
public class MoFileUploadServlet extends FileUploadServlet {

	@Override
	protected boolean checkFileType(FileItem item) {
		String ext = getExtensionName(item);

		if ("mo".equalsIgnoreCase(ext)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void fileUploadSuccessHandler(String filePath,
			PrintWriter writer, Map<String, String> fields) {

		String name = fields.get("name");
		String description = fields.get("description");

		try {
			MOFileParser moFileParser = new MOFileParser();
			long id = moFileParser.parser(filePath, name, description);
			writer.println(filePath+"......");
			writer.println("id=" + id);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			writer.println(e.getMessage());
			e.printStackTrace();
			writer.println("Mo file upload failed");
			return;
		}
		writer.println("Mo file successfully uploaded");
	}

	@Override
	protected void fileUploadFailedHandler(String fileName, PrintWriter writer,
			Map<String, String> fields) {
		writer.println("Mo file upload failed");
	}
}
