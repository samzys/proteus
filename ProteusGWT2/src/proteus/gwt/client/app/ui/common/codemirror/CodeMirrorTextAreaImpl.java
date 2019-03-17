/**
 * 
 */
package proteus.gwt.client.app.ui.common.codemirror;

import com.google.gwt.event.logical.shared.HasInitializeHandlers;
import com.google.gwt.event.logical.shared.InitializeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;



/**
 * @author leiting
 * April 17 2011
 */
public class CodeMirrorTextAreaImpl {

	  protected Element elem;
	  protected HasInitializeHandlers owner;

	  public CodeMirrorTextAreaImpl() {
	    elem = createElement();
	  }

	  public Element getElement() {
	    return elem;
	  }

	  public String getHTML() {
	    return DOM.getElementProperty(elem, "value");
	  }

	  public String getText() {
	    return DOM.getElementProperty(elem, "value");
	  }

	  public void initElement() {
	    onElementInitialized();
	  }

	  public boolean isEnabled() {
	    return !elem.getPropertyBoolean("disabled");
	  }

	  public void setEnabled(boolean enabled) {
	    elem.setPropertyBoolean("disabled", !enabled);
	  }

	  public native void setFocus(boolean focused) /*-{
	    if (focused) {
	      this.@proteus.gwt.client.app.ui.common.codemirror.CodeMirrorTextAreaImpl::elem.focus();
	    } else {
	      this.@proteus.gwt.client.app.ui.common.codemirror.CodeMirrorTextAreaImpl::elem.blur();
	    }
	  }-*/;

	  public void setHTML(String html) {
	    DOM.setElementProperty(elem, "value", html);
	  }

	  public void setOwner(HasInitializeHandlers owner) {
	    this.owner = owner;
	  }

	  public void setText(String text) {
	    DOM.setElementProperty(elem, "value", text);
	  }

	  public void uninitElement() {
	  }

	  protected Element createElement() {
	    return DOM.createTextArea();
	  }

	  protected void hookEvents() {
	    DOM.sinkEvents(elem, Event.MOUSEEVENTS | Event.KEYEVENTS | Event.ONCHANGE
	        | Event.ONCLICK | Event.FOCUSEVENTS);
	  }

	  protected void onElementInitialized() {
	    hookEvents();
	    if (owner != null) {
	      InitializeEvent.fire(owner);
	    }
	  }
}
