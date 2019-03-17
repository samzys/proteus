package proteus.gwt.server.businesslogic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import proteus.gwt.server.util.ServerContext;
import proteus.gwt.shared.datatransferobjects.ExportProteusDTO;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ExportProteusProxy {
	public static String saveMOFile(ExportProteusDTO exportDTO)
			throws IOException {

		String fileName = UUID.randomUUID().toString() + ".mo";
		String exportPath = ServerContext.get("ProteusRootPath")
				+ ServerContext.get("ExportPath") + "/mo/" + fileName;

		File file = new File(exportPath);
		FileWriter fw = new FileWriter(file);
		fw.write(exportDTO.getCode());
		fw.close();
		return fileName;
	}

	public static String saveImage(ExportProteusDTO exportDTO, String format)
			throws IOException {

		String rootPath = ServerContext.get("ProteusRootPath");

		String exportPath = ServerContext.get("ExportPath") + "/" + format
				+ "/" + UUID.randomUUID().toString() + "." + format;

		if (exportDTO.getRgbArray() == null) {
			return "";
		}

		BufferedImage mindMapImage = null;
		try {
			mindMapImage = new BufferedImage(exportDTO.getCropWidth(),
					exportDTO.getCropHeight(), BufferedImage.TYPE_INT_RGB);
			mindMapImage.setRGB(0, 0, exportDTO.getCropWidth(),
					exportDTO.getCropHeight(), exportDTO.getRgbArray(), 0,
					exportDTO.getCropWidth());
			if (mindMapImage != null) {
				File file = new File(rootPath + exportPath);

				System.out.println("rootPath:" + rootPath);
				System.out.println("exportPath:" + exportPath);
				ImageIO.write(mindMapImage, format, file);
			}
		} catch (IOException ex) {
			throw ex;
		}

		return exportPath;
	}

	public static String savePDF(ExportProteusDTO exportDTO) {

		String rootPath = ServerContext.get("ProteusRootPath");

		String exportPath = ServerContext.get("ExportPath") + "/pdf/"
				+ UUID.randomUUID().toString() + ".pdf";
		System.out.println("rootPath:" + rootPath);
		System.out.println("exportPath:" + exportPath);
		if (exportDTO.getRgbArray() == null) {
			return "";
		}

		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(rootPath
					+ exportPath));
			document.open();
			addMetaData(document, exportDTO);
			addContent(document, exportDTO);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return exportPath;
	}

	private static void addMetaData(Document document,
			ExportProteusDTO exportDTO) {
		document.addTitle(exportDTO.getTitle());
		document.addCreator("Fabrikz MindMap");
	}

	private static void addContent(Document document, ExportProteusDTO exportDTO)
			throws DocumentException, IOException {
		Paragraph title = new Paragraph();
		title.add(new Paragraph(exportDTO.getTitle(), new Font(
				Font.FontFamily.TIMES_ROMAN, 13, Font.BOLD)));

		BufferedImage mindMapImage = new BufferedImage(
				exportDTO.getCropWidth(), exportDTO.getCropHeight(),
				BufferedImage.TYPE_INT_RGB);
		mindMapImage.setRGB(0, 0, exportDTO.getCropWidth(),
				exportDTO.getCropHeight(), exportDTO.getRgbArray(), 0,
				exportDTO.getCropWidth());

		com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(
				mindMapImage, null);
		// if (orientation == 0) {
		// // portrait
		// } else {
		// // landscape
		// image.setRotationDegrees(90);
		// }

		image.setAlignment(Element.ALIGN_MIDDLE);
		if (image.getWidth() > document.getPageSize().getWidth()) {
			float percent = (float) (100.0 * document.getPageSize().getWidth() / image
					.getWidth());
			image.scalePercent(percent);
		}

		Paragraph footer1 = new Paragraph("Created by Proteus", new Font(
				Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL,
				BaseColor.DARK_GRAY));
		footer1.setAlignment(Element.ALIGN_RIGHT);
		Paragraph footer2 = new Paragraph(
				"http://www.visualphysics.net/ProteusGWT/", new Font(
						Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL,
						BaseColor.BLUE));
		footer2.setAlignment(Element.ALIGN_RIGHT);

		document.add(title);
		document.add(image);
		document.add(footer1);
		document.add(footer2);
	}
}
