package com.infoscient.proteus.db.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "modelincludemodification")
public class ModelIncludemodification implements Serializable {
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
	private Modelinclude modelinclude;

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

	public Modelinclude getModelinclude() {
		return modelinclude;
	}

	public void setModelinclude(Modelinclude modelinclude) {
		this.modelinclude = modelinclude;
	}

	

}
