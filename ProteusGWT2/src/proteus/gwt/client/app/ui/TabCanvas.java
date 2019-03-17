/**
 * 
 */
package proteus.gwt.client.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import proteus.gwt.client.app.AppController;
import proteus.gwt.client.app.event.ClearMessagePanelEvent;
import proteus.gwt.client.app.event.DropComponentEvent;
import proteus.gwt.client.app.event.MVIChangeEvent;
import proteus.gwt.client.app.event.TabTitleHandler;
import proteus.gwt.client.app.ui.MessagePanel.Message;
import proteus.gwt.shared.modelica.ClassDef;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author gao lei
 * 
 */
public class TabCanvas extends Composite implements TabTitleHandler {

	private static final Logger logger = Logger.getLogger("TabCanvas");
	
	private static ProteusTabCanvasUiBinder uiBinder = GWT
			.create(ProteusTabCanvasUiBinder.class);

	interface ProteusTabCanvasUiBinder extends UiBinder<Widget, TabCanvas> {
	}

	public @UiField
	TabLayoutPanel tabPanel;
	private HashMap<TabTitle, ModelViewInstance> tabContentMap = new HashMap<TabTitle, ModelViewInstance>();
	private ArrayList<TabLinkData> linkDataList = new ArrayList<TabLinkData>();
	public ModelViewInstance currentMVI;
	public String currentTitleName;
	private HandlerManager eventBus;
	private TabLink tabLink;
	private PopupPanel tabLinkPanel;
	private final int tabLinkWidth = 50;

	public TabCanvas() {
		initWidget(uiBinder.createAndBindUi(this));
		ClassDef classDef = AppController.newComponentInstance("model");
		ModelViewInstance oneCanvas = new ModelViewInstance(classDef, true);
		addToTabPanel(oneCanvas, new TabTitle("Model", this));

		currentMVI = oneCanvas;
		currentTitleName = "Model";
		initTabLink();
	}

	// public void newTab(String name,boolean edited) {
	// ModelViewInstance canvas = new ModelViewInstance(edited);
	//
	// addToTabPanel(canvas, new TabTitle(name, this));
	// currentMVI= canvas;
	// tabPanel.selectTab(canvas);
	// eventBus.fireEvent(new MVIChangeEvent(currentMVI));
	// }

	public void newTab(String name, ModelViewInstance canvas) {
		TabTitle tabTitle = new TabTitle(name, this);
		addToTabPanel(canvas, tabTitle);
		currentMVI = canvas;
		currentTitleName = name;
		tabPanel.selectTab(canvas);
		eventBus.fireEvent(new MVIChangeEvent(currentMVI));
		// ///////////////////////////////////
		while (tabTitle.getAbsoluteLeft() + tabTitle.getOffsetWidth()
				+ tabLinkWidth > Window.getClientWidth()) {
			tabMoveLeft();
			tabLinkPanel.setPopupPosition(Window.getClientWidth()
					- tabLinkWidth, this.getAbsoluteTop());
			tabLinkPanel.show();
		}

		// Log.warn("This: " + this.getOffsetHeight() + " "
		// + this.getOffsetWidth());
		// Log.warn("TabPanel: " + tabPanel.getOffsetHeight() + " "
		// + tabPanel.getOffsetWidth());
		//
		// Log.warn("tabTitle: " + tabTitle.getOffsetHeight() + " "
		// + tabTitle.getOffsetWidth() + " " + tabTitle.getAbsoluteLeft());
		// Log.warn("" + Window.getClientWidth());

	}

	/**
	 * @return the currentMVI
	 */
	public ModelViewInstance getCurrentMVI() {
		return currentMVI;
	}

	/**
	 * @param currentMVI
	 *            the currentMVI to set
	 */
	public void setCurrentMVI(ModelViewInstance currentMVI) {
		this.currentMVI = currentMVI;
	}

	public void setEventBus(HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	@Override
	public void close(TabTitle tabTitle) {
		tabPanel.remove(tabContentMap.remove(tabTitle));
		tabDetele(tabTitle);
		// if no hide tab
		logger.log(Level.INFO, "all show: " + tabAllShowed());
		if (tabAllShowed()) {
			tabLinkPanel.hide();
		} else {
			TabTitle lastTitle = (TabTitle) tabPanel.getTabWidget(tabPanel
					.getWidgetCount() - 1);
			// Log.info(lastTitle.tabText.getText());

			if (lastTitle.getAbsoluteLeft() + lastTitle.getOffsetWidth()
					+ tabLinkWidth < Window.getClientWidth()) {
				boolean isAddLeft = tabAddLeft();
				logger.log(Level.INFO, "isAddLeft: " + isAddLeft);
				if (!isAddLeft) {
					tabAddRight();
				}

			}

		}

	}

	@Override
	public void selected(TabTitle tabTitle) {
		currentMVI = tabContentMap.get(tabTitle);
		currentTitleName = tabTitle.getWholeText();
		eventBus.fireEvent(new MVIChangeEvent(currentMVI));
	}

	@Override
	public void closeAll() {
		tabDeteleAll();
		Set<TabTitle> allTab = tabContentMap.keySet();
		for (TabTitle tabTitle : allTab) {
			tabPanel.remove(tabContentMap.get(tabTitle));
		}
		tabLinkPanel.hide(); 
		eventBus.fireEvent(new ClearMessagePanelEvent(Message.ALL));
	}

	@Override
	public void closeOthers(TabTitle tabTitleSelected) {
		Set<TabTitle> allTab = tabContentMap.keySet();
		tabDeteleOthers(tabTitleSelected);
		for (TabTitle tabTitle : allTab) {
			if (!tabTitle.equals(tabTitleSelected))
				tabPanel.remove(tabContentMap.get(tabTitle));
		}
		selected(tabTitleSelected);
		tabLinkPanel.hide();
	}

	@Override
	public void previousTab() {
		if (!this.linkDataList.get(0).showed) {
			tabMoveRight();
			tabAddLeft();
		} else {
			// this.tabLink.imgPrevious.setVisible(false);
		}
	}

	@Override
	public void nextTab() {
		if (!this.linkDataList.get(linkDataList.size() - 1).showed) {
			tabMoveLeft();
			tabAddRight();
		} else {
			// this.tabLink.imgNext.setVisible(false);
		}

	}

	private void tabMoveLeft() {
		for (TabLinkData data : this.linkDataList) {
			if (data.showed) {
				data.showed = false;
				this.tabPanel.remove(data.wiget);
				this.tabLink.imgPrevious.setVisible(true);
				break;
			}
		}
	}

	private void tabMoveRight() {

		for (int i = this.linkDataList.size() - 1; i >= 0; i--) {
			TabLinkData data = linkDataList.get(i);
			if (data.showed) {
				data.showed = false;
				this.tabPanel.remove(data.wiget);
				this.tabLink.imgNext.setVisible(true);
				break;
			}
		}
	}

	private boolean tabAddRight() {
		boolean ret = false;
		TabLinkData previousData = null;
		if (linkDataList.size() < 2) {
			return ret;
		}
		for (int i = this.linkDataList.size() - 1; i >= 0; i--) {
			TabLinkData data = linkDataList.get(i);
			if (data.showed) {
				if (previousData != null) {
					previousData.showed = true;
					this.tabPanel.add(previousData.wiget, previousData.title);
					ret = true;
					break;
				}
			}
			previousData = data;
		}
		return ret;
	}

	private boolean tabAddLeft() {
		boolean ret = false;
		TabLinkData previousData = null;
		if (linkDataList.size() < 2) {
			return ret;
		}
		// String s ="";
		// for (TabLinkData data : this.linkDataList){
		// s+= " "+data.showed;
		// }
		// Log.info("s:"+s);
		for (TabLinkData data : this.linkDataList) {
			if (data.showed) {
				if (previousData != null && previousData.showed == false) {
					previousData.showed = true;
					this.tabPanel.insert(previousData.wiget,
							previousData.title, 0);
					return true;
				}
			}
			previousData = data;
		}
		return ret;
	}

	private boolean tabAllShowed() {
		boolean ret = true;
		for (TabLinkData data : this.linkDataList) {
			if (!data.showed) {
				ret = false;
				break;
			}
		}
		return ret;
	}

	private void tabDetele(TabTitle title) {
		for (TabLinkData data : this.linkDataList) {
			if (data.title.equals(title)) {
				linkDataList.remove(data);
				break;
			}
		}
	}

	private void tabDeteleOthers(TabTitle title) {
		TabLinkData tabSelectedData = null;
		for (TabLinkData data : this.linkDataList) {
			if (data.title.equals(title)) {
				tabSelectedData = data;
				break;
			}
		}

		linkDataList.clear();
		linkDataList.add(tabSelectedData);
	}

	private void tabDeteleAll() {
		linkDataList.clear();
	}

	private void initTabLink() {
		tabLink = new TabLink(this);
		tabLinkPanel = new PopupPanel(false);
		tabLinkPanel.setStyleName("tabLink");
		tabLinkPanel.add(tabLink);
	}

	private void addToTabPanel(ModelViewInstance currentProteusCanvas,
			TabTitle tabtitle) {
		tabPanel.add(currentProteusCanvas, tabtitle);
		tabContentMap.put(tabtitle, currentProteusCanvas);
		TabLinkData linkdata = new TabLinkData(tabtitle, currentProteusCanvas,
				true);
		this.linkDataList.add(linkdata);
		currentProteusCanvas.getDiagramCanvas().addMouseMoveHandler(
				new MouseMoveHandler() {

					@Override
					public void onMouseMove(MouseMoveEvent event) {
						int x = event.getX();
						int y = event.getY();

						eventBus.fireEvent(new DropComponentEvent(x, y));
					}
				});
	}

	class TabLinkData {
		public TabTitle title;
		public ModelViewInstance wiget;
		public boolean showed;

		public TabLinkData(TabTitle title, ModelViewInstance wiget,
				boolean showed) {
			this.title = title;
			this.wiget = wiget;
			this.showed = showed;
		}

	}

}
