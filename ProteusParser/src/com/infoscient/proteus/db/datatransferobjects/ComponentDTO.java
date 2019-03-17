package com.infoscient.proteus.db.datatransferobjects;

import java.util.ArrayList;
import java.util.List;


/**
 * @author sam
 * 
 */
public class ComponentDTO {
	private Long ID;
	private String docInfo; 
	private String declarationName;
	private DiagramgraphicDTO diagramGraphic;
	private String name;
	private String origin;
	private String rotation;
	private String extent;// null?,""
	private String restriction;
	private String wholeName;
	
	private List<String> arrayFormList = new ArrayList<String>();

	private List<IcongraphicDTO> iconGraphicList = new ArrayList<IcongraphicDTO>();
	private List<ConnectDTO> connects = new ArrayList<ConnectDTO>();
	private List<ComponentDTO> extendComps = new ArrayList<ComponentDTO>();
	private List<ComponentDTO> includeComps = new ArrayList<ComponentDTO>();
	private List<ParameterDTO> parameters = new ArrayList<ParameterDTO>();

	
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
