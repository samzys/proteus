/**
 * 
 */
package proteus.gwt.client.app.ui.common.codemirror;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.dom.client.ScriptElement;

/**
 * @author leiting
 * Created Apr 17, 2011
 */
public class CodeMirrorResourceInjector {
	
	private static final Logger logger = Logger.getLogger("CodeMirrorResourceInjector");
	
	private static HeadElement head;
	 
	public static void injectAll() {
		CodeMirrorResourceBundle bundle = GWT.create(CodeMirrorResourceBundle.class);
		logger.log(Level.FINE, "All CodeMirror resource created.");
		logger.log(Level.FINE, "Injecting CodeMirror Resources...");
		
		bundle.codeMirrorCss().ensureInjected();
		bundle.modelicaCss().ensureInjected();
		
		inject(bundle.codeMirrorJs().getText());
		inject(bundle.overlayJs().getText());
		inject(bundle.modelicaJs().getText());
		
		logger.log(Level.FINE, "All CodeMirror resource injected.");
	}
    
	public static void inject(String javascript) {
        HeadElement head = getHead();
        ScriptElement element = createScriptElement();
        element.setText(javascript);
        head.appendChild(element);
    }
 
    private static ScriptElement createScriptElement() {
        ScriptElement script = Document.get().createScriptElement();
        script.setAttribute("language", "javascript");
        return script;
    }
 
    private static HeadElement getHead() {
        if (head == null) {
            Element element = Document.get().getElementsByTagName("head").getItem(0);
            assert element != null : "HTML Head element required";
            HeadElement head = HeadElement.as(element);
            CodeMirrorResourceInjector.head = head;
        }
        return CodeMirrorResourceInjector.head;
    }

}
