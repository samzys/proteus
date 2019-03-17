package proteus.gwt.client.app;


import proteus.gwt.client.app.ui.ProteusEditor;
import proteus.gwt.client.app.ui.TabCanvas;
import proteus.gwt.client.app.ui.common.codemirror.CodeMirrorResourceInjector;
import proteus.gwt.client.proxy.ProteusRemoteService;
import proteus.gwt.client.proxy.ProteusRemoteServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.PieChart;
import com.google.gwt.visualization.client.visualizations.corechart.ScatterChart;

//This is the entry point class
public class ProteusGWT implements EntryPoint {
	
	public static HandlerManager eventBus = new HandlerManager(null);
	private ProteusEditor editor = new ProteusEditor(eventBus);

	// private ProteusCanvas proteusCanvas =
	// editor.proteusTabCanvas.proteusCanvas;

	private TabCanvas tabCanvas = editor.tabCanvas;
	private ProteusRemoteServiceAsync proteusService = GWT
			.create(ProteusRemoteService.class);

	private ClassNames classNames;

	public void onModuleLoad() {
		loadVisualizationApi();
		CodeMirrorResourceInjector.injectAll();
		initUI();

		classNames = new ClassNames(editor, eventBus);
		classNames.initComponentsTreePanel();
		classNames.getCursorChangedWidgetList().add(tabCanvas.currentMVI.getDiagramCanvas());
		tabCanvas.setEventBus(eventBus);
		AppController app = new AppController(editor, classNames,
				proteusService, eventBus);
	}

	public void initUI() {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(editor);
	}
	
	public void loadVisualizationApi() {
		VisualizationApiLoader.loadApiIfNotLoaded(Table.PACKAGE);
		VisualizationApiLoader.loadApiIfNotLoaded(PieChart.PACKAGE);
		VisualizationApiLoader.loadApiIfNotLoaded(ScatterChart.PACKAGE);
		VisualizationApiLoader.loadApiIfNotLoaded(LineChart.PACKAGE);
	}
}
