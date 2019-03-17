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
@Table(name = "content_type_mod")
public class Content_type_mod implements Serializable {
	@Id
	@Column(name = "VID")
	private int vid;
	
	@Column(name = "FIELD_MO_FID")
	private int field_mo_fid;
	

	@Column(name = "FIELD_MID_VALUE")
	private int field_mid_value;


	/**
	 * @return the vid
	 */
	public int getVid() {
		return vid;
	}


	/**
	 * @param vid the vid to set
	 */
	public void setVid(int vid) {
		this.vid = vid;
	}


	/**
	 * @return the field_mo_fid
	 */
	public int getField_mo_fid() {
		return field_mo_fid;
	}


	/**
	 * @param fieldMoFid the field_mo_fid to set
	 */
	public void setField_mo_fid(int fieldMoFid) {
		field_mo_fid = fieldMoFid;
	}


	/**
	 * @return the field_mid_value
	 */
	public int getField_mid_value() {
		return field_mid_value;
	}


	/**
	 * @param fieldMidValue the field_mid_value to set
	 */
	public void setField_mid_value(int fieldMidValue) {
		field_mid_value = fieldMidValue;
	}
}
