package com.infoscient.proteus.db.domain;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the includecomponent database table.
 * 
 */
@Entity
@Table(name = "includecomponent")
public class Includecomponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;
	private String extent;

	private String origin;
	private String rotation;
	private String name;
	private String ifFlagName;
	private String arrayForm;
	@Column(name = "TYPE_ID")
	private Long typeId;

	@ManyToOne
	private Component component;

	@OneToMany(mappedBy = "includecomponent", cascade = CascadeType.ALL)
	private List<Includemodification> includemodifications = new ArrayList<Includemodification>();

	public Includecomponent() {
	}

	public String getArrayForm() {
		return arrayForm;
	}

	public void setArrayForm(String arrayForm) {
		this.arrayForm = arrayForm;
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

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
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

	public String getIfFlagName() {
		return ifFlagName;
	}

	public void setIfFlagName(String ifFlagName) {
		this.ifFlagName = ifFlagName;
	}

	public List<Includemodification> getIncludemodifications() {
		return includemodifications;
	}

	public void setIncludemodifications(
			List<Includemodification> includemodifications) {
		this.includemodifications = includemodifications;
	}
	
}