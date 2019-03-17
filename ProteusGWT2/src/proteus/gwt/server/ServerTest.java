package proteus.gwt.server;

import proteus.gwt.server.businesslogic.DomainComponentProxy;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ParameterDTO;

public class ServerTest {

	/**
	 * @param args
	 * @throws ComponentNotFoundException
	 */
	public static void main(String[] args) throws ComponentNotFoundException {
		// TODO Auto-generated method stub
		String[] testExamples = {
				"Modelica.Mechanics.MultiBody.Examples.Elementary.ForceAndTorque",
				"Modelica.Electrical.Machines.Examples.AIMC_DOL",
				"Modelica.Electrical.Machines.Examples.AIMS_Start" };

		for (String testname : testExamples) {
			ComponentDTO parentComponent = DomainComponentProxy
					.getComponent(testname);
			System.err.println(testname);
			for (ComponentDTO includecomp : parentComponent.getIncludeComps()) {
				System.out.println(includecomp.getWholeName() + " "
						+ includecomp.getDeclarationName() + " "
						+ includecomp.getArrayFormList().toString());
			}
			
			for(ParameterDTO p: parentComponent.getParameters()){
				
			}
		}
	}

}
