/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;

/**
 * @author leiting
 * Created May 8, 2011
 */

public abstract class VisualizationTypeListBox extends ListBox {
	
	private String viewType = "Table";
	
	public VisualizationTypeListBox(String viewType) {
		super();
		this.viewType = viewType;
		
		//setAnimationEnabled(true);
		setVisibleItemCount(1);
		
		populateItems();
		
		this.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (getSelectedIndex() < 0 || getSelectedIndex() > getItemCount()) {
					return;
				}
				
				String cmdName = getValue(getSelectedIndex());
				
				VisualizationTypeListBox.this.onChange(cmdName);
			}
			
		});
		
		for (int i = 0; i < getItemCount(); ++i) {
			if (this.getValue(i).equals(viewType)) {
				this.setSelectedIndex(i);
//				onChange(viewType);
				break;
			}
		}
	}
	
	protected abstract void onChange(String cmdName);
	
	protected void addItem(String text, final String cmdName, String hint) {
//		addItem(text, new Command() {
//			@Override
//			public void execute() {
//				SimulationResultDialog.this.viewType = cmdName;
//				SimulationResultDialog.this.refreshVisualization();
//			}
//		});
		addItem(text, cmdName);
	}
	
	protected void populateItems() {
		addItem("table", "Table", "a data table view");
		addItem("line chart", "LineChart", "a line chart view");
//		addItem("bar chart", "BarChart", "a bar chart view");
//		addItem("column chart", "ColumnChart", "a column chart view");
//		addItem("annotated time line", "AnnotatedTimeLine", "annotated timeline view");
//		addItem("area chart", "AreaChart", "an area chart view");
//		addItem("intensity map", "IntensityMap", "an intensity map view");
//		addItem("motion chart", "MotionChart", "a motion chart view");
//		addItem("pie chart", "PieChart", "a pie chart view");
//		addItem("scatter chart", "ScatterChart", "a scatter chart view");
//		addItem("sparkline", "Sparkline", "a sparkline view");
	}
	
	public String getDefaultTypeName() {
		return viewType;
	}
}