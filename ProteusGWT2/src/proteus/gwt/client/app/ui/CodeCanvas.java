package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.ui.common.codemirror.CodeMirrorTextArea;
import proteus.gwt.shared.modelica.ClassDef;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author gao lei
 * 
 */
public class CodeCanvas extends Composite {

	interface DocumentCanvasUiBinder extends UiBinder<Widget, CodeCanvas> {
	}

	private static DocumentCanvasUiBinder uiBinder = GWT
			.create(DocumentCanvasUiBinder.class);

	private ClassDef classDef;

	public @UiField
	CodeMirrorTextArea content;

	public CodeCanvas() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * @return the classDef
	 */
	public ClassDef getClassDef() {
		return classDef;
	}

	/**
	 * @param classDef
	 *            the classDef to set
	 */
	public void setClassDef(ClassDef classDef) {
		this.classDef = classDef;
	}

	/**
	 * @return the content
	 */
	public CodeMirrorTextArea getContent() {
		return content;
	}
	
	public String getContentText() {
		return content.getText();
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(CodeMirrorTextArea content) {
		this.content = content;
	}

	public void setContent(String html) {
		content.setHTML(html);
	}
}