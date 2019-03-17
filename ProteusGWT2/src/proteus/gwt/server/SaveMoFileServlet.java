package proteus.gwt.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import proteus.gwt.server.db.MOFileParser;

public class SaveMoFileServlet extends HttpServlet {

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse) deperacted para: name and
	 * description
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		String filePath = req.getParameter("filepath");

		String name = req.getParameter("name");
		String description = req.getParameter("description");
//		String mid = req.getParameter("mid");
		if (filePath == null) {
			out.println("Save mo file failed");
			out.println("id=-1");
		} else {
			File f = new File(filePath);
			if (f.exists()) {
				MOFileParser moFileParser = new MOFileParser();
				// inside this already writeJson
				long id = moFileParser.parser(filePath, name, description);

				if (id >= 0) {
					out.println("Save mo file successfully");
					out.println("id=" + id);
				} else {
					out.println("Save mo file failed");
					out.println("id=" + id);
				}
			} else {
				out.println("Save mo file failed");
				out.println("id=-1");
			}
		}

		out.close();
	}
}
