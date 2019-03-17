package proteus.gwt.client.app.util;

import com.google.gwt.user.client.ui.Image;

import proteus.gwt.client.proxy.AppContext;

public class HTMLGetter {
	public static String getImageText(String imgSrc, String text) {
		String imgURL = AppContext.pathToServerUrl(imgSrc);
		String html = "<table><tbody><tr><td><img src=\" " + imgURL
				+ " \" /></td><td>" + text + "</td></tr>	</tbody></table>";
		return html;
	}

	public static String getImageTextWithImageSize(String imgSrc, String text,
			int width, int height) {
		String imgURL = AppContext.pathToServerUrl(imgSrc);
		String html = "<table><tbody><tr><td><img src=\" " + imgURL
				+ "\"  style=\"height: " + height + "px; width: " + width
				+ "px; \" /></td><td>" + text + "</td></tr>	</tbody></table>";
		return html;
	}

	public static String getImageTextWithBG(String imgSrc, String text,
			String color) {
		String imgURL = AppContext.pathToServerUrl(imgSrc);
		String html = "<table style=\"background-color:" + color
				+ " \"><tbody><tr><td><img src=\" " + imgURL
				+ " \" /></td><td>" + text + "</td></tr>	</tbody></table>";
		return html;

	}

	public static String getTextWithBG(String text, String color) {
		String html = "<span style=\"background-color:" + color + "\">" + text
				+ "</span>";
		return html;
	}

	public static String getTextWithColor(String text, String color) {
		String html = "<span style=\"color:" + color + "\">" + text + "</span>";
		return html;
	}

	public static String getTextwithSize(String text, String size, boolean bold) {
		String html = "";
		if (bold) {
			html += "<strong>";
		}
		html += "<span style=\"font-size:" + size + "px;\">" + text + "</span>";

		return html;

	}
}
