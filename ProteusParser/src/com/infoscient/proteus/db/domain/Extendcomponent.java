package com.infoscient.proteus.db.domain;

import java.io.Serializable;
import javax.persistence.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the extendcomponent database table.
 * 
 */
@Entity
@Table(name = "extendcomponent")
public class Extendcomponent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;
	private String extent;
	private String origin;
	private String rotation;

	@Column(name = "TYPE_ID")
	private Long typeId;

	@ManyToOne
	private Component component;

	@OneToMany(mappedBy = "extendcomponent", cascade = CascadeType.ALL)
	private List<Extendmodification> extendmodifications = new ArrayList<Extendmodification>();

	public Extendcomponent() {
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

	

	public List<Extendmodification> getExtendmodifications() {
		return extendmodifications;
	}

	public void setExtendmodifications(
			List<Extendmodification> extendmodifications) {
		this.extendmodifications = extendmodifications;
	}

}