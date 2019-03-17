/**
 * 
 */
package proteus.gwt.client.app.model.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proteus.gwt.client.app.model.simulation.SimulationVariableTreeTraverser.NodeTraverseContext;

/**
 * 
 * @author leiting
 * Created May 8, 2011
 */
public class SimulationVariableTreeParser {

	public static SimulationVariableTree flatToTree(
			String rootName, Collection<String> variableNames) {
		
		Map <String, SimulationVariable> map = 
			new HashMap<String, SimulationVariable>();
		
		SimulationVariable rootVariable = new SimulationVariable(rootName, false);
		
		for (String variableName : variableNames) {
			SimulationVariable variable = new SimulationVariable(variableName, true);
			SimulationVariable parentVariable = null;
			
			int i = variableName.lastIndexOf('.');
			
			// down to top 
			while (i > 0) {
				String parentName = variableName.substring(0, i);
				if (!map.containsKey(parentName)) {
					parentVariable = new SimulationVariable(parentName, false);
					map.put(parentName, parentVariable);
				}
				
				parentVariable = map.get(parentName);
				
				if (!parentVariable.hasChild(variable)) {
					parentVariable.addChild(variable);
				}
				
				variable = parentVariable;
				i = parentName.lastIndexOf('.');
			}
			
			// add the grand parent to root
			SimulationVariable node = parentVariable == null ? variable : parentVariable;
			if (!rootVariable.hasChild(node)) {
				rootVariable.addChild(node);
			}
		}
		
		return new SimulationVariableTree(rootVariable);
	}
	
	public static List<String>  treeToFlat(SimulationVariableTree tree) {
		
		SimulationVariableTreeTraverser traverser = 
			new SimulationVariableTreeTraverser(tree) {

				@Override
				protected void visitNode(SimulationVariable node,
						NodeTraverseContext context) {
					List<String> variableNames = ((NodeVisitContext) context).variableNames;
					if (node.hasValue()) {
						variableNames.add(node.getName());
					}
				}
			
		};
		
		NodeVisitContext context = new NodeVisitContext();
		traverser.traverse(true, context);
		
		return context.variableNames;
	}
	
	private static class NodeVisitContext extends NodeTraverseContext {
		public List<String> variableNames = new ArrayList<String>();
	}
}
