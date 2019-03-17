/**
 * 
 */
package proteus.gwt.client.app.ui.common.codemirror;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasInitializeHandlers;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.event.logical.shared.InitializeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Widget;



/**
 * @author leiting
 * April 17 2011
 */
public class CodeMirrorTextArea extends Widget implements HasHTML, HasInitializeHandlers {

	private CodeMirrorTextAreaImpl impl = GWT.create(CodeMirrorTextAreaImpl.class);
	
	Timer initCodeMirrorTimer;
	
	public CodeMirrorTextArea() {
		setElement(impl.getElement());
		setStyleName("gwt-CodeMirror");
		impl.setOwner(this);
		
	}
	
	protected native boolean initCodeMirror(Element element) /*-{
		var CodeMirror = $wnd.CodeMirror;
		
		if (CodeMirror == undefined || element == null) {
			return false;
		}
		var hlLine;
	    var editor = CodeMirror.fromTextArea(element, {
	      mode: "modelica-overlay", 
	      lineNumbers: true,
	      matchBrackets: true, 
	      onCursorActivity: function() {
	        editor.setLineClass(hlLine, null);
	        hlLine = editor.setLineClass(editor.getCursor().line, "activeline");
	      }
	    });
	    return true;
	}-*/;
	
	@Override
	public String getText() {
		return impl.getText();
	}

	@Override
	public void setText(String text) {
		impl.setText(text);
	}

	@Override
	public String getHTML() {
		return impl.getHTML();
	}

	@Override
	public void setHTML(String html) {
		impl.setHTML(html);
	}
  
	@Override
	protected void onAttach() {
	    super.onAttach();
	    impl.initElement();

	    setStyleName("gwt-CodeMirror");

	    // set size to fit parent
	    setSize("100%", "100%");
	    
	    // init CodeMirror
		initCodeMirrorTimer = new Timer() {
			@Override
			public void run() {
				if (initCodeMirror(getElement())) {
					initCodeMirrorTimer.cancel();
				} 
				else {
					initCodeMirrorTimer.schedule(300);
				}
			}
		};
		initCodeMirrorTimer.run();
	  }
	
	 @Override
	 protected void onDetach() {
	    super.onDetach();
	    impl.uninitElement();
	  }

	@Override
	public HandlerRegistration addInitializeHandler(InitializeHandler handler) {
		return addHandler(handler, InitializeEvent.getType());
	}

}
