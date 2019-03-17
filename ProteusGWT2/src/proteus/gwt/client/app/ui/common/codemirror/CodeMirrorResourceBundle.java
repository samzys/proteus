/**
 * 
 */
package proteus.gwt.client.app.ui.common.codemirror;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;
import com.google.gwt.resources.client.CssResource.NotStrict;

/**
 * @author leiting
 * Created Apr 17, 2011
 */
public interface CodeMirrorResourceBundle extends ClientBundle {
	
	@NotStrict
	@Source("codemirror.css")
	CssResource codeMirrorCss();
	
	@NotStrict
	@Source("modelica.css")
	CssResource modelicaCss();
	
	@Source("codemirror.js")
	TextResource codeMirrorJs();
	
	@Source("overlay.js")
	TextResource overlayJs();
	
	@Source("modelica.js")
	TextResource modelicaJs();
}
