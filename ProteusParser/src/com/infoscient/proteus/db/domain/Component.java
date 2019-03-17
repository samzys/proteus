package com.infoscient.proteus.db.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * The persistent class for the component database table.
 * 
 */
@Entity
@Table(name = "component")
public class Component implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	
	@Column(name = "NAME")
	private String name;

	@ManyToOne
	private Restriction restriction;

	@Column(name = "WHOLENAME")
	private String wholename;

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Parameter> parameters = new ArrayList<Parameter>();

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Icongraphic> icongraphics = new ArrayList<Icongraphic>();


	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Extendcomponent> extendComponents = new ArrayList<Extendcomponent>();
	

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Includecomponent> includeComponents = new ArrayList<Includecomponent>();
	
	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Diagramgraphic> diagramgraphics = new ArrayList<Diagramgraphic>();

	@OneToOne(mappedBy = "component", cascade = CascadeType.ALL)
    private Diagramgraphicsmodification diagramgraphicsmodification;

	@OneToMany(mappedBy = "component", cascade = CascadeType.ALL)
	private List<Componentconnect> componentConnect = new ArrayList<Componentconnect>();
	
	
	public Component(Long id) {
		this.id=id;
	}
	
	public Component() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public Diagramgraphicsmodification getDiagramgraphicsmodification() {
		return diagramgraphicsmodification;
	}

	public void setDiagramgraphicsmodification(
			Diagramgraphicsmodification diagramgraphicsmodification) {
		this.diagramgraphicsmodification = diagramgraphicsmodification;
	}

	public List<Diagramgraphic> getDiagramgraphics() {
		return diagramgraphics;
	}

	public void setDiagramgraphics(List<Diagramgraphic> diagramgraphics) {
		this.diagramgraphics = diagramgraphics;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Restriction getRestriction() {
		return restriction;
	}

	public void setRestriction(Restriction restriction) {
		this.restriction = restriction;
	}

	public String getWholename() {
		return wholename;
	}

	public void setWholename(String wholename) {
		this.wholename = wholename;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public List<Icongraphic> getIcongraphics() {
		return icongraphics;
	}

	public void setIcongraphics(List<Icongraphic> icongraphics) {
		this.icongraphics = icongraphics;
	}

	public List<Extendcomponent> getExtendComponents() {
		return extendComponents;
	}

	public void setExtendComponents(List<Extendcomponent> extendComponents) {
		this.extendComponents = extendComponents;
	}

	public List<Includecomponent> getIncludeComponents() {
		return includeComponents;
	}

	public void setIncludeComponents(List<Includecomponent> includeComponents) {
		this.includeComponents = includeComponents;
	}

	public List<Componentconnect> getComponentConnect() {
		return componentConnect;
	}

	public void setComponentConnect(List<Componentconnect> componentConnect) {
		this.componentConnect = componentConnect;
	}
	
	
}