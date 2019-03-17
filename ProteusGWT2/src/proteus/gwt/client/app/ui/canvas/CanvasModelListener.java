package proteus.gwt.client.app.ui.canvas;

public interface CanvasModelListener {

	public void itemsAdded(CanvasItem[] items);

	public void itemsRemoved(CanvasItem[] items);

	public void selectionChanged(CanvasItem[] selected);

	public void editHappened(CanvasModel model);
}
