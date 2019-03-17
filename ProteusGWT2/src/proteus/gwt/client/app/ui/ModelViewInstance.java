package proteus.gwt.client.app.ui;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.RepaintCanvasEvent;
import proteus.gwt.client.app.ui.canvas.Canvas;
import proteus.gwt.client.app.ui.canvas.CanvasItem;
import proteus.gwt.client.app.ui.canvas.CanvasModel;
import proteus.gwt.client.app.ui.canvas.CanvasModelAdapter;
import proteus.gwt.client.app.ui.canvas.CodeCanvasHandler;
import proteus.gwt.client.app.ui.canvas.ComponentCanvasItem;
import proteus.gwt.client.app.ui.canvas.DiagramCanvas;
import proteus.gwt.client.app.ui.canvas.IconCanvas;
import proteus.gwt.shared.modelica.ClassDef;
import proteus.gwt.shared.modelica.Component;
import proteus.gwt.shared.util.Utils;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class ModelViewInstance extends DockLayoutPanel {

	private CanvasHeader cHead;
	private ClassDef classDef;
	private CodeCanvas codeCanvas;

	private CodeCanvasHandler codeCanvasInstance;
	private DiagramCanvas diagramCanvas;
	private ScrollPanel diagramScrollPanel;
	private DocumentCanvas documentCanvas;

	private boolean edited = false;

	private String errorLog = "";
	private String generalLog = "";
	private IconCanvas iconCanvas;
	private ScrollPanel iconScrollPanel;

	private String infoLog = "";

	private boolean isFoucsDiagramView = false;

	private Widget lastFocus = null;

	private long modelID;

	private TreeItem treeRoot; // sam

	private String warningLog = "";

	public ModelViewInstance(ClassDef classDef, boolean edited) {
		super(Unit.PX);
		modelID = -1;
		this.edited = edited;
		this.classDef = classDef;
		createNewInstance(classDef);
	}

	private void bind() {
		cHead.imgIcon.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				focusIconCanvas();
			}
		});

		cHead.imgDiagram.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				focusDiagramCanvas();
			}
		});

		cHead.imgCode.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				focusCodeCanvas();
			}
		});

		cHead.imgDoc.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				focusDocumentCanvas();
			}
		});
	}

	public boolean canDrag() {
		return edited && isFoucsDiagramView;
	}

	private void createNewInstance(ClassDef classDef) {

		Component com = (Component) classDef;
		if (com.getCompData() != null) {
			documentCanvas = new DocumentCanvas(com.getCompData()
					.getWholeName());
		} else {
			documentCanvas = new DocumentCanvas();
		}

		CanvasModel canvasModel = new CanvasModel(edited);
		cHead = new CanvasHeader();

		diagramCanvas = new DiagramCanvas(canvasModel);
		// Icon canvas should be editable
		iconCanvas = new IconCanvas(new CanvasModel(true));

		codeCanvas = new CodeCanvas();

		// create handler for these canvas. handlers for diagramCanvas and
		// iconCanvas is embedded inside these canvas
		codeCanvasInstance = new CodeCanvasHandler(codeCanvas, this);
		initUI();
		setClassDef(classDef);//reaint #2
	}

	private void focusCodeCanvas() {
		codeCanvasInstance.refresh();
		if (lastFocus != null) {
			this.remove(lastFocus);
		}
		lastFocus = codeCanvas;
		codeCanvas.content.setHeight("99%");
		codeCanvas.content.setWidth("99%");
		this.add(codeCanvas);
		isFoucsDiagramView = false;
		cHead.imgCode.setStyleName("proteuscnvashead-images-selected ");
		cHead.imgIcon.setStyleName("proteuscnvashead-images");
		cHead.imgDiagram.setStyleName("proteuscnvashead-images");
		cHead.imgDoc.setStyleName("proteuscnvashead-images");
		// ProteusGWT.eventBus.fireEvent(new LogMessagePanelEvent(
		// Message.WARNING, "this is the WARNING test"));
		// ProteusGWT.eventBus.fireEvent(new ClearMessagePanelEvent(
		// Message.INFO));
	}

	private void focusDiagramCanvas() {
		if (lastFocus != null) {
			this.remove(lastFocus);
			ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
		}
		lastFocus = diagramScrollPanel;
		this.add(diagramScrollPanel);
		isFoucsDiagramView = true;
		cHead.imgDiagram.setStyleName("proteuscnvashead-images-selected ");
		cHead.imgIcon.setStyleName("proteuscnvashead-images");
		cHead.imgCode.setStyleName("proteuscnvashead-images");
		cHead.imgDoc.setStyleName("proteuscnvashead-images");
	}

	private void focusDocumentCanvas() {
		if (lastFocus != null) {
			this.remove(lastFocus);
		}
		lastFocus = documentCanvas;
		documentCanvas.setHeight("99%");
		documentCanvas.setWidth("99%");
		this.add(documentCanvas);
		isFoucsDiagramView = false;
		cHead.imgDoc.setStyleName("proteuscnvashead-images-selected ");
		cHead.imgIcon.setStyleName("proteuscnvashead-images");
		cHead.imgDiagram.setStyleName("proteuscnvashead-images");
		cHead.imgCode.setStyleName("proteuscnvashead-images");
	}

	private void focusIconCanvas() {
		if (lastFocus != null) {
			this.remove(lastFocus);
		}
		lastFocus = iconScrollPanel;
		// iconCanvas.repaint();
		this.add(iconScrollPanel);
		isFoucsDiagramView = false;
		cHead.imgIcon.setStyleName("proteuscnvashead-images-selected ");
		cHead.imgDiagram.setStyleName("proteuscnvashead-images");
		cHead.imgCode.setStyleName("proteuscnvashead-images");
		cHead.imgDoc.setStyleName("proteuscnvashead-images");
	}

	public Canvas getCanvas() {
		// return (Canvas) gridPanel.get
		return (Canvas) diagramCanvas;
	}

	public CanvasHeader getcHead() {
		return cHead;
	}

	public ClassDef getClassDef() {
		return classDef;
	}

	/**
	 * @return the codeCanvas
	 */
	public CodeCanvas getCodeCanvas() {
		return codeCanvas;
	}
	/**
	 * @return the codeCanvasInstance
	 */
	public CodeCanvasHandler getCodeCanvasInstance() {
		return codeCanvasInstance;
	}
	public DiagramCanvas getDiagramCanvas() {
		return diagramCanvas;
	}
	/**
	 * @return the documentCanvas
	 */
	public DocumentCanvas getDocumentCanvas() {
		return documentCanvas;
	}

	public String getErrorLog() {
		return errorLog;
	}
	public String getGeneralLog() {
		return generalLog;
	}

	public IconCanvas getIconCanvas() {
		return iconCanvas;
	}

	public String getInfoLog() {
		return infoLog;
	}

	/**
	 * @return the lastFocus
	 */
	public Widget getLastFocus() {
		return lastFocus;
	}

	/**
	 * @return the modelID
	 */
	public long getModelID() {
		return modelID;
	}

	/**
	 * @return the treeRoot
	 */
	public TreeItem getTreeRoot() {
		return treeRoot;
	}

	public String getWarningLog() {
		return warningLog;
	}

	private void initUI() {
		this.addNorth(cHead, 40);

		diagramScrollPanel = new ScrollPanel();
		diagramScrollPanel.setAlwaysShowScrollBars(true);
		diagramScrollPanel.add(diagramCanvas);

		iconScrollPanel = new ScrollPanel();
		iconScrollPanel.setAlwaysShowScrollBars(true);
		iconScrollPanel.add(iconCanvas);

		focusDiagramCanvas();

		treeRoot = new TreeItem("Model");

		diagramCanvas.getModel().addCanvasModelListener(
				new CanvasModelAdapter() {
					private void addComponentCanvasItem(TreeItem node,
							ComponentCanvasItem cci) {
						TreeItem n = new TreeItem(cci.getName());
						node.addItem(n);
//						for (ComponentCanvasItem subcci : cci.getCompItems()) {
//							TreeItem n1 = new TreeItem(subcci.getName());
//							n.addItem(n1);
//							//only one level is searching down..which will enable the loading speed
////							addComponentCanvasItem(n, subcci);
//						}
					}

					public void editHappened(CanvasModel model) {
					}

					@Override
					public void itemsAdded(CanvasItem[] items) {
						super.itemsAdded(items);
						for (final CanvasItem item : items) {
							if (item instanceof ComponentCanvasItem) {
								ComponentCanvasItem cci = (ComponentCanvasItem) item;

								addComponentCanvasItem(treeRoot, cci);
							}
						}
					}

					@Override
					public void itemsRemoved(CanvasItem[] items) {
						super.itemsRemoved(items);
						for (CanvasItem item : items) {
							if (item instanceof ComponentCanvasItem) {
								ComponentCanvasItem cci = (ComponentCanvasItem) item;
								removeComponentCanvasItem(treeRoot, cci);
							}
						}
					}

					private void removeComponentCanvasItem(TreeItem node,
							ComponentCanvasItem cci) {
						for (int i = 0; i < node.getChildCount(); i++) {
							TreeItem cItem = node.getChild(i);
							if (cItem != null
									&& cItem.getText().equals(cci.getName())) {
								cItem.remove();
							} else if (cItem != null) {
								removeComponentCanvasItem(cItem, cci);
							}
						}
					}

					@Override
					public void selectionChanged(CanvasItem[] selected) {
						super.selectionChanged(selected);
						// expand the treeRoot
						treeRoot.setState(true);
						if (selected != null) {
							for (int i = 0; i < treeRoot.getChildCount(); i++) {
								TreeItem child = treeRoot.getChild(i);
								child.setSelected(false);
								// set the selected array to true
								for (CanvasItem item : selected) {
									String obj = (String) treeRoot.getChild(i)
											.getText();
									if (obj != null && item.getName() != null
											&& item.getName().equals(obj)) {
										child.setSelected(true);
									}
								}
							}
						}
					}

				});

		bind();
	}

	public void reFresh() {
	ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
		iconCanvas.repaint();
	}

	public void setcHead(CanvasHeader cHead) {
		this.cHead = cHead;
	}

	private void setClassDef(ClassDef cd) {
		// this one could be type or other type
		diagramCanvas.setClassDef(cd);
		iconCanvas.setClassDef(cd);
		codeCanvas.setClassDef(cd);
	}

	/**
	 * @param codeCanvas
	 *            the codeCanvas to set
	 */
	public void setCodeCanvas(CodeCanvas codeCanvas) {
		this.codeCanvas = codeCanvas;
	}

	/**
	 * @param codeCanvasInstance
	 *            the codeCanvasInstance to set
	 */
	public void setCodeCanvasInstance(CodeCanvasHandler codeCanvasInstance) {
		this.codeCanvasInstance = codeCanvasInstance;
	}

	/**
	 * @param diagramCanvas
	 *            the diagramCanvas to set
	 */
	public void setDiagramCanvas(DiagramCanvas diagramCanvas) {
		this.diagramCanvas = diagramCanvas;
	}

	/**
	 * @param documentCanvas
	 *            the documentCanvas to set
	 */
	public void setDocumentCanvas(DocumentCanvas documentCanvas) {
		this.documentCanvas = documentCanvas;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public void setGeneralLog(String generalLog) {
		this.generalLog = generalLog;
	}

	public void setIconCanvas(IconCanvas iconCanvas) {
		this.iconCanvas = iconCanvas;
	}

	public void setInfoLog(String infoLog) {
		this.infoLog = infoLog;
	}

	/**
	 * @param lastFocus
	 *            the lastFocus to set
	 */
	public void setLastFocus(Widget lastFocus) {
		this.lastFocus = lastFocus;
	}

	/**
	 * @param modelID the modelID to set
	 */
	public void setModelID(long modelID) {
		this.modelID = modelID;
	}
	

	/**
	 * @param treeRoot
	 *            the treeRoot to set
	 */
	public void setTreeRoot(TreeItem treeRoot) {
		this.treeRoot = treeRoot;
	}

	public void setWarningLog(String warningLog) {
		this.warningLog = warningLog;
	}

}
