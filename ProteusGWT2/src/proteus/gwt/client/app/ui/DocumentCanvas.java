package proteus.gwt.client.app.ui;

import proteus.gwt.client.proxy.AppContext;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Widget;

public class DocumentCanvas extends Composite {

	private static DocumentCanvasUiBinder uiBinder = GWT
			.create(DocumentCanvasUiBinder.class);

	interface DocumentCanvasUiBinder extends UiBinder<Widget, DocumentCanvas> {
	}

	@UiField
	Frame fram;

	private String docPath = AppContext
			.pathToServerUrl("/resources/images/classname/MSL3.1Image/");

	public DocumentCanvas() {
		initWidget(uiBinder.createAndBindUi(this));

	}

	public DocumentCanvas(String wholename) {
		initWidget(uiBinder.createAndBindUi(this));
		if (check(wholename)) {

			String url = wholename.substring(0, wholename.lastIndexOf("."));
			url = url.replace(".", "_");
			url = docPath + url + ".html#" + wholename;
			// content.setHTML(url);
			fram.setUrl(url);
		}

	}

	private boolean check(String wholename) {
		if(wholename==null){
			return false;
		}
		if (wholename.startsWith("Modelica")
				|| wholename.startsWith("ModelicaServices")
				|| wholename.startsWith("ModelicaReference")) {
			return true;
		} else {
			return false;
		}

	}
}
