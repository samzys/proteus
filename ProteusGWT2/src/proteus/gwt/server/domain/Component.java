package proteus.gwt.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Gao Lei
 * 
 */
@Entity
@Table(name = "component")
public class Component implements Serializable {
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Componentconnect> componentConnect = new ArrayList<Componentconnect>();

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Diagramgraphic> diagramgraphics = new ArrayList<Diagramgraphic>();

	@OneToOne(mappedBy = "component", cascade = CascadeType.ALL)
	private Diagramgraphicsmodification diagramgraphicsmodification;

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Enumeration> enumList = new ArrayList<Enumeration>();

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Extendcomponent> extendComponents = new ArrayList<Extendcomponent>();

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Icongraphic> icongraphics = new ArrayList<Icongraphic>();

	@Id
	@Column(name = "ID")
	private Long id;

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Includecomponent> includeComponents = new ArrayList<Includecomponent>();

	@Column(name = "NAME")
	private String name;

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Parameter> parameters = new ArrayList<Parameter>();

	@ManyToOne
	private Restriction restriction;

	@Column(name = "WHOLENAME")
	private String wholename;

	@Column(name = "COMMENT")
	private String comment;

	public Component() {
	}

	public Component(Long id) {
		this.id = id;
	}

	public List<Componentconnect> getComponentConnect() {
		return componentConnect;
	}

	public List<Diagramgraphic> getDiagramgraphics() {
		return diagramgraphics;
	}

	public Diagramgraphicsmodification getDiagramgraphicsmodification() {
		return diagramgraphicsmodification;
	}

	/**
	 * @return the enumList
	 */
	public List<Enumeration> getEnumList() {
		return enumList;
	}

	public List<Extendcomponent> getExtendComponents() {
		return extendComponents;
	}

	public List<Icongraphic> getIcongraphics() {
		return icongraphics;
	}

	public Long getId() {
		return id;
	}

	public List<Includecomponent> getIncludeComponents() {
		return includeComponents;
	}

	public String getName() {
		return name;
	}

	public List<Parameter> getParameters() {
		return parameters;
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

	public Restriction getRestriction() {
		return restriction;
	}

	public String getWholename() {
		return wholename;
	}

	public void setComponentConnect(List<Componentconnect> componentConnect) {
		this.componentConnect = componentConnect;
	}

	public void setDiagramgraphics(List<Diagramgraphic> diagramgraphics) {
		this.diagramgraphics = diagramgraphics;
	}

	public void setDiagramgraphicsmodification(
			Diagramgraphicsmodification diagramgraphicsmodification) {
		this.diagramgraphicsmodification = diagramgraphicsmodification;
	}

	/**
	 * @param enumList
	 *            the enumList to set
	 */
	public void setEnumList(List<Enumeration> enumList) {
		this.enumList = enumList;
	}

	public void setExtendComponents(List<Extendcomponent> extendComponents) {
		this.extendComponents = extendComponents;
	}

	public void setIcongraphics(List<Icongraphic> icongraphics) {
		this.icongraphics = icongraphics;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setIncludeComponents(List<Includecomponent> includeComponents) {
		this.includeComponents = includeComponents;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	public void setWholename(String wholename) {
		this.wholename = wholename;
	}

}