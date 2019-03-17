package proteus.gwt.client.app;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import proteus.gwt.client.app.event.ClearMessagePanelEvent;
import proteus.gwt.client.app.event.ClearMessagePanelEventHandler;
import proteus.gwt.client.app.event.DeleteComponentsEvent;
import proteus.gwt.client.app.event.DeleteComponentsEventHandler;
import proteus.gwt.client.app.event.DragComponentEvent;
import proteus.gwt.client.app.event.DragComponentEventHandler;
import proteus.gwt.client.app.event.DropComponentEvent;
import proteus.gwt.client.app.event.DropComponentEventHandler;
import proteus.gwt.client.app.event.ExportEvent;
import proteus.gwt.client.app.event.ExportEventHandler;
import proteus.gwt.client.app.event.FlipHorizontalEvent;
import proteus.gwt.client.app.event.FlipHorizontalEventHandler;
import proteus.gwt.client.app.event.FlipVerticalEvent;
import proteus.gwt.client.app.event.FlipVerticalEventHandler;
import proteus.gwt.client.app.event.LogCodeCanvasEvent;
import proteus.gwt.client.app.event.LogCodeCanvasEventHandler;
import proteus.gwt.client.app.event.LogMessagePanelEvent;
import proteus.gwt.client.app.event.LogMessagePanelEventHandler;
import proteus.gwt.client.app.event.LoginEvent;
import proteus.gwt.client.app.event.LoginEventHandler;
import proteus.gwt.client.app.event.LogoutEvent;
import proteus.gwt.client.app.event.LogoutEventHandler;
import proteus.gwt.client.app.event.MVIChangeEvent;
import proteus.gwt.client.app.event.MVIChangeEventHandler;
import proteus.gwt.client.app.event.NewModuleEvent;
import proteus.gwt.client.app.event.NewModuleEventHandler;
import proteus.gwt.client.app.event.OpenComponentEvent;
import proteus.gwt.client.app.event.OpenComponentEventHandler;
import proteus.gwt.client.app.event.RedoEvent;
import proteus.gwt.client.app.event.RedoEventHandler;
import proteus.gwt.client.app.event.RefreshCodeCanvasEvent;
import proteus.gwt.client.app.event.RefreshCodeCanvasEventHandler;
import proteus.gwt.client.app.event.RepaintCanvasEvent;
import proteus.gwt.client.app.event.RepaintCanvasEventHandler;
import proteus.gwt.client.app.event.RotateAntiClockwiseEvent;
import proteus.gwt.client.app.event.RotateAntiClockwiseEventHandler;
import proteus.gwt.client.app.event.RotateClockwiseEvent;
import proteus.gwt.client.app.event.RotateClockwiseEventHandler;
import proteus.gwt.client.app.event.SaveEvent;
import proteus.gwt.client.app.event.SaveEventHandler;
import proteus.gwt.client.app.event.ShowDemoEvent;
import proteus.gwt.client.app.event.ShowDemoEventHandler;
import proteus.gwt.client.app.event.ShowDiagramEvent;
import proteus.gwt.client.app.event.ShowDiagramEventHandler;
import proteus.gwt.client.app.event.UndoEvent;
import proteus.gwt.client.app.event.UndoEventHandler;
import proteus.gwt.client.app.event.ZoomEvent;
import proteus.gwt.client.app.event.ZoomEventHandler;
import proteus.gwt.client.app.event.ZoomInEvent;
import proteus.gwt.client.app.event.ZoomInEventHandler;
import proteus.gwt.client.app.event.ZoomOutEvent;
import proteus.gwt.client.app.event.ZoomOutEventHandler;
import proteus.gwt.client.app.event.ZoomResetEvent;
import proteus.gwt.client.app.event.ZoomResetEventHandler;
import proteus.gwt.client.app.event.simulation.SimulateEvent;
import proteus.gwt.client.app.event.simulation.SimulateFinishedEvent;
import proteus.gwt.client.app.event.simulation.SimulateResultDisplayEvent;
import proteus.gwt.client.app.event.simulation.SimulateStopRequestedEvent;
import proteus.gwt.client.app.event.simulation.SimulateEvent.SimulateEventHandler;
import proteus.gwt.client.app.event.simulation.SimulateFinishedEvent.SimulateFinishedEventHandler;
import proteus.gwt.client.app.event.simulation.SimulateResultDisplayEvent.SimulateResultDisplayEventHandler;
import proteus.gwt.client.app.event.simulation.SimulateStopRequestedEvent.SimulateStopRequestedEventHandler;
import proteus.gwt.client.app.jsonObject.Users;
import proteus.gwt.client.app.model.simulation.SimulationResult;
import proteus.gwt.client.app.model.simulation.SimulationResultPool;
import proteus.gwt.client.app.ui.MessagePanel;
import proteus.gwt.client.app.ui.ModelViewInstance;
import proteus.gwt.client.app.ui.NewComponentDialog;
import proteus.gwt.client.app.ui.ProteusEditor;
import proteus.gwt.client.app.ui.TabCanvas;
import proteus.gwt.client.app.ui.UploadMoFile;
import proteus.gwt.client.app.ui.MessagePanel.Message;
import proteus.gwt.client.app.ui.canvas.CanvasItem;
import proteus.gwt.client.app.ui.canvas.DiagramCanvas;
import proteus.gwt.client.app.ui.canvas.edit.FlipComponentEdit;
import proteus.gwt.client.app.ui.canvas.edit.RemoveComponentEdit;
import proteus.gwt.client.app.ui.canvas.edit.RotateComponentEdit;
import proteus.gwt.client.app.ui.common.BalloonTooltip;
import proteus.gwt.client.app.ui.common.ConfirmDialog;
import proteus.gwt.client.app.ui.common.MessageDialog;
import proteus.gwt.client.app.ui.menu.ProteusMainMenu;
import proteus.gwt.client.app.ui.simulation.SimulationResultDisplayOptionsDialog;
import proteus.gwt.client.app.ui.util.Constant;
import proteus.gwt.client.app.ui.util.FloatingStatus;
import proteus.gwt.client.app.ui.util.MiscUtils;
import proteus.gwt.client.app.util.Domain2DTO;
import proteus.gwt.client.proxy.AppContext;
import proteus.gwt.client.proxy.ProteusRemoteServiceAsync;
import proteus.gwt.shared.app.ui.NewClassDefDialog;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusResponseDTO;
import proteus.gwt.shared.datatransferobjects.GetModelResponseDTO;
import proteus.gwt.shared.datatransferobjects.PlotDataDTO;
import proteus.gwt.shared.datatransferobjects.SimulationResultDTO;
import proteus.gwt.shared.graphics.Point;
import proteus.gwt.shared.modelica.Component;
import proteus.gwt.shared.types.Constants;
import proteus.gwt.shared.util.ExportType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

import emu.java.swing.undo.CannotRedoException;
import emu.java.swing.undo.CannotUndoException;
import proteus.gwt.shared.util.Utils;

//This file corresponds to the Proteus.java file in the desktop version
public class AppController implements ValueChangeHandler<String> {

	private Users user;

	public static boolean JSON_FLAG = true;

	public static final Logger logger = Logger.getLogger("AppController");

	private Tree classNamesTree;
	private Tree componentsTree;
	private TextBox searchBox;
	private ModelViewInstance currentmvi;
	private ProteusRemoteServiceAsync rpcService;
	private HandlerManager eventBus;
	private MessagePanel msgPanel;
	private TabCanvas tabCanvas;
	private ClassNames classNames;

	private ProteusEditor editor;
	private boolean simulationStopped;

	private String uploadUrl = "";
	ComponentDTO dragResult = null;
	private ProteusMainMenu proteusmainmenu;
	// duration remembering login. 1 day in this example.
	private final int DURATION = 1000 * 60 * 60 * 24 * 14;
	private final String COOKIENAME = "userStr";

	public AppController(ProteusEditor editor, ClassNames classNames,
			ProteusRemoteServiceAsync proteusService, HandlerManager eventBus) {

		classNamesTree = editor.componentsTreePanel.classNamesTree;
		componentsTree = editor.componentsTreePanel.canvasComponentsTree;
		searchBox = editor.componentsTreePanel.searchBox;
		currentmvi = editor.tabCanvas.currentMVI;
		proteusmainmenu = editor.proteusMainMenu;
		this.editor = editor;

		rpcService = proteusService;
		msgPanel = editor.infoMsgPanel;
		this.eventBus = eventBus;
		this.tabCanvas = editor.tabCanvas;
		this.classNames = classNames;
		bind();

		componentsTree.addItem(currentmvi.getTreeRoot());
		componentsTree.addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				TreeItem item = event.getSelectedItem();
				for (CanvasItem citem : currentmvi.getCanvas().getCanvasItems()) {
					if (citem.getName().equals(item.getText())) {
						currentmvi.getCanvas().setSelectedItems(
								new CanvasItem[] { citem });
						break;
					}
				}
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		});

		String value = com.google.gwt.user.client.Window.Location
				.getParameter("id");
		String name = Location.getParameter("n");
		String pass = Location.getParameter("m");
		if (value != null) {
			long id = Long.valueOf(value);
			openModel(id);
		}
		// login the user and password
		boolean verify = true;
		if (name != null && pass != null) {
			if (verify) {
				StringBuilder sb = new StringBuilder();
				sb.append(Constants.HEADER_USER + name + "\n");
				sb.append(Constants.HEADER_PASS + pass + "\n");
				rpcService.checkLogin(sb.toString(),
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {

							}

							@Override
							public void onSuccess(String result) {
								// Window.alert(result);
								Users user = parseUser(result);
								setCookie(result);
								setProfile(user);
							}
						});
			}
		}
	}

	private void bind() {
		// check cookie
		String userCookie = Cookies.getCookie(COOKIENAME);

		if (userCookie != null) {
			Users c_user = parseUser(userCookie);
			setCookie(userCookie);
			setProfile(c_user);
		}
		bindMainMenu();
		// add value change handler
		History.addValueChangeHandler(this);

		eventBus.addHandler(ShowDemoEvent.TYPE, new ShowDemoEventHandler() {

			@Override
			public void onShowDemo(ShowDemoEvent event) {
				FloatingStatus.showStatus();
				final String name = event.getName();
				AsyncCallback<ComponentDTO> callback = new AsyncCallback<ComponentDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("not success");
						FloatingStatus.hideStatus();
					}

					@Override
					public void onSuccess(ComponentDTO result) {
						// boolean false will set the canvas non
						// editable
						Component comp = newComponentInstance("model");
						comp.parseModel(result);
						ModelViewInstance canvas = new ModelViewInstance(comp,
								true);
						canvas.reFresh();
						String shortName = name
								.substring(name.lastIndexOf(".") + 1);
						tabCanvas.newTab(shortName, canvas);
						FloatingStatus.hideStatus();
					}

				};

				AsyncCallback<String> jsonCallback = new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(String result) {
						proteus.gwt.client.app.jsonObject.ComponentDTO component = parseComponentDTO(result);

						ComponentDTO componentDTO = Domain2DTO
								.convert(component);

						Component comp = newComponentInstance("model");
						// comp.parseModel(componentDTO);
						comp.setCompData(componentDTO);
						comp.parseDiagram(componentDTO);
						ModelViewInstance canvas = new ModelViewInstance(comp,
								true);
						canvas.reFresh();
						String shortName = name
								.substring(name.lastIndexOf(".") + 1);
						tabCanvas.newTab(shortName, canvas);
						FloatingStatus.hideStatus();
					}
				};
				if (JSON_FLAG) {
					rpcService.getComponentJSON(name, jsonCallback);
				} else {
					rpcService.getDemo(name, callback);
				}
			}
		});
		eventBus.addHandler(LogCodeCanvasEvent.TYPE,
				new LogCodeCanvasEventHandler() {

					@Override
					public void onLogCodeCanvas(LogCodeCanvasEvent event) {

					}
				});

		eventBus.addHandler(RepaintCanvasEvent.TYPE,
				new RepaintCanvasEventHandler() {

					@Override
					public void onRepaint(RepaintCanvasEvent event) {
						tabCanvas.getCurrentMVI().getDiagramCanvas().repaint();
					}
				});
		eventBus.addHandler(LogMessagePanelEvent.TYPE,
				new LogMessagePanelEventHandler() {

					@Override
					public void onLogMessagePanel(LogMessagePanelEvent event) {
						Message message = event.getMessage();
						String html = event.getHtml();
						switch (message) {
						case GENERAL:
							String generalLog = msgPanel.logGeneral(html);
							currentmvi.setGeneralLog(generalLog);
							break;
						case INFO:
							String infoLog = msgPanel.logInfo(html);
							currentmvi.setInfoLog(infoLog);
							break;
						case ERROR:
							String errorLog = msgPanel.logError(html);
							currentmvi.setErrorLog(errorLog);
							break;
						case WARNING:
							String warningLog = msgPanel.logWarning(html);
							currentmvi.setWarningLog(warningLog);
							break;
						}
					}
				});
		eventBus.addHandler(ClearMessagePanelEvent.TYPE,
				new ClearMessagePanelEventHandler() {
					@Override
					public void onClearMessagePanel(ClearMessagePanelEvent event) {
						Message message = event.getMessage();

						switch (message) {
						case GENERAL:
							msgPanel.clearGeneral();
							break;
						case INFO:
							msgPanel.clearInfo();
							break;
						case ERROR:
							msgPanel.clearError();
							break;
						case WARNING:
							msgPanel.clearWarning();
							break;
						case ALL:
							msgPanel.clearAll();
							break;
						}
					}
				});

		eventBus.addHandler(MVIChangeEvent.TYPE, new MVIChangeEventHandler() {

			@Override
			public void onChange(MVIChangeEvent event) {

				currentmvi = event.getInstance();
				msgPanel.resetAll(currentmvi.getGeneralLog(), currentmvi
						.getInfoLog(), currentmvi.getWarningLog(), currentmvi
						.getErrorLog());

				// remove all other tree
				componentsTree.removeItems();
				componentsTree.addItem(currentmvi.getTreeRoot());
				// msgPanel
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		});
		eventBus.addHandler(NewModuleEvent.TYPE, new NewModuleEventHandler() {

			@Override
			public void onNewModule(NewModuleEvent event) {
				final String type = event.getType();
				final NewComponentDialog moduleDialog = new NewComponentDialog();
				moduleDialog.box.center();
				moduleDialog.btCancel.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						moduleDialog.box.hide();
					}
				});
				moduleDialog.btOk.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent arg0) {
						String name = moduleDialog.tbName.getText();
						String desc = moduleDialog.tbDescription.getText();
						boolean final_ = moduleDialog.final_.getValue();
						boolean encapsulated = moduleDialog.encapsulated
								.getValue();
						boolean partial = moduleDialog.partial.getValue();

						Component comp = newComponentInstance(type);

						// set Value in component
						comp.name.setValue(name);
						comp.description.setValue(desc);
						comp.final_ = final_;
						comp.encapsulated = encapsulated;
						comp.partial = partial;

						ModelViewInstance canvas = new ModelViewInstance(comp,
								true);

						classNames.getCursorChangedWidgetList().add(
								canvas.getDiagramCanvas());
						tabCanvas.newTab(moduleDialog.tbName.getText(), canvas);
						moduleDialog.box.hide();
					}
				});
			}

		});

		eventBus.addHandler(SaveEvent.TYPE, new SaveEventHandler() {

			@Override
			public void onSave(SaveEvent event) {
				String modelCode = tabCanvas.getCurrentMVI().getCodeCanvas()
						.getContentText();
				if (user != null && modelCode != null) {
					StringBuilder sb = new StringBuilder();

					sb.append(Constants.HEADER_MID
							+ tabCanvas.getCurrentMVI().getModelID() + "\n");
					sb.append(Constants.HEADER_UID + user.getUid() + "\n");
					sb.append(tabCanvas.getCurrentMVI().getCodeCanvas()
							.getContentText());

					AsyncCallback<String> saveMOcallback = new AsyncCallback<String>() {
						@Override
						public void onFailure(Throwable caught) {
							FloatingStatus.showStatus("Model failed saving"
									+ caught.getMessage());

						}

						@Override
						public void onSuccess(String result) {
							if (result.equals(Constants.NOT_UR_MODEL_ERROR)) {
								boolean save = Window
										.confirm("Would you like to export this model to your local computer?");
								if (save) {
									ExportProteusDTO exportDTO = new ExportProteusDTO();
									exportDTO.setType(ExportType.MO);
									eventBus.fireEvent(new ExportEvent(
											exportDTO));
								}
							} else {
								FloatingStatus.showStatus(result);
							}
							Timer t = new Timer() {
								public void run() {
									FloatingStatus.hideStatus();
								}
							};
							// Schedule the timer to run once in 5 seconds.
							t.schedule(3000);
						}
					};
					rpcService.saveMOFile(sb.toString(), saveMOcallback);
				} else {
					Window.alert("Please login first!");
				}
			}
		});

		eventBus.addHandler(UndoEvent.TYPE, new UndoEventHandler() {
			@Override
			public void onUndo(UndoEvent event) {
				logger.log(Level.INFO, "undo");
				ModelViewInstance mvi = tabCanvas.getCurrentMVI();
				if (mvi != null) {
					try {
						if (mvi.getDiagramCanvas() != null) {
							mvi.getDiagramCanvas().getUndoManager().undo();
						}
					} catch (CannotUndoException e) {
						logger.log(Level.INFO, "["
								+ mvi.getClassDef().getName().getValue()
								+ "] no more edit to redo");
					}
				}
			}
		});

		eventBus.addHandler(RedoEvent.TYPE, new RedoEventHandler() {
			@Override
			public void onRedo(RedoEvent event) {
				logger.log(Level.INFO, "redo");
				ModelViewInstance mvi = tabCanvas.getCurrentMVI();
				if (mvi != null) {
					try {
						if (mvi.getDiagramCanvas() != null) {
							mvi.getDiagramCanvas().getUndoManager().redo();
						}
					} catch (CannotRedoException e) {
						logger.log(Level.INFO, "["
								+ mvi.getClassDef().getName().getValue()
								+ "] no more edit to undo");
					}
				}
			}
		});

		eventBus.addHandler(ZoomInEvent.TYPE, new ZoomInEventHandler() {

			@Override
			public void onZoomIn(ZoomInEvent event) {
				// logger.info(tabCanvas.getCurrentMVI().getCodeCanvas()
				// .getContentText());
				DiagramCanvas dc = currentmvi.getDiagramCanvas();
				dc.zoomIn();
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		});

		eventBus.addHandler(ZoomOutEvent.TYPE, new ZoomOutEventHandler() {

			@Override
			public void onZoomOut(ZoomOutEvent event) {
				DiagramCanvas dc = currentmvi.getDiagramCanvas();
				dc.zoomOut();
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		});

		eventBus.addHandler(ZoomResetEvent.TYPE, new ZoomResetEventHandler() {

			@Override
			public void onZoomReset(ZoomResetEvent event) {
				DiagramCanvas dc = currentmvi.getDiagramCanvas();
				dc.zoomReset();
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		});

		eventBus.addHandler(RotateClockwiseEvent.TYPE,
				new RotateClockwiseEventHandler() {

					@Override
					public void onRotateClockwise(RotateClockwiseEvent event) {
						DiagramCanvas dc = currentmvi.getDiagramCanvas();
						CanvasItem[] selItems = dc.getSelectedItems();

						dc.getUndoSupport().postEdit(
								new RotateComponentEdit(dc, selItems, true));
					}
				});

		eventBus.addHandler(RotateAntiClockwiseEvent.TYPE,
				new RotateAntiClockwiseEventHandler() {

					@Override
					public void onRotateAntiClockwise(
							RotateAntiClockwiseEvent event) {
						DiagramCanvas dc = currentmvi.getDiagramCanvas();
						CanvasItem[] selItems = dc.getSelectedItems();

						dc.getUndoSupport().postEdit(
								new RotateComponentEdit(dc, selItems, false));
						// dc.repaint();
					}
				});

		eventBus.addHandler(FlipHorizontalEvent.TYPE,
				new FlipHorizontalEventHandler() {

					@Override
					public void onFlipHorizontal(FlipHorizontalEvent event) {
						DiagramCanvas dc = currentmvi.getDiagramCanvas();
						CanvasItem[] selItems = dc.getSelectedItems();

						dc.getUndoSupport().postEdit(
								new FlipComponentEdit(dc, selItems, true));
					}
				});

		eventBus.addHandler(FlipVerticalEvent.TYPE,

		new FlipVerticalEventHandler() {

			@Override
			public void onFlipVertical(FlipVerticalEvent event) {
				DiagramCanvas dc = currentmvi.getDiagramCanvas();
				CanvasItem[] selItems = dc.getSelectedItems();

				dc.getUndoSupport().postEdit(
						new FlipComponentEdit(dc, selItems, false));
			}
		});

		eventBus.addHandler(ZoomEvent.TYPE, new ZoomEventHandler() {

			@Override
			public void onZoom(ZoomEvent event) {
				DiagramCanvas dc = currentmvi.getDiagramCanvas();
				// System.out.println("zoom");
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		});

		eventBus.addHandler(SimulateEvent.TYPE, new SimulateEventHandler() {
			@Override
			public void onSimulate(SimulateEvent event) {
				
				final AsyncCallback<String> omcInfoCall = new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {}

					@Override
					public void onSuccess(String result) {
						if (result != null)
							eventBus.fireEvent(new LogMessagePanelEvent(
									MessagePanel.Message.GENERAL, result));
						if (!simulationStopped) {
							rpcService.fetchOMCInfo(this);
						} else {
							eventBus
									.fireEvent(new LogMessagePanelEvent(
											MessagePanel.Message.INFO,
											"end simulation"));
						}
					}
				};
			
				
				AsyncCallback<SimulationResultDTO> simulationResultCall = new AsyncCallback<SimulationResultDTO>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("failed");
						
						if (!simulationStopped) {
							BalloonTooltip tooltip = BalloonTooltip
									.getGlobalTooltip();
							tooltip.setText("Simulation Failed").show(
									editor.editorToolbar.pbSimulate);
							simulationStopped = true;
							editor.showStopSimulation();
						}
						logger.log(Level.WARNING, "Simulation Failed");
						eventBus.fireEvent(new LogMessagePanelEvent(
								Message.WARNING, "Simulation Failed"+caught.toString()));
					}

					@Override
					public void onSuccess(SimulationResultDTO result) {
						List<PlotDataDTO> pds = result.getPds();
						if (!simulationStopped) {
							SimulationResult res = new SimulationResult("data",
									pds);
							eventBus.fireEvent(new LogMessagePanelEvent(
									Message.INFO, "end Simulate...."));
							// store this result for future plotting
							SimulationResultPool.putSimulateResult(res);

							eventBus.fireEvent(new SimulateFinishedEvent());
							simulationStopped = true;
						}
					}
				};
				

				AsyncCallback<SimulationResultDTO> mslSimResultCall = new AsyncCallback<SimulationResultDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						logger.log(Level.WARNING, "Simulation Failed");
						eventBus.fireEvent(new LogMessagePanelEvent(
								Message.WARNING, "Simulation Failed"));
						if (!simulationStopped) {
							BalloonTooltip tooltip = BalloonTooltip
									.getGlobalTooltip();
							tooltip.setText("Simulation Failed").show(
									editor.editorToolbar.pbSimulate);

							simulationStopped = true;
						}
					}

					@Override
					public void onSuccess(SimulationResultDTO result) {
						List<PlotDataDTO> pds = result.getPds();
						if (!simulationStopped) {
							SimulationResult res = new SimulationResult("data",
									pds);
							eventBus.fireEvent(new LogMessagePanelEvent(
									Message.INFO, "end Simulate...."));
							// store this result for future plotting
							SimulationResultPool.putSimulateResult(res);

							eventBus.fireEvent(new SimulateFinishedEvent());
							simulationStopped = true;
						}
					}
				};


				BalloonTooltip tooltip = BalloonTooltip.getGlobalTooltip();
				tooltip
						.setText(
								"Simulation started<br/>Click again to stop it")
						.show(editor.editorToolbar.pbSimulate);

				editor.showStartSimulation();

				simulationStopped = false;

				double startTime = event.getStartTime();
				double stopTime = event.getStopTime();
				int numOfInterval = event.getNumOfInterval();

				eventBus.fireEvent(new LogMessagePanelEvent(Message.INFO,
						"Start Simulate<br/>start time =" + startTime
								+ " , stop time =" + stopTime));

				StringBuilder header = new StringBuilder();
				Component comp = (Component) (tabCanvas.getCurrentMVI()
						.getDiagramCanvas().getClassDef());

				String msg = null, model = null;
				if (comp.isMSLmodel()) {
					model = comp.getCompData().getWholeName();
					header.append(Constants.HEADER_PREFIX_MODEL + model + "\n");
					header.append(Constants.HEADER_PREFIX_START_TIME
							+ startTime + "\n");
					header.append(Constants.HEADER_PREFIX_STOP_TIME + stopTime
							+ "\n");
					header.append(Constants.HEADER_PREFIX_INTERVALS
							+ numOfInterval + "\n");
					msg = header.toString()
					// ;
							+ tabCanvas.getCurrentMVI().getCodeCanvas()
									.getContentText();
					 rpcService.mslSim(msg, mslSimResultCall);
				} else {
					model = comp.name.getValue();
					header.append(Constants.HEADER_PREFIX_MODEL + model + "\n");
					header.append(Constants.HEADER_PREFIX_START_TIME
							+ startTime + "\n");
					header.append(Constants.HEADER_PREFIX_STOP_TIME + stopTime
							+ "\n");
					header.append(Constants.HEADER_PREFIX_INTERVALS
							+ numOfInterval + "\n");
					msg = header.toString()
							+ tabCanvas.getCurrentMVI().getCodeCanvas()
									.getContentText();
					rpcService.Simulate(msg, simulationResultCall);
				}
				// get compiler message
				rpcService.fetchOMCInfo(omcInfoCall);
			}
		});

		eventBus.addHandler(SimulateStopRequestedEvent.TYPE,
				new SimulateStopRequestedEventHandler() {
					@Override
					public void onSimulateStopRequested(
							final SimulateStopRequestedEvent event) {
						ConfirmDialog simulationStopConfirmDialog = new ConfirmDialog(
								"Confirm stop?",
								"Are you sure you want to stop the simulation?") {
							@Override
							protected void onConfirm() {
								simulationStopped = true;
								BalloonTooltip tooltip = BalloonTooltip
										.getGlobalTooltip();
								tooltip.setText("Simulation stopped").show(
										editor.editorToolbar.pbSimulate);
								editor.showStopSimulation();
							}

							@Override
							protected void onCancel() {
								// user cancel, nothing need to be done here
							}
						};
						simulationStopConfirmDialog.center();
					}
				});

		eventBus.addHandler(SimulateFinishedEvent.TYPE,
				new SimulateFinishedEventHandler() {
					@Override
					public void onSimulateFinished(SimulateFinishedEvent event) {
						BalloonTooltip tooltip = BalloonTooltip
								.getGlobalTooltip();
						tooltip
								.setText(
										"Simulation Finished<br/>Result charts available")
								.show(editor.editorToolbar.pbSimulateResult);

						editor.editorToolbar.pbSimulate
								.removeStyleName(editor.editorToolbar.simulateBtnStyle
										.running());
						editor.editorToolbar.pbSimulate
								.addStyleName(editor.editorToolbar.simulateBtnStyle
										.stopped());
						editor.editorToolbar.pbSimulate
								.setTitle("run simulation");
					}
				});

		eventBus.addHandler(RefreshCodeCanvasEvent.TYPE,
				new RefreshCodeCanvasEventHandler() {

					@Override
					public void onRefreshCode(RefreshCodeCanvasEvent event) {
						tabCanvas.getCurrentMVI().getCodeCanvasInstance()
								.textChanged();
					}
				});
		eventBus.addHandler(SimulateResultDisplayEvent.TYPE,
				new SimulateResultDisplayEventHandler() {
					@Override
					public void onSimulateResultDisplay(
							SimulateResultDisplayEvent event) {
						if (SimulationResultPool.getLastSimulateResult() != null) {
							SimulationResult result = SimulationResultPool
									.getLastSimulateResult();

							SimulationResultDisplayOptionsDialog dialog = new SimulationResultDisplayOptionsDialog(
									result);

							dialog.center();
						} else {
							MessageDialog dialog = new MessageDialog("Oops!",
									"There is no simulation result at the moment");
							dialog.center();
						}
					}
				});

		eventBus.addHandler(OpenComponentEvent.TYPE,
				new OpenComponentEventHandler() {

					@Override
					public void onOpenComponent(OpenComponentEvent event) {
						Component comp = event.getComp();

						comp.setCompData(comp.getCompData());
						comp.parseDiagram(comp.getCompData());

						ModelViewInstance mvi = new ModelViewInstance(comp,
								false);
						String name = comp.getName().getValue();
						String shortName = name
								.substring(name.lastIndexOf(".") + 1);
						tabCanvas.newTab(shortName, mvi);
						mvi.reFresh();
					}
				});
		eventBus.addHandler(ShowDiagramEvent.TYPE,
				new ShowDiagramEventHandler() {

					@Override
					public void onShowDiaram(ShowDiagramEvent event) {
						FloatingStatus.showStatus();
						final String name = event.getName();

						final StringBuffer debugStr = new StringBuffer("Start"
								+ MiscUtils.getTime());

						if (!JSON_FLAG) {
							AsyncCallback<ComponentDTO> callback = new AsyncCallback<ComponentDTO>() {

								@Override
								public void onFailure(Throwable caught) {
									FloatingStatus.hideStatus();
								}

								@Override
								public void onSuccess(ComponentDTO result) {
									// boolean false will set the canvas non
									// editable
									debugStr.append("After get  "
											+ MiscUtils.getTime());

									if (result == null) {
										FloatingStatus
												.showStatus("Empty Result(Only for Test)");
										return;
									}
									Component comp = newComponentInstance("model");
									comp.setCompData(result);
									comp.parseDiagram(result);

									ModelViewInstance canvas = new ModelViewInstance(
											comp, false);
									debugStr.append("intermediate"
											+ MiscUtils.getTime());
									String shortName = name.substring(name
											.lastIndexOf(".") + 1);
									tabCanvas.newTab(shortName, canvas);
									FloatingStatus.hideStatus();

									debugStr.append("End "
											+ MiscUtils.getTime());
									if(Utils.DEBUGTIME)
									eventBus
											.fireEvent(new LogMessagePanelEvent(
													Message.WARNING, debugStr
															.toString()));
								}
							};
							rpcService.getComponent(name, callback);
						} else if (JSON_FLAG) {

							AsyncCallback<String> callback = new AsyncCallback<String>() {
								@Override
								public void onFailure(Throwable caught) {
									FloatingStatus.hideStatus();
								}

								@Override
								public void onSuccess(String result) {
									debugStr.append("After get file "
											+ MiscUtils.getTime());
									proteus.gwt.client.app.jsonObject.ComponentDTO component = parseComponentDTO(result);

									debugStr.append("After parse file "
											+ MiscUtils.getTime());

									ComponentDTO componentDTO = Domain2DTO
											.convert(component);

									debugStr.append("After convert "
											+ MiscUtils.getTime());
									Component comp = newComponentInstance("model");
									comp.setCompData(componentDTO);
									comp.parseDiagram(componentDTO);

									ModelViewInstance mvi = new ModelViewInstance(
											comp, false);
									debugStr.append("intermedian"
											+ MiscUtils.getTime());
									String shortName = name.substring(name
											.lastIndexOf(".") + 1);
									tabCanvas.newTab(shortName, mvi);

									FloatingStatus.hideStatus();

									debugStr.append("End "
											+ MiscUtils.getTime());

									if(Utils.DEBUGTIME)
									eventBus
											.fireEvent(new LogMessagePanelEvent(
													Message.WARNING, debugStr
															.toString()));
								}
							};
							
							
							rpcService.getComponentJSON(name, callback);
			
						}
					}
				});
		eventBus.addHandler(DragComponentEvent.TYPE,
				new DragComponentEventHandler() {

					@Override
					public void onDragComponent(DragComponentEvent event) {
						if (!JSON_FLAG) {
							AsyncCallback<ComponentDTO> callback = new AsyncCallback<ComponentDTO>() {

								@Override
								public void onFailure(Throwable caught) {
								}

								@Override
								public void onSuccess(ComponentDTO result) {
									dragResult = result;
								}
							};
							rpcService.getComponent(event.getName(), callback);
						} else {
							AsyncCallback<String> callback = new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									FloatingStatus.hideStatus();
								}

								@Override
								public void onSuccess(String result) {
									proteus.gwt.client.app.jsonObject.ComponentDTO component = parseComponentDTO(result);
									dragResult = Domain2DTO.convert(component);
								}
							};
							rpcService.getComponentJSON(event.getName(),
									callback);
						}
					}
				});
		eventBus.addHandler(DropComponentEvent.TYPE,
				new DropComponentEventHandler() {
					private Point dropP;

					// final int WAIT_DELAY = 1000;
					// final int MAX_RETRIES = 5;
					// int retryCount = 0;

					@Override
					public void onDropComponent(DropComponentEvent event) {
						if (classNames.isClicked()) {
							classNames.setOriginal();//set mouse clicked to false
							DiagramCanvas canvas = tabCanvas.currentMVI
									.getDiagramCanvas();

							dropP = MiscUtils.scaleLoc(event.getX(), event
									.getY(), canvas.getCanvasScale());
							logger.info("" + dropP);
							if (dragResult != null) {
								dropToCanvas(dropP);
							} else {
								GWT
										.log("error!!!!!!!!!. Time out of getting the Data Object!");
							}
						}
					}

				});
		eventBus.addHandler(DeleteComponentsEvent.TYPE,
				new DeleteComponentsEventHandler() {

					@Override
					public void onDeleteComponents(DeleteComponentsEvent event) {

						DiagramCanvas dc = currentmvi.getDiagramCanvas();
						CanvasItem[] selItems = dc.getSelectedItems();
						dc.getUndoSupport().postEdit(
								new RemoveComponentEdit(dc, selItems));
					}

				});
		eventBus.addHandler(ExportEvent.TYPE, new ExportEventHandler() {

			@Override
			public void onExport(ExportEvent event) {
				FloatingStatus.showStatus("Processing");
				final ExportProteusDTO exportDTO = event.getExportDTO();
				if (exportDTO.getType().equals(ExportType.MO)) {
					exportDTO.setCode(tabCanvas.currentMVI.getCodeCanvas()
							.getContentText());
					exportDTO.setTitle(tabCanvas.currentTitleName);
				} else {
					int x = 0, y = 0, w = Constant.diagramCanvasSize.width, h = Constant.diagramCanvasSize.height;
					exportDTO.setRgbArray(tabCanvas.currentMVI
							.getDiagramCanvas().getRGBArray(x, y, w, h));
					exportDTO.setCropHeight(h);
					exportDTO.setCropWidth(w);
					exportDTO.setTitle(tabCanvas.currentTitleName);
				}

				AsyncCallback<ExportProteusResponseDTO> callback = new AsyncCallback<ExportProteusResponseDTO>() {

					@Override
					public void onSuccess(ExportProteusResponseDTO result) {
						if (result.isSuccess()) {

							if (exportDTO.getType().equals(ExportType.MO)) {
								String filename = result.getUrl();
								String parameters = "filename=" + filename
										+ "&tabname=" + exportDTO.getTitle()
										+ ".mo";
								String url = AppContext
										.urlToServerUrl("/exportmo" + "?"
												+ parameters);
								// Window.alert(url);
								Window.open(url, "_self", "");
								FloatingStatus.hideStatus();
							} else {
								String url = AppContext.urlToServerUrl(result
										.getUrl());
								// Window.alert(url);
								Window.open(url, "_blank", "");
								FloatingStatus.hideStatus();
							}
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						FloatingStatus.hideStatus();
					}
				};
				rpcService.exportProteus(exportDTO, callback);
			}
		});

		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

			@Override
			public void onLogin(LoginEvent event) {
				String userinfo = event.getInfo();
				AsyncCallback<String> callback = new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(String result) {
						// logger.info(String.format("Logging in: %s", result));
						if (result.equals(Constants.INVALID_USER)) {
							setProfile(null);
						} else {
							try {
								Users user = parseUser(result);
								setCookie(result);
								setProfile(user);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				rpcService.login(userinfo, callback);
			}
		});
		eventBus.addHandler(LogoutEvent.TYPE, new LogoutEventHandler() {
			@Override
			public void onLogout(LogoutEvent event) {
				removeProfile();
			}
		});
	}
	
	

	private void setCookie(String result) {
		Date expires = new Date(System.currentTimeMillis() + DURATION);
		Cookies.setCookie(COOKIENAME, result, expires, null, "/", false);
	}

	public static Component newComponentInstance(String string) {
		return (Component) new NewClassDefDialog().getDefaultClassDef(string);
	}

	protected void dropToCanvas(Point dropP) {
		// assume it is of type model and default is
		// component
		// the constant name "model" should be changed
		Component comp = (Component) new NewClassDefDialog()
				.getDefaultClassDef("model");
		comp.setCompData(dragResult);
		tabCanvas.currentMVI.getDiagramCanvas().dropOnCanvas(comp, dropP);
		dragResult = null;
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
	}

	private void bindMainMenu() {

		editor.proteusMainMenu.miOpen.setCommand(new Command() {

			@Override
			public void execute() {
				uploadUrl = "http://155.69.129.79:8080/ProteusGWT/uploadmofile";
				final UploadMoFile uploadMoFile = new UploadMoFile(uploadUrl);
				uploadMoFile.box.center();
				uploadMoFile.formPanel
						.addSubmitHandler(new FormPanel.SubmitHandler() {
							public void onSubmit(SubmitEvent event) {

								uploadMoFile.lbMessage
										.removeStyleName(uploadMoFile.messageStyle
												.showFailMessage());
								uploadMoFile.lbMessage
										.removeStyleName(uploadMoFile.messageStyle
												.showWaitMessage());
								String fileName = uploadMoFile.fileUpload
										.getFilename().toLowerCase();
								if (fileName.length() == 0) {
									event.cancel();
									uploadMoFile.lbMessage
											.addStyleName(uploadMoFile.messageStyle
													.showFailMessage());
									uploadMoFile.lbMessage
											.setText("Please choose the file");
								} else if (!fileName.endsWith("mo")) {
									event.cancel();
									uploadMoFile.lbMessage
											.addStyleName(uploadMoFile.messageStyle
													.showFailMessage());
									uploadMoFile.lbMessage
											.setText("Please choose the .mo file");
								} else {
									uploadMoFile.lbMessage
											.addStyleName(uploadMoFile.messageStyle
													.showWaitMessage());
									Window.alert(uploadMoFile.fileUpload
											.getFilename());
									uploadMoFile.lbMessage
											.setText("opening...");
								}
							}
						});
				uploadMoFile.formPanel
						.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
							public void onSubmitComplete(
									SubmitCompleteEvent event) {
								uploadMoFile.lbMessage
										.removeStyleName(uploadMoFile.messageStyle
												.showFailMessage());
								uploadMoFile.lbMessage
										.removeStyleName(uploadMoFile.messageStyle
												.showWaitMessage());

								// Window.alert("result:\n" +
								// event.getResults());

								if (event.getResults() != null
										&& event
												.getResults()
												.indexOf(
														"Mo file successfully uploaded") != -1) {

									int pos1 = event.getResults()
											.indexOf("id=")
											+ "id=".length();
									int endpos1 = event.getResults().indexOf(
											"\r\n", pos1);
									if (endpos1 == -1) {
										endpos1 = event.getResults().indexOf(
												"\n", pos1);
									}
									String idStr = event.getResults()
											.substring(pos1, endpos1);
									long id = Long.valueOf(idStr);
									// Window.alert(id + "");
									if (id <= 0) {
										return;
									}
									AsyncCallback<GetModelResponseDTO> callback = new AsyncCallback<GetModelResponseDTO>() {

										@Override
										public void onFailure(Throwable caught) {
											uploadMoFile.lbMessage
													.addStyleName(uploadMoFile.messageStyle
															.showFailMessage());
											uploadMoFile.lbMessage
													.setText("File open failed. ");
										}

										@Override
										public void onSuccess(
												GetModelResponseDTO result) {
											if (result.isSuccess()) {
												ComponentDTO componentDTO = result
														.getComponentDTO();
												String name = componentDTO
														.getName();
												Component comp = newComponentInstance(name);
												comp.setCompData(componentDTO);
												comp.parseDiagram(componentDTO);

												ModelViewInstance canvas = new ModelViewInstance(
														comp, true);

												tabCanvas.newTab(name, canvas);
												FloatingStatus
														.showStatus(
																"File Open Successfully",
																2000);
												uploadMoFile.box.hide();
											}
										}
									};
									rpcService.getModel(id, callback);

								} else {
									uploadMoFile.lbMessage
											.addStyleName(uploadMoFile.messageStyle
													.showFailMessage());
									uploadMoFile.lbMessage
											.setText("File open failed. ");
								}
							}
						});
			}
		});
	}

	private void openModel(long id) {
		if (!JSON_FLAG) {
			AsyncCallback<GetModelResponseDTO> callback = new AsyncCallback<GetModelResponseDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					FloatingStatus.showStatus("File Open Failed",3000);
				}

				@Override
				public void onSuccess(GetModelResponseDTO result) {
					if (result.isSuccess()) {
						ComponentDTO componentDTO = result.getComponentDTO();
						String name = componentDTO.getName();
						Component comp = newComponentInstance(name);
						comp.setCompData(componentDTO);
						comp.parseDiagram(componentDTO);

						ModelViewInstance canvas = new ModelViewInstance(comp,
								true);
						tabCanvas.newTab(name, canvas);
						FloatingStatus.showStatus("File Open Successfully",
								1000);
					} 
				}
			};
			rpcService.getModel(id, callback);
		} else {
			AsyncCallback<String> callback = new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					FloatingStatus.hideStatus();
				}

				@Override
				public void onSuccess(String result) {
					proteus.gwt.client.app.jsonObject.ComponentDTO component = parseComponentDTO(result);
					// TODO CHECK WHETHER THE CHANGE OF CONVERT METHOD AFFECT.
					ComponentDTO componentDTO = Domain2DTO.convert(component);
					String name = componentDTO.getName();
					Component comp = newComponentInstance("model");
					comp.setCompData(componentDTO);
					comp.parseDiagram(componentDTO);
					ModelViewInstance mvi = new ModelViewInstance(comp, true);
					String shortName = name
							.substring(name.lastIndexOf(".") + 1);
					tabCanvas.newTab(shortName, mvi);
					// set the modelId sam
					mvi.setModelID(componentDTO.getID());
					FloatingStatus.hideStatus();
				}
			};
			rpcService.getModelJSON(id, callback);
		}
	}

	public void setProfile(Users user) {
		this.user = user;
		if (this.user != null) {
			proteusmainmenu.onLoginComplete(user.getName(), true);
		} else {
			proteusmainmenu.onLoginComplete("", false);
		}
	}

	private void removeProfile() {
		this.user = null;
		proteusmainmenu.onLogoutComplete();
		removeCookie();
	}

	private void removeCookie() {
		Cookies.removeCookie(COOKIENAME, "/");
	}

	public static native proteus.gwt.client.app.jsonObject.ComponentDTO parseComponentDTO(
			String jsonStr) /*-{
		return eval('(' + jsonStr + ')');
	}-*/;

	public static native Users parseUser(String jsonStr)
	/*-{
		return eval('(' + jsonStr + ')');
	}-*/;

}
