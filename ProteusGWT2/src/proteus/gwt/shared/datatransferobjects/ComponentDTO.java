package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author sam
 * 
 */
public class ComponentDTO implements IsSerializable,Serializable  {
	private Long ID;
	private String docInfo; 
	private String name;
	private String restriction;
	private String wholeName;
	private String comment;
	

	private DiagramgraphicDTO diagramGraphic;
	private List<IcongraphicDTO> iconGraphicList = new ArrayList<IcongraphicDTO>();
	private List<ConnectDTO> connects = new ArrayList<ConnectDTO>();
	private List<ComponentDTO> extendComps = new ArrayList<ComponentDTO>();
	private List<ComponentDTO> includeComps = new ArrayList<ComponentDTO>();
	private List<ParameterDTO> parameters = new ArrayList<ParameterDTO>();
	
	/*#This part variables are for instantiations#*/
	private String declarationName;
	private String origin;
	private String rotation;
	private String extent;// null?,""
	//this for attribute
	private String protected_;
	private String final_;
	private String variability;
	private String typing;
	private String causality;
	//attribute end
	private List<String> arrayFormList = new ArrayList<String>();
	private List<ModificationArgument> superIncludeArgList = new ArrayList<ModificationArgument>();
	
	public String getProtected_() {
		return protected_;
	}

	public void setProtected_(String protected_) {
		this.protected_ = protected_;
	}

	public String getFinal_() {
		return final_;
	}

	public void setFinal_(String final_) {
		this.final_ = final_;
	}


	public String getVariability() {
		return variability;
	}

	public void setVariability(String variability) {
		this.variability = variability;
	}

	public String getTyping() {
		return typing;
	}

	public void setTyping(String typing) {
		this.typing = typing;
	}

	public String getCausality() {
		return causality;
	}

	public void setCausality(String causality) {
		this.causality = causality;
	}

	
	public ComponentDTO() {
	}

	public List<String> getArrayFormList() {
		return arrayFormList;
	}

	public List<ConnectDTO> getConnects() {
		return connects;
	}

	public String getDeclarationName() {
		return declarationName;
	}

	public DiagramgraphicDTO getDiagramGraphic() {
		return diagramGraphic;
	}

	public String getDocInfo() {
		return docInfo;
	}

	public List<ComponentDTO> getExtendComps() {
		return extendComps;
	}

	public String getExtent() {
		return extent;
	}

	public List<IcongraphicDTO> getIconGraphicList() {
		return iconGraphicList;
	}

	public String getIconGraphics2() {
		if (iconGraphicList.size() > 0) {
			return iconGraphicList.get(0).getGraphics();
		}
		return null;
	}

	public IcongraphicDTO getIconGraphicsDTO() {
		if (iconGraphicList.size() > 0)
			return iconGraphicList.get(0);
		else
			return null;
	}

	/**
	 * @return the iD
	 */
	public Long getID() {
		return ID;
	}


	/**
	 * @return the superIncludeArgList
	 */
	public List<ModificationArgument> getSuperIncludeArgList() {
		return superIncludeArgList;
	}

	/**
	 * @param superIncludeArgList the superIncludeArgList to set
	 */
	public void setSuperIncludeArgList(
			List<ModificationArgument> superIncludeArgList) {
		this.superIncludeArgList = superIncludeArgList;
	}

	public List<ComponentDTO> getIncludeComps() {
		return includeComps;
	}

	public String getName() {
		return name;
	}

	public String getOrigin() {
		return origin;
	}

	public List<ParameterDTO> getParameters() {
		return parameters;
	}

	public String getRestriction() {
		return restriction;
	}

	public String getRotation() {
		return rotation;
	}

	public String getWholeName() {
		return wholeName;
	}

	public void setArrayFormList(List<String> arrayFormList) {
		this.arrayFormList = arrayFormList;
	}

	public void setConnects(List<ConnectDTO> connects) {
		this.connects = connects;
	}

	public void setDeclarationName(String declarationName) {
		this.declarationName = declarationName;
	}

	public void setDiagramGraphic(DiagramgraphicDTO diagramGraphic) {
		this.diagramGraphic = diagramGraphic;
	}

	public void setDocInfo(String docInfo) {
		this.docInfo = docInfo;
	}

	public void setExtendComps(List<ComponentDTO> extendComps) {
		this.extendComps = extendComps;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	public void setIconGraphicList(List<IcongraphicDTO> iconGraphicList) {
		this.iconGraphicList = iconGraphicList;
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(Long iD) {
		ID = iD;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setIncludeComps(List<ComponentDTO> includeComps) {
		this.includeComps = includeComps;
	}


	public void setName(String name) {
		this.name = name;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public void setParameters(List<ParameterDTO> parameters) {
		this.parameters = parameters;
	}

	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public void setWholeName(String wholeName) {
		this.wholeName = wholeName;
	}

}
