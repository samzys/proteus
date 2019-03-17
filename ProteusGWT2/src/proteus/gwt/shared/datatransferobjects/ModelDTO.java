package proteus.gwt.shared.datatransferobjects;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Gao Lei Created Apr 12, 2011
 */
/**
 * @author sam
 *The function of this class has been replaced by ComponentDTO
 *@deprecated
 */
public class ModelDTO implements IsSerializable {
	
	private String name;
	private String wholeName;
	private List<ComponentDTO> modelExtends = new ArrayList<ComponentDTO>();
	private List<ComponentDTO> modelIncludes = new ArrayList<ComponentDTO>();
	private List<ConnectDTO> modelConnects = new ArrayList<ConnectDTO>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWholeName() {
		return wholeName;
	}

	public void setWholeName(String wholeName) {
		this.wholeName = wholeName;
	}

	public List<ComponentDTO> getModelExtends() {
		return modelExtends;
	}

	public void setModelExtends(List<ComponentDTO> modelExtends) {
		this.modelExtends = modelExtends;
	}

	public List<ComponentDTO> getModelIncludes() {
		return modelIncludes;
	}

	public void setModelIncludes(List<ComponentDTO> modelIncludes) {
		this.modelIncludes = modelIncludes;
	}

	public List<ConnectDTO> getModelConnects() {
		return modelConnects;
	}

	

}
