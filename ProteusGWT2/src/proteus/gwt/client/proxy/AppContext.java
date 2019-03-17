package proteus.gwt.client.proxy;


import com.google.gwt.core.client.GWT;

public class AppContext {
	public static String getUrlContext() {
		String urlContext = GWT.getHostPageBaseURL();

		if (urlContext.endsWith("/")) {
			urlContext = urlContext.substring(0, urlContext.length() - 1);
		}

		return urlContext;
	}

	public static String urlToServerUrl(String url) {
		String urlContext = "";

		urlContext = AppContext.getUrlContext();

		if (url.startsWith("/")) {
			return urlContext + url;
		} else {
			return urlContext + "/" + url;
		}
	}

	public static String pathToServerUrl(String path) {
		String urlContext = "";

		urlContext = AppContext.getUrlContext();

		// for debug
		// if (!GWT.isScript()) {
		// urlContext = "http://www.fabrikz.com/Fabrikz2";
		// }

		// all resource files will be stored in the 'resource' folder
		String relativePath = path.substring(path.lastIndexOf("resources/"));
		return urlContext + "/" + relativePath;
	}
}
