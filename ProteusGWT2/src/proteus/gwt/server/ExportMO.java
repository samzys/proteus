package proteus.gwt.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proteus.gwt.server.util.ServerContext;

public class ExportMO extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		try {
			doPost(req, res);
		} catch (IOException e) {
			throw e;
		} catch (ServletException e) {
			System.err.println(e.getMessage());
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		String fileName = req.getParameter("filename");
		String tabName = req.getParameter("tabname");
		if (fileName == null) {
			return;
		}
		String filePath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ExportPath") + "/mo/" + fileName;

		res.setContentType(".fab;charset=UTF-8");
		
		//System.out.println(filePath);
		//System.out.println(tabName);
		
		// Get IE Version
		boolean IEVersion6_0 = (req.getHeader("User-Agent").indexOf("MSIE 6.0") > 0);

		try {
			ServletOutputStream os = res.getOutputStream();
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);

			byte[] b = new byte[(int) file.length()];
			int i = 0;
			while ((i = fis.read(b)) != -1) {
				os.write(b, 0, i);
			}
			res.setHeader("Content-Disposition", "attachment;filename="
					+ tabName);
			if (IEVersion6_0) {
				res.setHeader("Content-Disposition", "filename=" + tabName);
			}
			fis.close();
			os.flush();
			os.close();
			res.setStatus(HttpServletResponse.SC_OK);
			res.flushBuffer();
		} catch (IOException e) {
			res = null;
			e.printStackTrace();
		}

	}
}
