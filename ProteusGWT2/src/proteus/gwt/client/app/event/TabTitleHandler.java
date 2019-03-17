package proteus.gwt.client.app.event;

import proteus.gwt.client.app.ui.TabTitle;

public interface TabTitleHandler {
	public void close(TabTitle tabTitle);
	public void selected(TabTitle tabTitle);
	public void closeAll();
	public void closeOthers(TabTitle tabTitle);
	public void previousTab();
	public void nextTab();
}
