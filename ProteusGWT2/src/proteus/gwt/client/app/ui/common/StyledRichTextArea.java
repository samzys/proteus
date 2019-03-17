/**
 * 
 */
package proteus.gwt.client.app.ui.common;

import proteus.gwt.shared.highlighter.ISyntaxHighlighter;
import proteus.gwt.shared.highlighter.RegexModelicaSyntaxHighlighter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RichTextArea;

@Deprecated
/**
 * Use CodeMirror wrappers instead
 * 
 * @author Lei Ting
 * Created Apr 15, 2011
 */
public class StyledRichTextArea extends RichTextArea {

	interface MyResources extends ClientBundle {
		@NotStrict
		@Source("highlighting.css")
		CssResource css();
	}
	
	private static final MyResources resources = GWT.create(MyResources.class);
	
	Timer injectionTimer = null;
	
	public StyledRichTextArea() {
		super();

		// to apply code styles in the iframe
//		resources.css().ensureInjected();
		
		this.ensureResourceInjected();
		this.addMouseUpHandler(new MouseUpHandler() {
			@Override
			public void onMouseUp(MouseUpEvent event) {
				onContentChanged();
			}			
		});
		this.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				onContentChanged();	
			}
		});
		
		// unable to support highlighted code editing
		this.setEnabled(false);
	}
	
	private void ensureResourceInjected() {
		injectionTimer = new Timer() {

			@Override
			public void run() {
				if (getElement() == null)
					return;
				
				IFrameElement iframe = IFrameElement.as(getElement());
				if (iframe == null) 
					return;

				Document document = null;
				// this causes an exception in hosted mode
				// not yet tested with compiled code				
				// uncomment below line to see syntax highlighted texts
//				document = iframe.getContentDocument();
				if (document != null && document.hasChildNodes()) {
					Element html = document.getDocumentElement();
					if (html != null) {
						Element head = html.getFirstChildElement();
						if (head != null) {
							// inject styles
							injectStyles(head);
							
							// stop timer
							injectionTimer.cancel();
							injectionTimer = null;
						}
					}
				}
			}
			
		};
		injectionTimer.scheduleRepeating(1000);
	}
	
	private void injectStyles(Element head) {
		String text = "<style>" + resources.css().getText() + "</style>";
		
		head.setInnerHTML(text);
	}

	@Override
	public void setHTML(String html) {
		ISyntaxHighlighter highlighter = new RegexModelicaSyntaxHighlighter();
		String s = highlighter.highlight(html);
		
		super.setHTML(s);
	}
	
	/**
	 * To be override 
	 */
	protected void onContentChanged() {
	}
}
