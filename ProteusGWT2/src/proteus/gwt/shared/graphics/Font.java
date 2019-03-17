/**
 * 
 */
package proteus.gwt.shared.graphics;

import java.util.Map;


/**
 * @author Lei Ting
 *
 */
public class Font {

//	public static final String DEFAULT_FACE = "Times New Roman";
	public static final String DEFAULT_FACE = "Arial";
	public static final int DEFAULT_SIZE = 14;
	public static final int DEFAULT_SIZE_BROWSER = 16;

	public static final String ARIAL="Arial", CALIBRI = "Calibri", VERDANA="Verdana";
	public static final int PLAIN = 0x00;
	public static final int BOLD = 0x01;
	public static final int ITALIC = 0x02;
	public static final int UNDERLINE = 0x04;
	public static final int STRIKE = 0x08;
	public static final int NOT_SPECIFIED = 0x80;

	public String face;
	public Integer style;
	public Integer size;

	public Font() {
		this(DEFAULT_FACE);
	}

	public Font(String face) {
		this.face = face;
		this.style = PLAIN;
		this.size = DEFAULT_SIZE;
	}
	
	public Font(String face, int style, int size) {
		this.face = face;
		this.style = style;
		this.size = size;
	}
	
	public Font(Font font) {
		if (font != null) {
			this.face = font.face;
			this.style = font.style;
			this.size = font.size;
		}
	}
	
	public boolean equals(Font font) {
		if (this.face == null || 
				this.style == null || 
				this.size == null)
			return false;
		
		if (font == null) {
			return false;
		}
		
		return this.face.equalsIgnoreCase(font.face) &&
			this.style == font.style && 
			this.size == font.size;
	}
	
	/**
	 * to css string
	 * @param font
	 * @return
	 */
	public String toString() {
		String s = "";
		
		if ((style&ITALIC) != 0) {
			s += "italic ";
		}
		if ((style&BOLD) != 0) {
			s += "bold ";
		}
		s += size + "px ";
		s += face;
		
		return s;
	}
	
	public static Font getFont(Map<TextAttribute,?> attributes) {
		return null;
	}
}
