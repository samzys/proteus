package proteus.gwt.client.app.ui.canvas;

import java.util.Arrays;
import java.util.Comparator;

import proteus.gwt.client.app.ui.canvas.Item.State;

public class CanvasUtils {

	public static void alignBottom(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double mayy = items[0].getY();
		for (int i = 1; i < items.length; i++) {
			mayy = Math.max(mayy, items[i].getY() + items[i].getHeight());
		}
		for (CanvasItem item : items) {
			item.setY(mayy - item.getHeight());
		}
	}

	public static void alignCenter(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		int sumx = 0;
		for (int i = 0; i < items.length; i++) {
			sumx += items[i].getX() + items[i].getWidth() / 2;
		}
		double avgx = sumx / items.length;
		for (CanvasItem item : items) {
			item.setX(avgx - item.getWidth() / 2);
		}
	}

	// public static MouseEvent convertMouseEvent(MouseEvent e, Canvas canvas,
	// CanvasItem item) {
	// Point loc = e.getPoint();
	// Point newLoc = MiscUtils.scaleLoc(loc, 1 / canvas.getScale());
	// newLoc.translate(-item.getX(), -item.getY());
	// MouseEvent ne = new MouseEvent(e.getComponent(), e.getID(),
	// e.getWhen(), e.getModifiers(), newLoc.x, newLoc.y, e
	// .getClickCount(), e.isPopupTrigger(), e.getButton());
	// ne.setSource(item);
	// return ne;
	// }
	//	
	public static void alignLeft(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double minx = items[0].getX();
		for (int i = 1; i < items.length; i++) {
			minx = Math.min(minx, items[i].getX());
		}
		for (CanvasItem item : items) {
			item.setX(minx);
		}
	}

	public static void alignMiddle(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		int sumy = 0;
		for (int i = 0; i < items.length; i++) {
			sumy += items[i].getY() + items[i].getHeight() / 2;
		}
		int avgy = sumy / items.length;
		for (CanvasItem item : items) {
			item.setY(avgy - item.getHeight() / 2);
		}
	}

	public static void alignRight(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double maxx = items[0].getX();
		for (int i = 1; i < items.length; i++) {
			maxx = Math.max(maxx, items[i].getX() + items[i].getWidth());
		}
		for (CanvasItem item : items) {
			item.setX(maxx - item.getWidth());
		}
	}

	public static void alignTop(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double miny = items[0].getY();
		for (int i = 1; i < items.length; i++) {
			miny = Math.min(miny, items[i].getY());
		}
		for (CanvasItem item : items) {
			item.setY(miny);
		}
	}

	public static void bringForward(CanvasItem[] list, CanvasItem[] items) {
		if (items == null || items.length <= 0) {
			return;
		}
		CanvasItem[] ordered = new CanvasItem[items.length];
		int i = 0;
		for (int j = 0; j < list.length; j++) {
			for (int k = 0; k < items.length; k++) {
				if (list[j] == items[k]) {
					ordered[i++] = items[k];
				}
			}
		}
		i--;
		while (i >= 0) {
			for (int j = 0; j < list.length - 1; j++) {
				if (list[j] == ordered[i]) {
					CanvasItem ci = list[j];
					list[j] = list[j + 1];
					list[j + 1] = ci;
					break;
				}
			}
			i--;
		}
	}

	public static void bringToFront(CanvasItem[] list, CanvasItem[] items) {
		if (items == null || items.length <= 0) {
			return;
		}
		CanvasItem[] ordered = new CanvasItem[items.length];
		int i = 0;
		for (int j = 0; j < list.length; j++) {
			for (int k = 0; k < items.length; k++) {
				if (list[j] == items[k]) {
					ordered[i++] = items[k];
				}
			}
		}
		i--;
		int pos = list.length - 2;
		while (i >= 0) {
			for (int j = 0; j < list.length - 1; j++) {
				if (list[j] == ordered[i]) {
					CanvasItem ci = list[j];
					for (int k = j; k < pos; k++) {
						list[k] = list[k + 1];
					}
					list[pos--] = ci;
					break;
				}
			}
			i--;
		}
	}

	private static State checkHorizontalState(State state_value) {
		Item.State new_state_value = null;
		if (state_value.equals(Item.State.A)) {
			new_state_value = Item.State.B;
		} else if (state_value.equals(Item.State.B)) {
			new_state_value = Item.State.A;
		} else if (state_value.equals(Item.State.C)) {
			new_state_value = Item.State.F;
		} else if (state_value.equals(Item.State.D)) {
			new_state_value = Item.State.G;
		} else if (state_value.equals(Item.State.E)) {
			new_state_value = Item.State.H;
		} else if (state_value.equals(Item.State.F)) {
			new_state_value = Item.State.C;
		} else if (state_value.equals(Item.State.G)) {
			new_state_value = Item.State.D;
		} else if (state_value.equals(Item.State.H)) {
			new_state_value = Item.State.E;
		} else {
			System.err.println("state out of scope");
		}
		return new_state_value;
	}

	private static State checkRotateAntiClock(State state_value) {
		State new_state_value = null;
		// The reason why the following code seems like Clockwise rotation
		// is that,
		// in Math, counter clockwise is assumed as positive direction, so
		// to rotate 90 degree
		// clockwise, we should 270 rotate counter clockwise
		if (state_value.equals(Item.State.A)) {
			new_state_value = Item.State.D;
		} else if (state_value.equals(Item.State.B)) {
			new_state_value = Item.State.H;
		} else if (state_value.equals(Item.State.C)) {
			new_state_value = Item.State.G;
		} else if (state_value.equals(Item.State.D)) {
			new_state_value = Item.State.F;
		} else if (state_value.equals(Item.State.E)) {
			new_state_value = Item.State.A;
		} else if (state_value.equals(Item.State.F)) {
			new_state_value = Item.State.E;
		} else if (state_value.equals(Item.State.G)) {
			new_state_value = Item.State.B;
		} else if (state_value.equals(Item.State.H)) {
			new_state_value = Item.State.C;
		}
		return new_state_value;
	}

	private static State checkRotateClockwise(State state_value) {
		State new_state_value = null;
		// The reason why the following code seems like Counter Clockwise
		// rotation is that,
		// in Math, counter clockwise is assumed as positive direction, so
		// to rotate 90 degree
		// clockwise, we should -270 rotate counter clockwise
		if (state_value.equals(Item.State.A)) {
			new_state_value = Item.State.E;
		} else if (state_value.equals(Item.State.B)) {
			new_state_value = Item.State.G;
		} else if (state_value.equals(Item.State.C)) {
			new_state_value = Item.State.H;
		} else if (state_value.equals(Item.State.D)) {
			new_state_value = Item.State.A;
		} else if (state_value.equals(Item.State.E)) {
			new_state_value = Item.State.F;
		} else if (state_value.equals(Item.State.F)) {
			new_state_value = Item.State.D;
		} else if (state_value.equals(Item.State.G)) {
			new_state_value = Item.State.C;
		} else if (state_value.equals(Item.State.H)) {
			new_state_value = Item.State.B;
		}
		return new_state_value;
	}

	private static State checkVerticalState(State state_value) {
		Item.State new_state_value = null;
		if (state_value.equals(Item.State.A)) {
			new_state_value = Item.State.C;
		} else if (state_value.equals(Item.State.B)) {
			new_state_value = Item.State.F;
		} else if (state_value.equals(Item.State.C)) {
			new_state_value = Item.State.A;
		} else if (state_value.equals(Item.State.D)) {
			new_state_value = Item.State.H;
		} else if (state_value.equals(Item.State.E)) {
			new_state_value = Item.State.G;
		} else if (state_value.equals(Item.State.F)) {
			new_state_value = Item.State.B;
		} else if (state_value.equals(Item.State.G)) {
			new_state_value = Item.State.E;
		} else if (state_value.equals(Item.State.H)) {
			new_state_value = Item.State.D;
		}
		return new_state_value;
	}

	public static void distributeHorizontalCenters(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double minxc = items[0].getX() + items[0].getWidth() / 2;
		double maxxc = minxc;
		for (int i = 1; i < items.length; i++) {
			minxc = Math.min(minxc, items[i].getX() + items[i].getWidth() / 2);
			maxxc = Math.max(maxxc, items[i].getX() + items[i].getWidth() / 2);
		}
		double xc = minxc;
		double dx = (maxxc - minxc) / (items.length - 1);
		Arrays.sort(items, new Comparator<CanvasItem>() {
			public int compare(CanvasItem o1, CanvasItem o2) {
				return (int) (o1.getX() - o2.getX());
			}
		});
		for (CanvasItem item : items) {
			item.setX(xc - item.getWidth() / 2);
			xc += dx;
		}
	}

	public static void distributeHorizontalSpacing(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double minx = items[0].getX();
		double maxx = items[0].getX() + items[0].getWidth();
		double sumx = items[0].getWidth();
		for (int i = 1; i < items.length; i++) {
			minx = Math.min(minx, items[i].getX());
			maxx = Math.max(maxx, items[i].getX() + items[i].getWidth());
			sumx += items[i].getWidth();
		}
		double x = minx;
		double dx = (maxx - minx - sumx) / (items.length - 1);
		Arrays.sort(items, new Comparator<CanvasItem>() {
			public int compare(CanvasItem o1, CanvasItem o2) {
				return (int) (o1.getX() - o2.getX());
			}
		});
		for (CanvasItem item : items) {
			item.setX(x);
			x += item.getWidth() + dx;
		}
	}

	public static void distributeVerticalCenters(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double minyc = items[0].getY() + items[0].getHeight() / 2;
		double maxyc = minyc;
		for (int i = 1; i < items.length; i++) {
			minyc = Math.min(minyc, items[i].getY() + items[i].getHeight() / 2);
			maxyc = Math.max(maxyc, items[i].getY() + items[i].getHeight() / 2);
		}
		double yc = minyc;
		double dy = (maxyc - minyc) / (items.length - 1);
		Arrays.sort(items, new Comparator<CanvasItem>() {
			public int compare(CanvasItem o1, CanvasItem o2) {
				return (int) (o1.getY() - o2.getY());
			}
		});
		for (CanvasItem item : items) {
			item.setY(yc - item.getHeight() / 2);
			yc += dy;
		}
	}

	public static void distributeVerticalSpacing(CanvasItem[] items) {
		if (items == null || items.length < 2) {
			return;
		}
		double miny = items[0].getY();
		double maxy = items[0].getY() + items[0].getHeight();
		double sumy = items[0].getHeight();
		for (int i = 1; i < items.length; i++) {
			miny = Math.min(miny, items[i].getY());
			maxy = Math.max(maxy, items[i].getY() + items[i].getHeight());
			sumy += items[i].getHeight();
		}
		double y = miny;
		double dy = (maxy - miny - sumy) / (items.length - 1);
		Arrays.sort(items, new Comparator<CanvasItem>() {
			public int compare(CanvasItem o1, CanvasItem o2) {
				return (int) (o1.getY() - o2.getY());
			}
		});
		for (CanvasItem item : items) {
			item.setY(y);
			y += item.getHeight() + dy;
		}
	}

	public static void flipHorizontal(CanvasItem cci) {
		flipHorizontal(new CanvasItem[] { cci });
	}

	public static void flipHorizontal(CanvasItem[] sel) {
		if (sel == null || sel.length <= 0) {
			return;
		}
		for (CanvasItem item : sel) {
			Item.State state_value = item.getState();
			Item.State new_state_value = checkHorizontalState(state_value);
			item.setState(new_state_value, state_value);

			if (item instanceof ComponentCanvasItem) {
				for (ConnectorItem ci : ((ComponentCanvasItem) item)
						.getConnectorItems()) {
					state_value = ci.getState();
					new_state_value = checkHorizontalState(state_value);
					ci.setState(new_state_value, state_value, ci);
				}
				item.updateCodeCanvas();
			}
		}
	}

	/**
	 * @author Maryam
	 * @func when flipHorizontal button is pressed, this function is called
	 *       after firing the event
	 */

	public static void flipHorizontal(ConnectorItem item) {
		if (item == null)
			return;
		Item.State state_value = item.getState();
		Item.State new_state_value = checkHorizontalState(state_value);
		item.setState(new_state_value, state_value);
	}

	public static void flipVertical(CanvasItem item) {
		flipVertical(new CanvasItem[] { item });
	}

	/**
	 * @author Maryam
	 * @func when flipVertical button is pressed, this function is called after
	 *       firing the event
	 */
	public static void flipVertical(CanvasItem[] sel) {
		if (sel == null || sel.length <= 0) {
			return;
		}
		for (CanvasItem item : sel) {

			Item.State state_value = item.getState();
			Item.State new_state_value = checkVerticalState(state_value);

			item.setState(new_state_value, state_value);

			if (item instanceof ComponentCanvasItem) {
				for (ConnectorItem ci : ((ComponentCanvasItem) item)
						.getConnectorItems()) {
					state_value = ci.getState();
					new_state_value = checkVerticalState(state_value);

					ci.setState(new_state_value, state_value, ci);

				}
				item.updateCodeCanvas();
			}
		}
	}

	public static void flipVertical(ConnectorItem item) {
		if (item == null)
			return;
		Item.State state_value = item.getState();
		Item.State new_state_value = checkVerticalState(state_value);

		item.setState(new_state_value, state_value);
	}

	/**
	 * @author Maryam
	 * @func when rotateCCW button is pressed, this function is called after
	 *       firing the event
	 */
	public static void rotateAntiClockwise(CanvasItem[] sel) {
		if (sel == null || sel.length <= 0) {
			return;
		}
		for (CanvasItem item : sel) {
			Item.State state_value = item.getState();
			Item.State new_state_value = checkRotateAntiClock(state_value);

			// The reason why the following code seems like Clockwise rotation
			// is that,
			// in Math, counter clockwise is assumed as positive direction, so
			// to rotate 90 degree
			// clockwise, we should 270 rotate counter clockwise

			item.setState(new_state_value, state_value);

			if (item instanceof ComponentCanvasItem) {
				for (ConnectorItem ci : ((ComponentCanvasItem) item)
						.getConnectorItems()) {
					state_value = ci.getState();
					new_state_value = checkRotateAntiClock(state_value);
					ci.setState(new_state_value, state_value, ci);
				}
				item.updateCodeCanvas();
			}
		}

	}

	public static void rotateAntiClockwise(ConnectorItem item) {
		if (item == null)
			return;
		Item.State state_value = item.getState();
		Item.State new_state_value = checkRotateAntiClock(state_value);

		item.setState(new_state_value, state_value);
	}

	/**
	 * @author Maryam
	 * @func when rotateClockwise button is pressed, this function is called
	 *       after firing the event
	 * @param sel
	 */
	public static void rotateClockwise(CanvasItem[] sel) {
		if (sel == null || sel.length <= 0) {
			return;
		}
		for (CanvasItem item : sel) {
			Item.State state_value = item.getState();
			Item.State new_state_value = checkRotateClockwise(state_value);
			item.setState(new_state_value, state_value);
			if (item instanceof ComponentCanvasItem) {
				for (ConnectorItem ci : ((ComponentCanvasItem) item)
						.getConnectorItems()) {
					state_value = ci.getState();
					new_state_value = checkRotateClockwise(state_value);
					ci.setState(new_state_value, state_value, ci);

				}
				item.updateCodeCanvas();
			}
		}
	}

	public static void rotateClockwise(ConnectorItem item) {
		if (item == null)
			return;
		Item.State state_value = item.getState();
		Item.State new_state_value = checkRotateClockwise(state_value);
		item.setState(new_state_value, new_state_value);
	}

	public static void sendBackward(CanvasItem[] list, CanvasItem[] items) {
		if (items == null || items.length <= 0) {
			return;
		}
		CanvasItem[] ordered = new CanvasItem[items.length];
		int i = 0;
		for (int j = 0; j < list.length; j++) {
			for (int k = 0; k < items.length; k++) {
				if (list[j] == items[k]) {
					ordered[i++] = items[k];
				}
			}
		}
		i = 0;
		while (i < ordered.length && ordered[i] != null) {
			for (int j = 1; j < list.length; j++) {
				if (list[j] == ordered[i]) {
					CanvasItem ci = list[j];
					list[j] = list[j - 1];
					list[j - 1] = ci;
					break;
				}
			}
			i++;
		}
	}

	public static void sendToBack(CanvasItem[] list, CanvasItem[] sel) {
		if (sel == null || sel.length <= 0) {
			return;
		}
		CanvasItem[] ordered = new CanvasItem[sel.length];
		int i = 0;
		for (int j = 0; j < list.length; j++) {
			for (int k = 0; k < sel.length; k++) {
				if (list[j] == sel[k]) {
					ordered[i++] = sel[k];
				}
			}
		}
		i = 0;
		int pos = 1;
		while (i < ordered.length && ordered[i] != null) {
			for (int j = 1; j < list.length; j++) {
				if (list[j] == ordered[i]) {
					CanvasItem ci = list[j];
					for (int k = j - 1; k >= pos; k--) {
						list[k + 1] = list[k];
					}
					list[pos++] = ci;
					break;
				}
			}
			i++;
		}
	}

}
