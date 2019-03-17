package proteus.gwt.client.app.ui;

import java.util.logging.Logger;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.DeleteComponentsEvent;
import proteus.gwt.client.app.event.ExportEvent;
import proteus.gwt.client.app.event.FlipHorizontalEvent;
import proteus.gwt.client.app.event.FlipVerticalEvent;
import proteus.gwt.client.app.event.NewModuleEvent;
import proteus.gwt.client.app.event.RedoEvent;
import proteus.gwt.client.app.event.RotateAntiClockwiseEvent;
import proteus.gwt.client.app.event.RotateClockwiseEvent;
import proteus.gwt.client.app.event.SaveEvent;
import proteus.gwt.client.app.event.ShowDemoEvent;
import proteus.gwt.client.app.event.UndoEvent;
import proteus.gwt.client.app.event.ZoomEvent;
import proteus.gwt.client.app.event.ZoomInEvent;
import proteus.gwt.client.app.event.ZoomOutEvent;
import proteus.gwt.client.app.event.ZoomResetEvent;
import proteus.gwt.client.app.event.simulation.SimulateEvent;
import proteus.gwt.client.app.event.simulation.SimulateResultDisplayEvent;
import proteus.gwt.client.app.event.simulation.SimulateStopRequestedEvent;
import proteus.gwt.client.app.ui.menu.ProteusMainMenu;
import proteus.gwt.client.proxy.ProteusRemoteService;
import proteus.gwt.client.proxy.ProteusRemoteServiceAsync;
import proteus.gwt.shared.datatransferobjects.ExportProteusDTO;
import proteus.gwt.shared.util.ExportType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class ProteusEditor extends Composite {

	private static final Logger logger = Logger.getLogger("ProteusEditor");
	private ProteusRemoteServiceAsync proteusService = GWT
			.create(ProteusRemoteService.class);

	private HandlerManager eventBus;
	private static ProteusEditorUiBinder uiBinder = GWT
			.create(ProteusEditorUiBinder.class);

	interface ProteusEditorUiBinder extends UiBinder<Widget, ProteusEditor> {
	}

	interface GlobalResources extends ClientBundle {
		@NotStrict
		@Source("global.css")
		CssResource css();
	}

	public @UiField
	EditorToolbar editorToolbar;

	public @UiField
	ComponentsTreePanel componentsTreePanel;

	public @UiField
	SplitLayoutPanel splitLayoutPanel;

	public @UiField
	DockLayoutPanel mainLayout;

	public @UiField
	MessagePanel infoMsgPanel;

	public @UiField
	TabCanvas tabCanvas;

	public @UiField
	ProteusMainMenu proteusMainMenu;

	public ProteusEditor(HandlerManager eventBus) {
		initWidget(uiBinder.createAndBindUi(this));

		// Inject global styles.
		GWT.<GlobalResources> create(GlobalResources.class).css()
				.ensureInjected();
		this.eventBus = eventBus;
		bind();
	}

	private void bind() {
		bindMainMenu();

		editorToolbar.pbNewModule.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// default type is model
				eventBus.fireEvent(new NewModuleEvent("model"));
			}
		});

		editorToolbar.pbSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new SaveEvent());
			}
		});
		editorToolbar.pbUndo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new UndoEvent());
			}
		});

		editorToolbar.pbRedo.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new RedoEvent());
			}
		});

		editorToolbar.pbZoomIn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new ZoomInEvent());
			}
		});

		editorToolbar.pbZoomOut.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new ZoomOutEvent());
			}
		});

		editorToolbar.pbZoomReset.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new ZoomResetEvent());
			}
		});

		editorToolbar.pbRotateClockwise.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new RotateClockwiseEvent());
			}
		});
		editorToolbar.pbRotateAntiClockwise.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new RotateAntiClockwiseEvent());
			}
		});
		editorToolbar.pbFlipHorizontal.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new FlipHorizontalEvent());
			}
		});
		editorToolbar.pbFlipVertical.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				eventBus.fireEvent(new FlipVerticalEvent());
			}
		});
//
//		editorToolbar.pbZoom.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				// TODO Auto-generated method stub
//				eventBus.fireEvent(new ZoomEvent());
//			}
//		});

		editorToolbar.pbSimulate.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				simulate();
			}
		});

		editorToolbar.pbSimulateResult.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new SimulateResultDisplayEvent());
			}
		});

	}

	private void simulate() {
		// suffice and produce less code.
		// new SimulateAction().actionPerformed(null);
		boolean simulationInprogress = false;
		String[] styleNames = editorToolbar.pbSimulate.getStyleName()
				.split(" ");
		for (int i = 0; i < styleNames.length; ++i) {
			if (styleNames[i].equals(editorToolbar.simulateBtnStyle.running())) {
				simulationInprogress = true;
			}
		}

		if (simulationInprogress) {
			eventBus.fireEvent(new SimulateStopRequestedEvent());
			return;
		}

		final SimulationInit SimInit = new SimulationInit();
		SimInit.box.center();

		SimInit.btCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				SimInit.box.hide();
			}
		});

		SimInit.btSimulate.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent arg0) {
				double startTime = 0.0, stopTime = 1.0;
				int numOfInterval = 0;
				try {
					startTime = Double.parseDouble(SimInit.tbStartTime
							.getValue());
				} catch (Exception e) {
					startTime = 0.0;
				}
				try {
					stopTime = Double
							.parseDouble(SimInit.tbStopTime.getValue());
				} catch (Exception e) {
					stopTime = 0.0;
				}
				try {
					numOfInterval = Integer
							.parseInt(SimInit.tbInterval.getValue());
				} catch (Exception e) {
					numOfInterval= 500;
				}
				// hide the box
				SimInit.box.hide();

				eventBus.fireEvent(new SimulateEvent(editorToolbar.pbSimulate,
						startTime, stopTime, numOfInterval));

			}
		});
	}

	public void showStartSimulation() {
		editorToolbar.pbSimulate.removeStyleName(editorToolbar.simulateBtnStyle
				.stopped());
		editorToolbar.pbSimulate.addStyleName(editorToolbar.simulateBtnStyle
				.running());
		editorToolbar.pbSimulate
				.setTitle("Simulation is in progress, Click again to stop simulation");
	}

	public void showStopSimulation() {
		editorToolbar.pbSimulate.removeStyleName(editorToolbar.simulateBtnStyle
				.running());
		editorToolbar.pbSimulate.addStyleName(editorToolbar.simulateBtnStyle
				.stopped());
		editorToolbar.pbSimulate.setTitle("run simulation");
	}

	private void bindMainMenu() {
		proteusMainMenu.mielectricalCircuit.setCommand(new DemoMenuCommand(
				"Modelica.Electrical.Analog.Examples.ChuaCircuit"));
		
		proteusMainMenu.mispringSystem.setCommand(new DemoMenuCommand(
				"Modelica.Mechanics.Translational.Examples.InitialConditions"));
		
		proteusMainMenu.midcMotor.setCommand(new DemoMenuCommand(
		"Modelica.Mechanics.MultiBody.Examples.Systems.RobotR3.Components.Motor"));
		
		proteusMainMenu.midoublePendulum.setCommand(new DemoMenuCommand(
		"Modelica.Mechanics.MultiBody.Examples.Elementary.DoublePendulum"));
		
		
		proteusMainMenu.miNewClass.setCommand(new NewMenuCommand("class"));
		proteusMainMenu.miNewModel.setCommand(new NewMenuCommand("model"));
		// proteusMainMenu.miOpen.setCommand(new OpenCommand());
		proteusMainMenu.miSave.setCommand(new SaveCommand());

		proteusMainMenu.miPrint.setCommand(new PrintCommand());
		proteusMainMenu.miExit.setCommand(new ExitCommand());

		proteusMainMenu.miUndo.setCommand(new UndoCommand());
		proteusMainMenu.miRedo.setCommand(new RedoCommand());
		proteusMainMenu.miDelete.setCommand(new DeleteCommand());

		proteusMainMenu.miZoomIn.setCommand(new ZoomInCommand());
		proteusMainMenu.miZoomOut.setCommand(new ZoomOutCommand());

		proteusMainMenu.miRotateAntiClockwise
				.setCommand(new RotateAntiClockwiseCommand());
		proteusMainMenu.miRotateClockwise
				.setCommand(new RotateClockwiseCommand());
		proteusMainMenu.miFlipHorizontal
				.setCommand(new FlipHorizontalCommand());
		proteusMainMenu.miFlipVertical.setCommand(new FlipVerticalCommand());

		proteusMainMenu.miSimulate.setCommand(new SimulateCommand());
		proteusMainMenu.miPlot.setCommand(new PlotCommand());

		proteusMainMenu.miAbout.setCommand(new AboutCommand());
		proteusMainMenu.miHelp.setCommand(new HelpCommand());

		proteusMainMenu.miExportBMP.setCommand(new ExportAsCommand(
				ExportType.BMP));
		proteusMainMenu.miExportJPEG.setCommand(new ExportAsCommand(
				ExportType.JPEG));
		proteusMainMenu.miExportPNG.setCommand(new ExportAsCommand(
				ExportType.PNG));
		proteusMainMenu.miExportMO
				.setCommand(new ExportAsCommand(ExportType.MO));
		proteusMainMenu.miExportPDF.setCommand(new ExportAsCommand(
				ExportType.PDF));
	}

	class SimulateCommand implements Command {

		@Override
		public void execute() {
			simulate();
		}

	}

	class PlotCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new SimulateResultDisplayEvent());
		}

	}

	class AboutCommand implements Command {

		@Override
		public void execute() {

		}

	}

	class HelpCommand implements Command {

		@Override
		public void execute() {

		}

	}

	class RotateAntiClockwiseCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new RotateAntiClockwiseEvent());
		}

	}

	class RotateClockwiseCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new RotateClockwiseEvent());
		}

	}

	class FlipHorizontalCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new FlipHorizontalEvent());
		}

	}

	class FlipVerticalCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new FlipVerticalEvent());

		}

	}

	class ZoomInCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new ZoomInEvent());
		}

	}

	class ZoomOutCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new ZoomOutEvent());
		}

	}

	class DeleteCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new DeleteComponentsEvent());
		}

	}

	class ExportAsCommand implements Command {
		ExportType type;

		public ExportAsCommand(ExportType type) {
			this.type = type;

		}

		@Override
		public void execute() {
			ExportProteusDTO exportDTO = new ExportProteusDTO();
			exportDTO.setType(type);
			eventBus.fireEvent(new ExportEvent(exportDTO));
		}

	}

	// class OpenCommand implements Command {
	//
	// @Override
	// public void execute() {
	// final UploadMoFile uploadMoFile = new UploadMoFile();
	// uploadMoFile.box.center();
	// uploadMoFile.formPanel
	// .addSubmitHandler(new FormPanel.SubmitHandler() {
	// public void onSubmit(SubmitEvent event) {
	//
	// uploadMoFile.lbMessage
	// .removeStyleName(uploadMoFile.messageStyle
	// .showFailMessage());
	// uploadMoFile.lbMessage
	// .removeStyleName(uploadMoFile.messageStyle
	// .showWaitMessage());
	// String fileName = uploadMoFile.fileUpload
	// .getFilename().toLowerCase();
	// if (fileName.length() == 0) {
	//
	// event.cancel();
	// uploadMoFile.lbMessage
	// .addStyleName(uploadMoFile.messageStyle
	// .showFailMessage());
	// uploadMoFile.lbMessage
	// .setText("Please choose the file to upload");
	// } else if (!fileName.endsWith("mo")) {
	// event.cancel();
	// uploadMoFile.lbMessage
	// .addStyleName(uploadMoFile.messageStyle
	// .showFailMessage());
	// uploadMoFile.lbMessage
	// .setText("Please choose the .mo file to upload");
	// } else {
	// uploadMoFile.lbMessage
	// .addStyleName(uploadMoFile.messageStyle
	// .showWaitMessage());
	// System.err.println(uploadMoFile.fileUpload
	// .getFilename());
	// uploadMoFile.lbMessage.setText("uploading...");
	// }
	// }
	// });
	// uploadMoFile.formPanel
	// .addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
	// public void onSubmitComplete(SubmitCompleteEvent event) {
	// uploadMoFile.lbMessage
	// .removeStyleName(uploadMoFile.messageStyle
	// .showFailMessage());
	// uploadMoFile.lbMessage
	// .removeStyleName(uploadMoFile.messageStyle
	// .showWaitMessage());
	// System.err.println(event.getResults());
	// System.err.println(event.toDebugString());
	// System.err.println(event.getSource());
	// if (event.getResults() != null
	// && event.getResults().indexOf(
	// "Mo file successfully uploaded") != -1) {
	// System.err.println(event.getResults());
	// if (true) {
	// int pos1 = event.getResults().indexOf(
	// "url = ")
	// + "url = ".length();
	// int endpos1 = event.getResults().indexOf(
	// "\r\n", pos1);
	// if (endpos1 == -1) {
	// endpos1 = event.getResults().indexOf(
	// "\n", pos1);
	// }
	// String url = event.getResults().substring(
	// pos1, endpos1);
	//
	// int pos2 = event.getResults().indexOf(
	// "server side name = ")
	// + "server side name = ".length();
	// int endpos2 = event.getResults().indexOf(
	// "\r\n", pos2);
	// if (endpos2 == -1) {
	// endpos2 = event.getResults().indexOf(
	// "\n", pos2);
	// }
	// String serverSideName = event.getResults()
	// .substring(pos2, endpos2);
	//
	// uploadMoFile.lbMessage
	// .addStyleName(uploadMoFile.messageStyle
	// .showWaitMessage());
	// uploadMoFile.lbMessage
	// .setText("File uploaded successfully.");
	//
	// }
	// } else {
	// uploadMoFile.lbMessage
	// .addStyleName(uploadMoFile.messageStyle
	// .showFailMessage());
	// uploadMoFile.lbMessage
	// .setText("File upload failed.");
	// }
	// }
	// });
	// }
	//
	// }

	class SaveCommand implements Command {

		@Override
		public void execute() {
			ProteusGWT.eventBus.fireEvent(new SaveEvent());
		}
	}

	class PrintCommand implements Command {

		@Override
		public void execute() {

		}

	}

	class ExitCommand implements Command {

		@Override
		public void execute() {

		}
	}

	class DemoMenuCommand implements Command {
		String name;

		public DemoMenuCommand(String name) {
			this.name = name;
		}

		@Override
		public void execute() {
			eventBus.fireEvent(new ShowDemoEvent(name));
		}

	}

	class NewMenuCommand implements Command {
		String type;

		public NewMenuCommand(String type) {
			this.type = type;
		}

		@Override
		public void execute() {
			eventBus.fireEvent(new NewModuleEvent(type));
		}
	}

	class UndoCommand implements Command {

		public UndoCommand() {
		}

		@Override
		public void execute() {
			eventBus.fireEvent(new UndoEvent());
		}

	}

	class RedoCommand implements Command {

		public RedoCommand() {
		}

		@Override
		public void execute() {
			eventBus.fireEvent(new RedoEvent());
		}

	}
}
