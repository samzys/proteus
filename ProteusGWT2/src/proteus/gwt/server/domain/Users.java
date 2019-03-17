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
@Table(name = "users")
public class Users implements Serializable {
	@Id
	@Column(name = "UID")
	private int uid;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PASS")
	private String pass;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the uid
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * @param pass
	 *            the pass to set
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}
}
