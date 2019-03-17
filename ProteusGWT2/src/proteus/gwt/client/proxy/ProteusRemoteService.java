package proteus.gwt.client.proxy;

import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusResponseDTO;
import proteus.gwt.shared.datatransferobjects.GetClassNamesResponseDTO;
import proteus.gwt.shared.datatransferobjects.GetModelResponseDTO;
import proteus.gwt.shared.datatransferobjects.SimulationResultDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("proteusservice")
public interface ProteusRemoteService extends RemoteService {
	
	GetClassNamesResponseDTO getClassNames();

//	// deprecated , use ComponentDTO getComponent(String name) instead
//	GetComponentDataResponseDTO GetComponent(String name);

	ComponentDTO getComponent(String name);

	String getComponentJSON(String name);

	ComponentDTO getDemo(String name);
	
	SimulationResultDTO Simulate(String model);
	
	
	String SimulateJSON(String model);
	
	String getComponentNameXML();
	
	ExportProteusResponseDTO exportProteus(ExportProteusDTO exportDTO);
	
	String getUploadUrl();

	GetModelResponseDTO getModel(Long modelId);

	String getModelJSON(Long modelId);
	
	String fetchOMCInfo();

	SimulationResultDTO mslSim(String model);
	
	String login(String userinfo);

	String saveMOFile(String modelCode);

	String checkLogin(String string);

	String getDemoJSON(String name);
}
