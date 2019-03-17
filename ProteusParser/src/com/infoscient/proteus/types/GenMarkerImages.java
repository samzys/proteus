package com.infoscient.proteus.types;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class GenMarkerImages {

	public static void main(String[] args) throws Exception {
		Marker[] markers = Marker.MARKERS;
		File file = new File(System.getProperty("user.home")
				+ "/Desktop/markerImages");
		file.mkdir();
		for (Marker marker : markers) {
			String name = marker.name;
			name = name.replace(" ", "");
			name = name.substring(0, 1).toLowerCase() + name.substring(1)
					+ ".png";
			int size = 8;
			BufferedImage image = new BufferedImage(size + 1, size + 1,
					BufferedImage.TYPE_INT_ARGB);
			marker.paint(image.getGraphics(), 0, 0, size, size, Color.black,
					null);
			ImageIO.write(image, "png", new File(file + "/" + name));
		}
	}
}
