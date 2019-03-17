package proteus.gwt.client.app.ui.canvas;

import proteus.gwt.client.app.event.MouseEventHandler;
import proteus.gwt.shared.graphics.Graphics2D;

public abstract class ModeHandler extends MouseEventHandler {

	protected final int mode;

	public ModeHandler(int mode) {
		this.mode = mode;
	}

	public final int getMode() {
		return mode;
	}

	public abstract void finish();
	
	public abstract void init(Object... args);

	public abstract boolean canPaint();

	public abstract void paint(Graphics2D g2);

		
}
