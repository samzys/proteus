package proteus.gwt.server.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CopyMissingImages {

	List<String> missingIcon = new ArrayList<String>();
	String filePath = "D:/workspace/ProteusGWT/war/resources/images/classname/MSL3.1Image/";
	String[] endText = { "S.png", "D.png", "I.png" };

	// @@@@modify this section @@@@
	String startName = "Modelica.Magnetic.FluxTubes.UsersGuide.";
	String sourceName = "FluxTubeConcept";

	String endName = "Modelica.Magnetic.FluxTubes.UsersGuide.";
	String[] files = { "ReluctanceForceCalculation", "Literature", "Contact"};
	// @@@@@@@@@

	public CopyMissingImages() {

		for (int j = 0; j < endText.length; j++) {
			String srcfile = filePath + startName + sourceName + endText[j];
			File f = new File(srcfile);
			if (!f.exists()) {
				System.err.println(srcfile);
				continue;
			}
			for (int i = 0; i < files.length; i++) {
				String destfile = filePath + endName + files[i] + endText[j];
				File ff = new File(destfile);
				// if(ff.exists()) ff.delete();
				if (!ff.exists()) {
					copy(srcfile, destfile);
				}
			}
		}

	}

	private void copy(String source, String destination) {
		// TODO Auto-generated method stub
		try {

			File sourceFile = new File(source);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(sourceFile), 4096);
			File targetFile = new File(destination);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(targetFile), 4096);
			int theChar;
			while ((theChar = bis.read()) != -1) {
				bos.write(theChar);
			}
			bos.close();
			bis.close();
			System.out.println("copy done!");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new CopyMissingImages();
	}

}
