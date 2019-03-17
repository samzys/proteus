/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import java.util.logging.Level;
import java.util.logging.Logger;

import proteus.gwt.client.app.model.simulation.SimulationResult;
import proteus.gwt.client.app.model.simulation.SimulationResultDisplayOptions;
import proteus.gwt.client.app.model.simulation.SimulationResultPool;
import proteus.gwt.client.app.model.simulation.SimulationVariable;
import proteus.gwt.client.app.model.simulation.SimulationVariableTree;
import proteus.gwt.client.app.model.simulation.SimulationVariableTreeParser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author leiting
 * Created May 4, 2011
 */
public class SimulationResultDisplayOptionsDialog extends DialogBox {

	private static final Logger logger = Logger.getLogger("SimulationResultDisplayOptionsDialog");
	
	private static SimulationResultDisplayOptionsDialogUiBinder uiBinder = GWT
			.create(SimulationResultDisplayOptionsDialogUiBinder.class);
	
	@UiField Button btnOk;
	@UiField Button btnCancel;
	@UiField CaptionPanel cpChartType;
	@UiField CaptionPanel cpVariables;
	@UiField ScrollPanel spVariables;

	interface SimulationResultDisplayOptionsDialogUiBinder extends
			UiBinder<Widget, SimulationResultDisplayOptionsDialog> {
	}

	SimulationResultDisplayOptions options = new SimulationResultDisplayOptions();
	SimulationResult result;
	
	public SimulationResultDisplayOptionsDialog(SimulationResult result) {
		add(uiBinder.createAndBindUi(this));
		
		this.result = result;
		
		initUI();
		
		setText("Plot Options");
	}

	protected void initUI() {
		SimulationVariableTree tree = 
			SimulationVariableTreeParser.flatToTree(result.getName(), result.getDatasets());
		
		SimulationVariableTreeComponent treeComponent = 
			new SimulationVariableTreeComponent(tree) {

			@Override
			protected void onValueChange(SimulationVariable node, Boolean value) {
				if (value) {
					options.addVariable(node.getName());
				}
				else {
					options.removeVariable(node.getName());
				}
			}
			
		};
		
		treeComponent.expandAll();

		spVariables.setWidth("400px");
		spVariables.setHeight("400px");
		
		spVariables.add(treeComponent);
		
		VisualizationTypeListBox typeList = new VisualizationTypeListBox("LineChart") {

			@Override
			protected void onChange(String cmdName) {
				options.setInitialChartType(cmdName);
			}
			
		};
		options.setInitialChartType(typeList.getDefaultTypeName());
		
		cpChartType.add(typeList);
		
		btnOk.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SimulationResultDisplayOptionsDialog.this.hide();
				
				logger.log(Level.FINE, options.toString());
				
				// plot the simulation result
				SimulationResultDialog dialog = new SimulationResultDialog(
						SimulationResultPool.getLastSimulateResult(), options, 
						new MixedVisualizationProvider());
				
				dialog.center();
			}
			
		});
		
		btnCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SimulationResultDisplayOptionsDialog.this.hide();
			}
			
		});
	}
}
