package proteus.gwt.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import proteus.gwt.server.businesslogic.DomainModelProxy2;
import proteus.gwt.server.db.MOFileParser;

public class UpdateMoFileServlet extends HttpServlet {

	/**
	 * This servelet is used by both Pweb and ProteusGWT to update(edit, delete)
	 * Modelica File(.mo) and associated JSON file
	 */

	/**
	 * This file is not used.
	 * 
	 * @deprecated
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
	 * , javax.servlet.http.HttpServletResponse) return >0 successfully remove
	 * the records return <0 remove operation is not successful.
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		String idString = req.getParameter("mid");
		if (idString != null && idString.length() != 0) {
			try {
				long mid = Long.valueOf(idString);
				DomainModelProxy2.delete(mid);
				out.println("delete operation is successful!");
				out.println("id=" + mid);
			} catch (Exception e) {
				out.println("the id is not an long value");
				out.println("id=-1");
			}
		} else {
			out.println("remove json file failed");
			out.println("id=" + "-1");
		}
		out.close();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\sam\\workspace\\ProteusGWT2\\1.mo";
		MOFileParser fileParser = new MOFileParser();
		long id = fileParser.parser(504, path, "dfds", "");
		// System.out.println("parser return:" + parser.parser(path, "dfd",
		// ""));
	}
}
