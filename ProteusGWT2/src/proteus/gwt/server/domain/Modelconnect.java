package proteus.gwt.server.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Gao Lei Created Apr 12, 2011
 */
@Entity
@Table(name = "modelconnect")
public class Modelconnect implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Lob()
	@Column(name = "VALUE")
	private byte[] value;

	private String startpin;
	private String endpin;

	public Modelconnect() {
	}

	@ManyToOne
	private Model model;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public String getStartpin() {
		return startpin;
	}

	public void setStartpin(String startpin) {
		this.startpin = startpin;
	}

	public String getEndpin() {
		return endpin;
	}

	public void setEndpin(String endpin) {
		this.endpin = endpin;
	}
	
}