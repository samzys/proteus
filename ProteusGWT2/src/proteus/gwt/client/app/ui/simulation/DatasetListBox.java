package proteus.gwt.client.app.ui.simulation;

import java.util.logging.Level;
import java.util.logging.Logger;

import proteus.gwt.client.app.model.simulation.SimulationResult;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;


public abstract class DatasetListBox extends ListBox {
	
	private static final Logger logger = Logger.getLogger("ChartToolsVisualizationProvider");
	
	public DatasetListBox(SimulationResult result) {
		super();
		
//		setAnimationEnabled(true);
		setVisibleItemCount(1);
		
		populateItems(result);
		
		this.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				logger.log(Level.FINE, "dataset changed to [index] " + getSelectedIndex());
				
				if (getSelectedIndex() < 0 || getSelectedIndex() > getItemCount()) {
					return;
				}
				
				String dataset = getValue(getSelectedIndex());
				
				DatasetListBox.this.onChange(dataset);
			}
			
		});
		
	}
	
	protected void populateItems(SimulationResult result) {
		for (String title : result.getDatasets()) {
			logger.log(Level.FINE, "adding dataset: " + title);
			addItem(title, title);
		}
	}
	
	protected abstract void onChange(String dataset);
	
}
