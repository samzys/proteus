/**
 * 
 */
package proteus.gwt.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import proteus.gwt.server.util.ServerContext;

/**
 * @author Gao Lei
 * 
 */
public class FileUploadServlet extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(5000000);
		res.setContentType("text/plain");
		PrintWriter out = res.getWriter();
		try {
			List items = upload.parseRequest(req);
			Iterator iterator = items.iterator();
			Map<String, String> fields = new HashMap<String, String>();
			boolean uploaded = false;
			String server = "";
			String serverSideRelPath = "";
			String serverSidePath = "";
			String serverSideName = "";
			String filePath="";

			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {
					out.println("Got a form field: " + item.getFieldName());
					fields.put(item.getFieldName(), item.getString());
				} else {
					if (checkFileType(item)) {
						InputStream in = item.getInputStream();
						String fieldName = item.getFieldName();
						String fileName = item.getName();
						String contentType = item.getContentType();
						boolean isInMemory = item.isInMemory();
						long sizeInBytes = item.getSize();
						server = getServer(req);
						serverSideRelPath = getServerSideRelPath();
						serverSidePath = getServerSidePath();
						serverSideName = getServerSideName(item);
						filePath = serverSidePath + "/" + serverSideName;
						
						out.println("--------------");
						out.println("fileName = " + fileName);
						out.println("field name = " + fieldName);
						out.println("contentType = " + contentType);
						out.println("size = " + sizeInBytes + " (bytes)");
						out.println("is in memory = " + isInMemory);
						out.println("url = " + server + "/" + serverSideRelPath
								+ "/" + serverSideName);
						out.println("server side name = " + serverSideName);

						FileOutputStream os = new FileOutputStream(
								serverSidePath + "/" + serverSideName);
						try {
							IOUtils.copy(in, os);
							out.println("file uploaded successfully");
							System.out.println("file uploaded successfully");
							uploaded = true;
						} finally {
							IOUtils.closeQuietly(in);
							//fileUploadFailedHandler("", out, fields);
						}
					} else {
						out.println("Unacceptable file type");
						fileUploadFailedHandler("", out, fields);
					}
				}
			}

			if (uploaded) {
				fileUploadSuccessHandler(serverSidePath + "/" + serverSideName,
						out, fields);
			}
		} catch (SizeLimitExceededException e) {
			out.println("You exceeded the maximum size ("
					+ e.getPermittedSize() + ") of the file ("
					+ e.getActualSize() + ")");
		} catch (FileUploadException e) {
			out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	protected String getServer(HttpServletRequest req) {
		String server = "";
		if (req.getRequestURL() != null && req.getRequestURL().length() != 0) {
			int pos = req.getRequestURL().lastIndexOf("/");
			server += req.getRequestURL().substring(0, pos);

			if (server.endsWith("/faces")) {
				int pos2 = server.lastIndexOf("/faces");
				server = server.substring(0, pos2);
			}
		}

		return server;
	}

	protected String getServerSideRelPath() {
		return "/resources/upload";
	}

	protected String getServerSidePath() {
		String userFilesPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("UploadPath");

		return userFilesPath;
	}

	protected String getServerSideName(FileItem item) {
		String ext = getExtensionName(item);
		String filename = item.getName();
		String uuid = (UUID.randomUUID().toString());

		if (ext != null && ext.length() > 0) {
			return uuid + "." + ext;
		} else {
			return uuid;
		}
	}

	protected String getExtensionName(FileItem item) {
		String filename = item.getName();
		String ext = "";

		int pos = filename.lastIndexOf('.');
		if (pos != -1 && filename.length() > pos + 1) {
			ext = filename.substring(pos + 1, filename.length());
		}

		return ext;
	}

	protected boolean checkFileType(FileItem item) {
		return true;
	}

	protected void fileUploadSuccessHandler(String filePath,
			PrintWriter writer, Map<String, String> fields) {

	}

	protected void fileUploadFailedHandler(String fileName, PrintWriter writer,
			Map<String, String> fields) {

	}
}
