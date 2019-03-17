package com.infoscient.proteus.db.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/** 
 * @author Gao Lei
 * Created Apr 12, 2011 
 */
@Entity
@Table(name = "modelinclude")
public class Modelinclude implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;
	private String extent;
	private String origin;
	private String rotation;
	private String name;
	@Column(name = "TYPE_ID")
	private Long typeId;

	@ManyToOne
	private Model model;


	@OneToMany(mappedBy = "modelinclude", cascade = CascadeType.ALL)
	private List<ModelIncludemodification> modelincludemodifications = new ArrayList<ModelIncludemodification>();

	
	public Modelinclude() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getRotation() {
		return rotation;
	}

	public void setRotation(String rotation) {
		this.rotation = rotation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ModelIncludemodification> getModelincludemodifications() {
		return modelincludemodifications;
	}
	
	
	
}