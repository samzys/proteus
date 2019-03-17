package proteus.gwt.client.proxy;

import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusDTO;
import proteus.gwt.shared.datatransferobjects.ExportProteusResponseDTO;
import proteus.gwt.shared.datatransferobjects.GetClassNamesResponseDTO;
import proteus.gwt.shared.datatransferobjects.GetModelResponseDTO;
import proteus.gwt.shared.datatransferobjects.SimulationResultDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProteusRemoteServiceAsync {

	void getClassNames(AsyncCallback<GetClassNamesResponseDTO> callback);

	void getComponent(String name, AsyncCallback<ComponentDTO> callback);

	void getDemo(String name, AsyncCallback<ComponentDTO> callback);

	void Simulate(String model, AsyncCallback<SimulationResultDTO> callback);

	
	void mslSim(String model, AsyncCallback<SimulationResultDTO> callback);
	
	void getComponentNameXML(AsyncCallback<String> callback);

	void fetchOMCInfo(AsyncCallback<String> callback);

	void exportProteus(ExportProteusDTO exportDTO,
			AsyncCallback<ExportProteusResponseDTO> callback);

	void getModel(Long modelId, AsyncCallback<GetModelResponseDTO> callback);

	void getUploadUrl(AsyncCallback<String> callback);

	void login(String userinfo, AsyncCallback<String> callback);

	void saveMOFile(String modelCode, AsyncCallback<String> saveMOcallback);
	
	void getComponentJSON(String name, AsyncCallback<String> callback);

	void getModelJSON(Long modelId, AsyncCallback<String> callback);

	void getDemoJSON(String name, AsyncCallback<String> jsonCallback);

	void checkLogin(String string, AsyncCallback<String> asyncCallback);

	void SimulateJSON(String model, AsyncCallback<String> callback);

}
