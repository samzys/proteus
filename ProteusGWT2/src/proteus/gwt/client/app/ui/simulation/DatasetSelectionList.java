/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author leiting
 * Created May 11, 2011
 */
public abstract class DatasetSelectionList extends VerticalPanel {

	List<String> datasets;
	List<String> selected = new ArrayList<String>();
	
	public DatasetSelectionList(List<String> datasets) {
		this.datasets = datasets;
		this.selected.addAll(datasets);
		
		initUI();
	}
	
	void initUI() {
		clear();
		
		for (final String dataset : datasets) {
			DatasetSelectionCheckBox checkbox = new DatasetSelectionCheckBox(dataset);
			checkbox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					if (event.getValue()) {
						if (!selected.contains(dataset)) {
							selected.add(dataset);
						}
					}
					else {
						selected.remove(dataset);
					}
					
					onSelectionChange(selected);
				}
			});
			checkbox.setValue(true);
			
			add(checkbox);
		}
	}
	
	public class DatasetSelectionCheckBox extends CheckBox {
		
		public DatasetSelectionCheckBox(String dataset) {
			this.setText(dataset);
		}
	}
	
	protected abstract void onSelectionChange(List<String> selected);
}
