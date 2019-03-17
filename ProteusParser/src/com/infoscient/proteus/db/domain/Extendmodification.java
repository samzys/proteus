package com.infoscient.proteus.db.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "extendmodification")
public class Extendmodification implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NAME")
	private String name;

	@Column(name = "VALUE")
	private String value;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Extendcomponent extendcomponent;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Extendcomponent getExtendcomponent() {
		return extendcomponent;
	}

	public void setExtendcomponent(Extendcomponent extendcomponent) {
		this.extendcomponent = extendcomponent;
	}

	

}
