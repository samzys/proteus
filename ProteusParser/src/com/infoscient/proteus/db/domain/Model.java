package com.infoscient.proteus.db.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Gao Lei Created Apr 12, 2011
 */
@Entity
@Table(name = "model")
public class Model implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "WHOLENAME")
	private String wholename;

	@OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
	private List<Modelconnect> modelConnects = new ArrayList<Modelconnect>();

	@OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
	private List<Modelextend> modelExtends = new ArrayList<Modelextend>();

	@OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
	private List<Modelinclude> modelIncludes = new ArrayList<Modelinclude>();

	public Model() {
	}

	public Model(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWholename() {
		return wholename;
	}

	public void setWholename(String wholename) {
		this.wholename = wholename;
	}

	public List<Modelconnect> getModelConnects() {
		return modelConnects;
	}

	public List<Modelextend> getModelExtends() {
		return modelExtends;
	}

	public List<Modelinclude> getModelIncludes() {
		return modelIncludes;
	}

	public boolean clear() {
		modelConnects.clear();
		modelExtends.clear();
		modelIncludes.clear();
		return true;
	}
}