package com.infoscient.proteus.db.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "diagramgraphicsmodification")
public class Diagramgraphicsmodification {
	@Id
	@Column(name = "ID")
	private Long id;
	private String extent;
	private String preserveAspectRatio;
	private String grid;
	@OneToOne	
	private Component component;
	
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
	public String getPreserveAspectRatio() {
		return preserveAspectRatio;
	}
	public void setPreserveAspectRatio(String preserveAspectRatio) {
		this.preserveAspectRatio = preserveAspectRatio;
	}
	public String getGrid() {
		return grid;
	}
	public void setGrid(String grid) {
		this.grid = grid;
	}
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	
	

}
