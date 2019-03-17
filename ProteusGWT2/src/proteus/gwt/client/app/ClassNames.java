package proteus.gwt.client.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import proteus.gwt.client.app.event.DragComponentEvent;
import proteus.gwt.client.app.event.LogMessagePanelEvent;
import proteus.gwt.client.app.event.ShowDiagramEvent;
import proteus.gwt.client.app.ui.ComponentsTreePanel;
import proteus.gwt.client.app.ui.MessagePanel.Message;
import proteus.gwt.client.app.ui.ProteusEditor;
import proteus.gwt.client.app.ui.util.FloatingStatus;
import proteus.gwt.client.app.util.HTMLGetter;
import proteus.gwt.client.proxy.AppContext;
import proteus.gwt.client.proxy.ProteusRemoteService;
import proteus.gwt.client.proxy.ProteusRemoteServiceAsync;
import proteus.gwt.shared.graphics.Cursor;
import proteus.gwt.shared.util.ComponentsNameMap;
import proteus.gwt.shared.util.TreeData;
import proteus.gwt.shared.util.TreeNode;
import proteus.gwt.shared.util.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ClassNames {
	private static final Logger logger = Logger.getLogger("ClassNames");
	private boolean clicked = false;
	private LinkedList<Widget> cursorChangedWidgetList = new LinkedList<Widget>();

	private final String clickColor = "#cad3e2";
	private final String componentStyle = "classNameTree-ToggleButton";
	private final String componentSelectedStyle = "classNameTreeSelected-ToggleButton";
	private Tree classNamesTree;
	private TextBox searchBox;
	private ToggleButton filterButton;
	private ComponentsNameMap componentsNameMap = null;
	private TreeData treeData = new TreeData();
	private ProteusRemoteServiceAsync proteusService = GWT
			.create(ProteusRemoteService.class);
	// private String smallIconPath = AppContext
	// .pathToServerUrl("/resources/images/classname/small/");
	// private String largeIconPath = AppContext
	// .pathToServerUrl("/resources/images/classname/large/");

	private String iconPath = AppContext
			.pathToServerUrl("/resources/images/classname/MSL3.1Image/");
	private String userGuideIconPath = iconPath
			+ "Modelica.Electrical.Digital.UsersGuide.ContactS.png";

	private ProteusEditor editor;
	HandlerManager eventBus;
	private ComponentsTreePanel componentsTreePanel;

	public ClassNames(ProteusEditor editor, HandlerManager eventBus) {
		this.editor = editor;

		this.eventBus = eventBus;
		this.componentsTreePanel = editor.componentsTreePanel;
		classNamesTree = editor.componentsTreePanel.classNamesTree;
		searchBox = editor.componentsTreePanel.searchBox;
		filterButton = editor.componentsTreePanel.filterButton;
		filterButton.setStyleName(this.componentStyle);
		ClassNamesTreeMouseHandler handler = new ClassNamesTreeMouseHandler();
		classNamesTree.addMouseMoveHandler(handler);
	}

	public boolean isClicked() {
		return clicked;
	}

	public LinkedList<Widget> getCursorChangedWidgetList() {
		return cursorChangedWidgetList;
	}

	public void initComponentsTreePanel() {

		FloatingStatus.showStatus();

		// AsyncCallback<GetClassNamesResponseDTO> callback = new
		// AsyncCallback<GetClassNamesResponseDTO>() {
		// public void onFailure(Throwable caught) {
		// FloatingStatus.hideStatus();
		// Window.alert("error" + caught.getMessage());
		// }
		//
		// public void onSuccess(GetClassNamesResponseDTO result) {
		// FloatingStatus.hideStatus();
		// if (result.isSuccess()) {
		// treeData = result.getTreeData();
		// componentsNameMap = treeData.getComponentsNameMap();
		// initUI();
		//
		// }
		// }
		// };
		// proteusService.getClassNames(callback);

		AsyncCallback<String> callback2 = new AsyncCallback<String>() {

			@Override
			public void onSuccess(String result) {
				// logger.info(result);
				// logger.info("TreeData initial start");
				treeData.parseXML(result);
				componentsNameMap = treeData.getComponentsNameMap();
				// logger.info("TreeData initial end");
				initUI();
				// logger.info("Tree UI initial end");
				FloatingStatus.hideStatus();
			}

			@Override
			public void onFailure(Throwable caught) {
				FloatingStatus.hideStatus();
			}
		};
		proteusService.getComponentNameXML(callback2);
	}

	public void setOriginal() {
		setDefaultCursor();
		clicked = false;
	}

	private void initUI() {

		// classNamesTree.setAnimationEnabled(true);


		classNamesTree.addOpenHandler(new OpenHandler<TreeItem>() {
			//
			public void onOpen(OpenEvent<TreeItem> event) {

				TreeItem target = event.getTarget();
				// logger.info("open" +
				// event.getTarget().getWidget().toString());

				// GWT.log(target.getWidget().toString());
			
				TreeNodeButton tnButton = (TreeNodeButton) target.getWidget();
				if (!tnButton.isChildrenLoaded()) {
					click2Load(target, tnButton.getTreeNode());
					tnButton.setChildrenLoaded(true);
				}
			}
		});

		// add root children
		for (TreeNode node : treeData.root.children) {
			String wholeName = treeData.getWholeName(node);

			ToggleButton tButton = new TreeNodeButton(node);
			String html = HTMLGetter.getImageText(
					"/resources/images/package.jpg", node.data);
			tButton.setHTML(html);
			tButton.setStyleName(componentStyle);
			TreeItem treeItem = classNamesTree.addItem(tButton);

			PackegeMouseHandler mouseHandle = new PackegeMouseHandler(treeItem,
					wholeName, node);
			tButton.addClickHandler(mouseHandle);
			tButton.addDoubleClickHandler(mouseHandle);

		

			// load level 1 and 2. (Modelica and Electrical)
			preloadChilren(treeItem, node);

		}

		searchBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.getCharCode() == KeyCodes.KEY_ENTER) {
					searchClassNameTree();
					filterButton.setDown(true);
					filterClassNameTree();
				}
			}
		});

		filterButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				filterClassNameTree();
			}
		});
	}

	private void preloadChilren(TreeItem parentTreeItem, TreeNode parentNode) {
		List<TreeNode> children = parentNode.children;
		// set component


		if (children.size() == 0 && !parentNode.isPackage) {

			String wholeName = treeData.getWholeName(parentNode);
			String classnameIcon = iconPath + wholeName + "S.png";

			if (wholeName.startsWith("ModelicaServices")
					|| wholeName.startsWith("ModelicaReference")) {
				classnameIcon = this.userGuideIconPath;
			}

			ToggleButton componentButton = new TreeNodeButton(parentNode);

			String html = HTMLGetter.getImageTextWithImageSize(classnameIcon,
					parentNode.data, 20, 20);
	
			if (parentNode.restrict.equals("type")) {
				html = parentNode.data;
			}
			componentButton.setHTML(html);
			componentButton.setStyleName(componentStyle);
			 ComponentMouseHandler mouseHandler = new ComponentMouseHandler(
			 wholeName);
			
	

			 componentButton.addClickHandler(mouseHandler);
			 componentButton.addDoubleClickHandler(mouseHandler);
			 componentButton.addMouseDownHandler(mouseHandler);


	
			parentTreeItem.setWidget(componentButton);
			return;
		}

		// set package
		for (TreeNode child : children) {
			String wholeName = treeData.getWholeName(child);
			String classnameIcon = iconPath + wholeName + "S.png";

			if (wholeName.startsWith("ModelicaServices")
					|| wholeName.startsWith("ModelicaReference")) {
				classnameIcon = this.userGuideIconPath;
			}
			ToggleButton packageButton = new TreeNodeButton(child);
			String html = HTMLGetter.getImageTextWithImageSize(classnameIcon,
					child.data, 20, 20);
			packageButton.setHTML(html);
			packageButton.setStyleName(componentStyle);

			TreeItem treeItem = parentTreeItem.addItem(packageButton);
			// set treeItem;
			child.treeItem = treeItem;

			PackegeMouseHandler mouseHandle = new PackegeMouseHandler(treeItem,
					wholeName, child);
			packageButton.addClickHandler(mouseHandle);
			packageButton.addDoubleClickHandler(mouseHandle);

		}
	}

	// private void addChildren(TreeItem parentTreeItem, TreeNode parentNode) {
	//
	// List<TreeNode> children = parentNode.children;
	// // set component
	// if (children.size() == 0 && !parentNode.isPackage) {
	//
	// String wholeName = treeData.getWholeName(parentNode);
	// String classnameIcon = iconPath + wholeName + "S.png";
	//
	// if (wholeName.startsWith("ModelicaServices")
	// || wholeName.startsWith("ModelicaReference")) {
	// classnameIcon = this.userGuideIconPath;
	// }
	//
	// ToggleButton componentButton = new ToggleButton();
	//
	// String html = HTMLGetter.getImageTextWithImageSize(classnameIcon,
	// parentNode.data, 20, 20);
	//
	// if (parentNode.restrict.equals("type")) {
	// html = parentNode.data;
	// }
	// componentButton.setHTML(html);
	// componentButton.setStyleName(componentStyle);
	// ComponentMouseHandler mouseHandler = new ComponentMouseHandler(
	// wholeName);
	// componentButton.addClickHandler(mouseHandler);
	// componentButton.addDoubleClickHandler(mouseHandler);
	// componentButton.addMouseDownHandler(mouseHandler);
	//
	// parentTreeItem.setWidget(componentButton);
	// return;
	// }
	//
	// // set package
	// for (TreeNode child : children) {
	// String wholeName = treeData.getWholeName(child);
	// String classnameIcon = iconPath + wholeName + "S.png";
	//
	// if (wholeName.startsWith("ModelicaServices")
	// || wholeName.startsWith("ModelicaReference")) {
	// classnameIcon = this.userGuideIconPath;
	// }
	// ToggleButton packageButton = new ToggleButton();
	// String html = HTMLGetter.getImageTextWithImageSize(classnameIcon,
	// child.data, 20, 20);
	// packageButton.setHTML(html);
	// packageButton.setStyleName(componentStyle);
	//
	// TreeItem treeItem = parentTreeItem.addItem(packageButton);
	//
	// PackegeMouseHandler mouseHandle = new PackegeMouseHandler(treeItem,
	// wholeName, child);
	// packageButton.addClickHandler(mouseHandle);
	// packageButton.addDoubleClickHandler(mouseHandle);
	//
	// addChildren(treeItem, child);
	// }
	// }

	private void click2Load(TreeItem treeItem1, TreeNode parentNode) {
		List<TreeNode> children = parentNode.children;

		for (TreeNode level1Child : children) {
			List<TreeNode> level2Children = level1Child.children;
			TreeItem parentTreeItem = level1Child.treeItem;
			preloadChilren(parentTreeItem, level1Child);

		}
	}

	class TreeItemUserObject {
		public String name;
		public String wholeName;
		public int clickTime;
		public boolean isPackage;

	}

	private void filterClassNameTree() {
		String searchedName = searchBox.getText().trim().toLowerCase();
		if (searchedName.length() < 2) {
			return;
		}

		if (!searchedName.equals(lastSearchText)) {
			searchClassNameTree();
		}

		if (filterButton.isDown()) {
			filterButton.setTitle("Show all");
			for (int i = 0; i < classNamesTree.getItemCount(); i++) {
				filterClassNameTree(classNamesTree.getItem(i));
			}
		} else {
			filterButton.setTitle("Only show the search result");
			showAllClassNameTree();
		}
	}

	private void searchClassNameTree() {
		// get the search result
		String searchedName = this.searchBox.getText().trim().toLowerCase();
		if (searchedName.length() < 2) {
			return;
		}
		lastSearchText = searchedName;
		showAllClassNameTree();
		this.filterButton.setDown(false);
		Set<String> nameSet = componentsNameMap.getNameMap().keySet();
		ArrayList<ArrayList<Integer>> resultList = new ArrayList<ArrayList<Integer>>();
		for (String name : nameSet) {
			if (name.contains(searchedName)) {
				resultList.addAll(componentsNameMap.getNameMap().get(name));
			}
		}

		// delete last Search Results
		if (lastSearchResults != null && lastSearchResults.size() > 0) {
			for (TreeItem result : lastSearchResults) {
				result.getWidget().setStyleName(componentStyle);
				result.setState(false);
				result.setSelected(false);
				lastSearchResults.remove(result);

				TreeItem parentItem = result.getParentItem();
				while (parentItem != null) {
					parentItem.setState(false);
					parentItem.setSelected(false);
					parentItem = parentItem.getParentItem();
				}
			}
		}
		// set this search results
		TreeItem currentItem;
		for (ArrayList<Integer> indexList : resultList) {
			currentItem = classNamesTree.getItem(indexList.get(0));
			currentItem.setState(true);
			currentItem.setSelected(true);
			for (int i = 1; i < indexList.size(); i++) {
				currentItem = currentItem.getChild(indexList.get(i));
				currentItem.setState(true);
				currentItem.setSelected(true);
			}
			// setTreeItemChildrenSelected(currentItem);
			ToggleButton componentButton = (ToggleButton) currentItem
					.getWidget();
			componentButton.setStyleName(componentSelectedStyle);
			lastSearchResults.add(currentItem);

		}

	}

	private void setTreeItemChildrenSelected(TreeItem item) {
		for (int i = 0; i < item.getChildCount(); i++) {
			TreeItem childItem = item.getChild(i);
			childItem.setSelected(true);
			setTreeItemChildrenSelected(childItem);
		}
	}

	private void filterClassNameTree(TreeItem item) {
		if (item.isSelected()) {
			for (int i = 0; i < item.getChildCount(); i++) {
				filterClassNameTree(item.getChild(i));
			}
		} else {
			item.setVisible(false);
			return;
		}
	}

	private void showAllClassNameTree(TreeItem item) {
		item.setVisible(true);
		for (int i = 0; i < item.getChildCount(); i++) {
			showAllClassNameTree(item.getChild(i));
		}

	}

	private void showAllClassNameTree() {
		for (int i = 0; i < classNamesTree.getItemCount(); i++) {
			showAllClassNameTree(classNamesTree.getItem(i));
		}
	}

	private LinkedList<TreeItem> lastSearchResults = new LinkedList<TreeItem>();
	private ToggleButton lastSeledtedButton = new ToggleButton();
	private String lastSearchText = "";

	class PackegeMouseHandler implements DoubleClickHandler, ClickHandler {

		TreeItem treeItem;
		String wholeName = "";
		String name = "";
		private TreeNode node;

		public PackegeMouseHandler(TreeItem treeItem, String wholeName,
				TreeNode node) {
			this.treeItem = treeItem;
			this.wholeName = wholeName;
			this.name = node.data;
			this.node = node;

		}

		@Override
		public void onClick(ClickEvent event) {
			ToggleButton selectdButton = (ToggleButton) event.getSource();
			selectdButton.setStyleName(componentSelectedStyle);
			lastSeledtedButton.setStyleName(componentStyle);

			lastSeledtedButton = selectdButton;
		}

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			treeItem.setState(!treeItem.getState());
		}
	}

	class TreeNodeButton extends ToggleButton {

		boolean childrenLoaded = false;
		TreeNode treeNode;

		public TreeNodeButton(TreeNode node) {
			treeNode = node;
		}

		/**
		 * @return the treeNode
		 */
		public TreeNode getTreeNode() {
			return treeNode;
		}

		/**
		 * @return the childrenLoaded
		 */
		public boolean isChildrenLoaded() {
			return childrenLoaded;
		}

		/**
		 * @param childrenLoaded
		 *            the childrenLoaded to set
		 */
		public void setChildrenLoaded(boolean childrenLoaded) {
			this.childrenLoaded = childrenLoaded;
		}

		/**
		 * @param treeNode
		 *            the treeNode to set
		 */
		public void setTreeNode(TreeNode treeNode) {
			this.treeNode = treeNode;
		}

	}

	class ComponentMouseHandler implements DoubleClickHandler, ClickHandler,
			MouseDownHandler, MouseUpHandler, MouseMoveHandler {
		String wholeName = "";

		public ComponentMouseHandler(String wholeName) {
			this.wholeName = wholeName;
		}

		@Override
		public void onClick(ClickEvent event) {
	
			ToggleButton selectdButton = (ToggleButton) event.getSource();
			selectdButton.setStyleName(componentSelectedStyle);
			lastSeledtedButton.setStyleName(componentStyle);
			lastSeledtedButton = selectdButton;
		}

		@Override
		public void onDoubleClick(DoubleClickEvent event) {
			
			eventBus.fireEvent(new ShowDiagramEvent(wholeName));
		}

		@Override
		public void onMouseDown(MouseDownEvent event) {
			if (Utils.DEBUGUI2){
				eventBus.fireEvent(new LogMessagePanelEvent(Message.ERROR, "on mouse down"));
			}
			
			if (editor.tabCanvas.currentMVI.canDrag()) {
				clicked = true;
				onMouseDrag(event);
			}
		}

		private void onMouseDrag(MouseDownEvent event) {
			if (Utils.DEBUGUI2){
				eventBus.fireEvent(new LogMessagePanelEvent(Message.ERROR, "on mouse drag"));
			}
			DOM.setStyleAttribute(
					((ToggleButton) event.getSource()).getElement(), "cursor",
					"default");
			String cursorUrl = iconPath + wholeName + "I.png";
			setDragCursor(cursorUrl);
		
			
			eventBus.fireEvent(new DragComponentEvent(wholeName));
		}

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			// TODO Auto-generated method stub
			if (Utils.DEBUGUI2){
				eventBus.fireEvent(new LogMessagePanelEvent(Message.ERROR, "on mouse move"));
			}
		}

		@Override
		public void onMouseUp(MouseUpEvent event) {
		
			if (Utils.DEBUGUI2){
				eventBus.fireEvent(new LogMessagePanelEvent(Message.ERROR, "on mouse up"));
			}
		}
	}

	class ClassNamesTreeMouseHandler implements MouseMoveHandler {

		@Override
		public void onMouseMove(MouseMoveEvent event) {
			setDefaultCursor();
			clicked = false;
		}
	}

	private HashMap<Widget, String> cursorMap = new HashMap<Widget, String>();
	private Cursor cursor;

	private void setDefaultCursor() {
		DOM.setStyleAttribute(classNamesTree.getElement(), "cursor", "default");

		for (Widget widget : cursorChangedWidgetList) {
			DOM.setStyleAttribute(widget.getElement(), "cursor", "default");
		}
	}

	private void setDragCursor(String cursorUrl) {

		DOM.setStyleAttribute(classNamesTree.getElement(), "cursor", "url("
				+ cursorUrl + "),default");

		for (Widget widget : cursorChangedWidgetList) {
			cursorMap.put(widget,
					DOM.getStyleAttribute(widget.getElement(), "cursor"));
			DOM.setStyleAttribute(widget.getElement(), "cursor", "url("
					+ cursorUrl + "),default");
		}

	}

}
