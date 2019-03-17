package proteus.gwt.server.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** 
 * @author Gao Lei
 *  
 */
@Entity
@Table(name = "diagramgraphics")
public class Diagramgraphic implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Lob()
	@Column(name = "VALUE")
	private byte[] value;

	public Diagramgraphic() {
	}
	
	@ManyToOne
    private Component component;

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

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	
}