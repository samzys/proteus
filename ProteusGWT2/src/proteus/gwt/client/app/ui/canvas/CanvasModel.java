package proteus.gwt.client.app.ui.canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.LogMessagePanelEvent;
import proteus.gwt.client.app.ui.MessagePanel.Message;
import proteus.gwt.client.app.ui.util.MiscUtils;

public class CanvasModel {

	protected List<CanvasItem> canvasItems = new ArrayList<CanvasItem>();

	private CanvasItem[] ciArray;

	private int editCount = 0, editIndex = 0;
	private boolean editable;

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	protected List<CanvasModelListener> listeners = new LinkedList<CanvasModelListener>();

	protected CanvasItem[] selectedItems;

	public CanvasModel() {
		this(false);
	}

	public CanvasModel(boolean editable) {
		this.editable = editable;
	}

	public void add(CanvasItem item) {
		add(new CanvasItem[] { item });
	}

	public void add(CanvasItem[] items) {
		canvasItems.addAll(Arrays.asList(items));
		ciArray = null;
		int i=0;
		for (CanvasModelListener l : listeners) {
			//fire listener
			l.itemsAdded(items);
			i++;
		}
	}

	public void addCanvasModelListener(CanvasModelListener l) {
		listeners.add(l);
	}

	public boolean canRedo() {
		return editIndex < editCount;
	}

	public int getCanvasItemCount() {
		return canvasItems.size();
	}

	// HINT: Don't change method to return a copy of the ciArray, coz that would
	// break the reordering actions in ModelViewInstance
	public CanvasItem[] getCanvasItems() {
		if (ciArray == null) {
			ciArray = canvasItems.toArray(new CanvasItem[0]);
		}
		return ciArray;
	}

	public CanvasItem[] getSelectedItems() {
		return selectedItems;
	}


	// postEdit take argument UndoEvent which is not available iside
	public void postEdit() {// UndoableEditEvent e) {
		editCount = ++editIndex;
		// undoManager.addEdit(e.getEdit());
		for (CanvasModelListener l : listeners) {
			l.editHappened(this);
		}
	}

	public void redo() {
	}

	public void remove(CanvasItem item) {
		remove(new CanvasItem[] { item });
	}

	public void remove(CanvasItem[] items) {
		if (items == null || items.length <= 0) {
			return;
		}

		canvasItems.removeAll(Arrays.asList(items));

		if (selectedItems != null) {
			List<CanvasItem> nsi = new LinkedList<CanvasItem>();
			for (CanvasItem si : selectedItems) {
				boolean f = false;
				for (CanvasItem item : items) {
					if (item == si) {
						f = true;
						break;
					}
				}
				if (!f) {
					nsi.add(si);
				}
			}
			setSelectedItems(nsi.toArray(new CanvasItem[0]));
		}
		ciArray = null;
		for (CanvasModelListener l : listeners) {
			l.itemsRemoved(items);
		}
	}

	public void removeCanvasModelListener(CanvasModelListener l) {
		listeners.remove(l);
	}

	public void setCanvasItems(CanvasItem[] list) {
		ciArray = null;
		canvasItems = Arrays.asList(list);
	}


	public void setSelectedItems(CanvasItem[] selectedItems) {
		if (selectedItems == null) {
			this.selectedItems = null;
		} else {
			this.selectedItems = new CanvasItem[selectedItems.length];
			System.arraycopy(selectedItems, 0, this.selectedItems, 0,
					selectedItems.length);
		}
		for (CanvasModelListener l : listeners) {
			l.selectionChanged(selectedItems);
		}
	}

	public void undo() {

		for (CanvasModelListener l : listeners) {
			l.editHappened(this);
		}
	}
}
