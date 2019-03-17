package proteus.gwt.server.businesslogic;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ConnectDTO;

/**
 * @author Gao Lei Created Apr 20, 2011
 */
public class DomainComponentProxyTest {
	@Test
	public void testComponentProxy() throws ComponentNotFoundException {
		String wholeName = "Modelica.Electrical.Analog.Basic.Resistor";
		wholeName = "Modelica.Electrical.Analog.Examples.ChuaCircuit";
		
		ComponentDTO componentDTO = DomainComponentProxy.getComponent(wholeName);
		List<ConnectDTO> connectDTOList = componentDTO.getConnects();
		
		assertEquals(7,connectDTOList.size());
		
		
	}
}
