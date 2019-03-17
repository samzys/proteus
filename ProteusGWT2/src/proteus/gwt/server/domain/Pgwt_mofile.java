package proteus.gwt.server.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sam
 * 
 */
@Entity
@Table(name = "pgwt_mofile")
public class Pgwt_mofile implements Serializable {
	@Column(name = "FILEPATH")
	private int filepath;

	@Id
	@Column(name = "MID")
	private int mid;

	/**
	 * @return the filepath
	 */
	public int getFilepath() {
		return filepath;
	}

	/**
	 * @return the mid
	 */
	public int getMid() {
		return mid;
	}

	/**
	 * @param filepath the filepath to set
	 */
	public void setFilepath(int filepath) {
		this.filepath = filepath;
	}

	/**
	 * @param mid the mid to set
	 */
	public void setMid(int mid) {
		this.mid = mid;
	}
	
}
