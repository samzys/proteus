package proteus.gwt.client.app.ui.canvas;

import proteus.gwt.client.app.ui.CodeCanvas;
import proteus.gwt.client.app.ui.ModelViewInstance;
import proteus.gwt.shared.modelica.ClassDef;

public class CodeCanvasHandler {
	private CodeCanvas codeCanvas;
	private ClassDef classDef;
	private ModelViewInstance currInstance;
	private String codeHTML;

	public CodeCanvasHandler(CodeCanvas codeCanvas, ModelViewInstance instance) {
		this.codeCanvas = codeCanvas;
		this.currInstance = instance;
		this.classDef = currInstance.getClassDef();
		//init the display panel
		if(classDef !=null)
		codeHTML = classDef.toCode();
	}
	
	public void textChanged() {
		if(classDef !=null)
		codeHTML = classDef.toCode();
		refresh();
	}

	public void refresh() {
		codeCanvas.setContent(codeHTML);
	}
}
