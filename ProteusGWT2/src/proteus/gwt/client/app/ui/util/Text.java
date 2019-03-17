package proteus.gwt.client.app.ui.util;

import java.util.List;

import proteus.gwt.client.app.ui.canvas.ParameterProperty;
import proteus.gwt.shared.graphics.Color;
import proteus.gwt.shared.graphics.Font;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.util.Utils;

public class Text extends FilledShape {

	public static final String NAME = "Text";

	private Rectangle extent;
	
	private final int SMALL_GAP = 10;

	private Font font;

	public String fontName;

	public int fontSize = 12;

	// public TextAlignment horizontalAlignment;// not used

	protected float lineThickness = 1.0f;

	public String textString, uTextString;

	// public TextStyle textStyle;

	private Rectangle tx;

	public Text(GraphicCanvas canvas, List<NamedArgument> args) {// maryam
		super(canvas, args);
		if (args != null) {
			for (NamedArgument arg : args) {
				if (arg.name.equals("extent")) {
					String s = arg.expression.getObjectModel().toCode();
					extent = GraphicUtils.parseRectangle(s);

				} else if (arg.name.equals("textString")
						|| arg.name.equals("string")) {
					String s = arg.expression.getObjectModel().toCode();
					textString = GraphicUtils.parseString(s);
				} else if (arg.name.equals("fontSize")) {
					String s = arg.expression.getObjectModel().toCode();
					fontSize = (int) Double.parseDouble(s);
				} else if (arg.name.equals("fontName")) {
					String s = arg.expression.getObjectModel().toCode();
					fontName = GraphicUtils.parseString(s);
				}
			}
			if (textString != null && textString.contains("%")) {
				uTextString = textString;
			}
		}

	}

	public Text(GraphicCanvas canvas, List<NamedArgument> args,
			List<ParameterProperty> paraList) {
		super(canvas, args);
		this.paraList = paraList;
		if (args != null) {
			for (NamedArgument arg : args) {
				if (arg.name.equals("visible")) {
					String propName = arg.expression.getObjectModel().toCode();
					String value = getParameterProperty(propName);
					if (value == null) {
						visible = true;
					} else {
						if (value.equalsIgnoreCase("false")) {
							visible = false;
						} else {
							visible = true;
						}
					}
				} else if (arg.name.equals("extent")) {
					String s = arg.expression.getObjectModel().toCode();
					extent = GraphicUtils.parseRectangle(s);
				} else if (arg.name.equals("textString")
						|| arg.name.equals("string")) {
					String s = arg.expression.getObjectModel().toCode();
					textString = GraphicUtils.parseString(s);
				} else if (arg.name.equals("fontSize")) {
					String s = arg.expression.getObjectModel().toCode();
					fontSize = (int) Double.parseDouble(s);
				} else if (arg.name.equals("fontName")) {
					String s = arg.expression.getObjectModel().toCode();
					fontName = GraphicUtils.parseString(s);
				}
			}
			uTextString = textString;
			textString = solvePercentageSign(uTextString);

		}
	}

	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}

	/**
	 * @return the fontSize
	 */
	public int getFontSize() {
		return fontSize;
	}

	public String getppValue(String propName) {
		String value = propName;
		for (ParameterProperty para : paraList) {
			if (para.getName().equals(propName)) {
				value = para.getValue();
				break;
			}
		}
		return value;
	}

	public String getpValue(String tString) {
		if (tString != null && !tString.equals("")) {
			int indexToken = tString.indexOf("%");
			if (indexToken != -1) {
				String retv = tString.substring(indexToken + 1);
				String ppValue = getParameterProperty(retv);
				if(ppValue!=null) {
					retv =  ppValue;
				}
				return retv;
			} else
				return tString;
		}
		return null;
	}

	/**
	 * @return the t
	 */
	public Rectangle getT() {
		return tx;
	}

	@Override
	public void paint(Graphics2D g2) {
		if (!visible)
			return;
		if (textString == null || extent == null)
			return;

		update();

		g2.saveTransform();

		// if (doFill(g2, tx)) {
		// g2.fillRect(tx.x, tx.y, tx.width, tx.height);
		// }
		g2.setColor(lineColor);
		if (font == null) {
			if (tx.width <= 0) {
				tx.width = Integer.MAX_VALUE;
			}
			if (tx.height <= 0) {
				tx.height = Integer.MAX_VALUE;
			}

			font = new Font("Arial", Font.PLAIN, 42);
		}

		g2.setFont(font);
		g2.setColor(Color.blue);
		int[] stringSize = g2.getStringSize(textString);
		double xpos = tx.x + (tx.width - stringSize[0]) / 2;
		double ypos = tx.y + (tx.height + stringSize[1] / 2) / 2+SMALL_GAP ;

		
		g2.translate(xpos, ypos);
		g2.drawString(textString, 0, 0);
		g2.restoreTransform();
	}

	/**
	 * @param font
	 *            the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}

	/**
	 * @param fontSize
	 *            the fontSize to set
	 */
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * @param t
	 *            the t to set
	 */
	public void setT(Rectangle t) {
		this.tx = t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * proteus.gwt.client.app.ui.util.GraphicItem#setViewRect(proteus.gwt.client
	 * .graphics.Rectangle)
	 */
	@Override
	public void setViewRect(Rectangle viewRect) {
		super.setViewRect(viewRect);
		tx = transformRect2(transformRect(extent));
	}

	/**
	 * @param text
	 *            assume only one "=" here for the string and assume on each
	 *            side of the "=", only one "%" sign present.
	 * @return
	 */
	public String solvePercentageSign(String text) {
		if (text != null && text.contains("%")) {
			StringBuilder sb = new StringBuilder();
			String textToken[] = text.split("=");
			if (textToken.length == 2) {
				// special case first "%=name"
				if (textToken[0].equals("%") && !textToken[1].contains("%")
						&& !textToken[1].equals("")) {
					sb.append(textToken[1]);
					sb.append("=");
					sb.append(getParameterProperty(textToken[1]));
				} else {
					String s1 = getpValue(textToken[0]);
					if (s1 != null) {
						sb.append(s1);
						sb.append("=");
					}
					String s2 = getpValue(textToken[1]);
					if (s2 != null)
						sb.append(s2);
				}
			} else if (textToken.length == 1) {
				String s2 = getpValue(textToken[0]);
				if (s2 != null)
					sb.append(s2);
			}
			return sb.toString();
		}
		return text;
	}

	@Override
	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(NAME + "(");
		String[] args = toArgs();
		sb.append(Utils.join(args, ", "));
		sb.append(")");
		return sb.toString();
	}

	public void update() {
		// update property value
		if (uTextString == null) {
			return;
		}
		textString = solvePercentageSign(uTextString);
	}
}