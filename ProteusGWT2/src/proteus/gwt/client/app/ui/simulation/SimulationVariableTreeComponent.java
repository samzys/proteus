/**
 * 
 */
package proteus.gwt.client.app.ui.simulation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import proteus.gwt.client.app.model.simulation.SimulationVariable;
import proteus.gwt.client.app.model.simulation.SimulationVariableTree;
import proteus.gwt.client.app.model.simulation.SimulationVariableTreeTraverser;
import proteus.gwt.client.app.model.simulation.SimulationVariableTreeTraverser.NodeTraverseContext;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HasTreeItems;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * @author leiting
 * Created May 8, 2011
 */
public abstract class SimulationVariableTreeComponent extends Tree {

	SimulationVariableTree model;
	
	public SimulationVariableTreeComponent(SimulationVariableTree model) {
		this.model = model;
		
		setAnimationEnabled(true);
		
		initVariableTree();
	}
	
	private class NodeVisitContext extends NodeTraverseContext {
		public Map<String, HasTreeItems> map = new HashMap<String, HasTreeItems>();
	}
	
	private HasTreeItems addTreeItem(HasTreeItems hasItems, final SimulationVariable node) {
		if (node.hasValue()) {
			CheckBox check = new CheckBox(node.getName());
			check.setValue(false);
			check.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					SimulationVariableTreeComponent.this.onValueChange(node, event.getValue());
				}
				
			});

			hasItems.addItem(check);
			return null;
		}
		else {
			TreeItem item = new TreeItem(node.getName());
			hasItems.addItem(item);
			
			return item;
		}
	}
	
	public void expandAll() {
		Iterator<TreeItem> it = this.treeItemIterator();
		
		while (it.hasNext()) {
			TreeItem item = it.next();
			item.setState(true);
		}
	}
	
	protected void initVariableTree() {
		SimulationVariableTree tree = this.model;
		
		SimulationVariableTreeTraverser traverser = new 
			SimulationVariableTreeTraverser(tree) {

				@Override
				protected void visitNode(
						SimulationVariable node, NodeTraverseContext context) {
					Map<String, HasTreeItems> map = ((NodeVisitContext) context).map;
					HasTreeItems parentItem;
					
					if (node.getParent() == null) {	// root
						parentItem = SimulationVariableTreeComponent.this;
					}
					else {
						parentItem = map.get(node.getParent().getName());	
					}
					
					HasTreeItems hasItems = addTreeItem(parentItem, node);
					
					if (hasItems != null) {
						map.put(node.getName(), hasItems);
					}
				}
			
		};
		
		NodeVisitContext context = new NodeVisitContext();
		traverser.traverse(true, context);
	}
	
	protected abstract void onValueChange(SimulationVariable node, Boolean value);
	
}
