/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import proteus.gwt.client.app.model.simulation.SimulationDataTable;
import proteus.gwt.client.app.model.simulation.SimulationResult;
import proteus.gwt.client.app.model.simulation.SimulationResultDisplayOptions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
/**
 *
 * @author Lei Ting
 * Created Apr 10, 2011
 */
public class SimulationResultDialog extends DialogBox implements ValueChangeHandler<String> {

	private static final Logger logger = Logger.getLogger("SimulationResultDialog");
	
	private static SimulationResultDialogUiBinder uiBinder = GWT
			.create(SimulationResultDialogUiBinder.class);
	
	@UiField Button btnClose;
	@UiField ScrollPanel spVisualization;
	@UiField VerticalPanel vpOptions;

	interface SimulationResultDialogUiBinder extends
			UiBinder<Widget, SimulationResultDialog> {
	}

	private SimulationResult result;
	private SimulationResultDisplayOptions options;
	
	private VisualizationProvider visualizationProvider;
	
	private String viewType = "Table";
	private List<String> selectedVariables = new ArrayList<String>();
	
	public SimulationResultDialog(
			SimulationResult result, SimulationResultDisplayOptions options, 
			VisualizationProvider visualizationProvider) {
		super();
		this.result = result;
		this.options = options;
		this.visualizationProvider = visualizationProvider;
		
		viewType = options.getInitialChartType();
		selectedVariables.addAll(options.getVariableList());
		
		add(uiBinder.createAndBindUi(this));
		initUI();
	}
	
	void initUI() {
		setSize("500px", "500px");
		setModal(true);
		setText("Simulation result");

		spVisualization.setSize("380px", "450px");
		
		DatasetSelectionList datasetList = new DatasetSelectionList(options.getVariableList()) {

			@Override
			protected void onSelectionChange(List<String> selected) {
				selectedVariables = selected;
				SimulationResultDialog.this.refreshVisualization();
			}
			
		};
		
		VisualizationTypeListBox typeList = new VisualizationTypeListBox(viewType) {

			@Override
			protected void onChange(String cmdName) {
				SimulationResultDialog.this.viewType = cmdName;
				SimulationResultDialog.this.refreshVisualization();
			}
			
		};
		
		btnClose.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				SimulationResultDialog.this.hide(true);
			}
		});

		vpOptions.add(datasetList);
		vpOptions.add(typeList);
		
		Timer timer = new Timer() {

			@Override
			public void run() {
				refreshVisualization();
			}
			
		};
		timer.schedule(200);
	}
	
	protected SimulationDataTable getDataTable() {
		return result.getSelectedData(selectedVariables);
	}

	public void refreshVisualization() {
		int width = spVisualization.getOffsetWidth() - 20;
		int height = spVisualization.getOffsetHeight() - 20;
		
		spVisualization.clear();
		spVisualization.add(visualizationProvider.getVisualization(
				result.getName(), viewType, width, height, getDataTable()));
	}
	
	@Override
	public void onValueChange(ValueChangeEvent event) {
		viewType = event.getValue().toString();
		refreshVisualization();
	}
	
	
}
