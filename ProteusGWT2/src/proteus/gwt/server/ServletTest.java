package proteus.gwt.server;

import java.io.IOException;

import proteus.gwt.server.db.MOFileParser;

public class ServletTest {

	public static void saveMoFileServletTest() throws IOException {
		String path = "C:\\tmp\\1.mo";
		String filePath = "C:\\tmp\\1.mo";
		String name = "www";
		String description = "zzz";
		MOFileParser moFileParser = new MOFileParser();
		// inside this already writeJson
		long id = moFileParser.parser(filePath, name, description);
	}

	public static void updateMoFileServletTest() throws IOException {
		String filePath = "D:\\1.mo"; //this is file under proteusweb which has already been updated. 
		String idString = "241";
//		String file = req.getParameter("file");
		String name = "OOOOO";
		String description = "o-ring......";
		long mid = Long.valueOf(idString);
		MOFileParser fileParser = new MOFileParser();
		long id = fileParser.parser(mid, filePath, name, description);
	}

	public static void main(String[] args) {
		try {
//			saveMoFileServletTest();
			
			updateMoFileServletTest();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
