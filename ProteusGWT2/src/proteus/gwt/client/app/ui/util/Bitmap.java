package proteus.gwt.client.app.ui.util;

import java.util.List;

import proteus.gwt.client.app.ui.canvas.ParameterProperty;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.graphics.Rectangle;
import proteus.gwt.shared.modelica.ClassDef.NamedArgument;
import proteus.gwt.shared.util.Utils;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;

public class Bitmap extends FilledShape {
	private int s_width, s_height;

	public static final String NAME = "Bitmap";

	public String filename;

	private Rectangle extent;

	private Image img;

	protected ImageElement imgEle;

	private Graphics2D g2;

	public Bitmap(GraphicCanvas canvas, List<NamedArgument> args,
			List<ParameterProperty> paraList) {
		super(canvas);
		if (args != null) {
			for (NamedArgument arg : args) {
				if (arg.name.equals("visible")) {
					String propName = arg.expression.getObjectModel().toCode();
					String value = getParameterProperty(propName);
					if (value == null) {
						visible = true;//default is visible
					} else {
						if (value.equalsIgnoreCase("false")) {
							visible = false;
						} else {
							visible = true;
						}
					}
				} else if (arg.name.equals("imageSource")) {
					String s = arg.expression.getObjectModel().toCode();
					String encodedString = GraphicUtils.parseString(s);
				} else if (arg.name.equals("fileName")
						|| arg.name.equals("name")) {
					String s = GraphicUtils.parseString(arg.expression
							.getObjectModel().toCode());
					setFilename(MiscUtils.getImagePath(s));
				} else if (arg.name.equals("extent")) {
					String s = arg.expression.getObjectModel().toCode();
					extent = GraphicUtils.parseRectangle(s);
				}
			}
		}
	}

	public Bitmap(GraphicCanvas canvas, List<NamedArgument> args) {
		super(canvas);
		if (args != null) {
			for (NamedArgument arg : args) {
				if (arg.name.equals("imageSource")) {
					String s = arg.expression.getObjectModel().toCode();
					String encodedString = GraphicUtils.parseString(s);
				} else if (arg.name.equals("fileName")
						|| arg.name.equals("name")) {
					String s = GraphicUtils.parseString(arg.expression
							.getObjectModel().toCode());
					System.out.println(s);
					setFilename(MiscUtils.getImagePath(s));
				} else if (arg.name.equals("extent")) {
					String s = arg.expression.getObjectModel().toCode();
					extent = GraphicUtils.parseRectangle(s);
				}
			}
		}
	}

	private void setFilename(String relativePath) {
		if(relativePath!=null) {
			img = new Image("/resources/images/classname"+relativePath);
		}
	}


	public String getFilename() {
		return filename;
	}
	
	public String toCode() {
		StringBuilder sb = new StringBuilder();
		sb.append(NAME + "(");
		String[] args = toArgs();
		sb.append(Utils.join(args, ", "));
		sb.append(")");
		return sb.toString();
	}

	public String[] toArgs() {
		return null;
	}

	public void paint(Graphics2D g2) {
		if(g2!=null) this.g2 = g2;
		if (!visible)
			return;
		Rectangle r;
		if (extent != null && img != null) {
			r = transformRect2(transformRect(extent));
			if (r != null) {
				g2.drawImage(img,r.x, r.y, r.width, r.height, null);
			}
		}
	}
}