package com.infoscient.proteus.db.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the restriction database table.
 * 
 */
@Entity
@Table(name="restriction")
public class Restriction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="NAME")
	private String name;

    public Restriction() {
    }

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}