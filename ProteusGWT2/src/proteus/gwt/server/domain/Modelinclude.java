package proteus.gwt.server.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Gao Lei Created Apr 12, 2011
 */
@Entity
@Table(name = "modelinclude")
public class Modelinclude implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private Long id;
	private String extent;
	private String origin;
	private String rotation;
	private String name;
	private String ifFlagName;
	private String arrayForm;
	@Column(name = "TYPE_ID")
	private Long typeId;
	@Column(name = "PROTECTED")
	private String protected_;
	@Column(name = "Final")
	private String final_;
	@Column(name = "TYPING")
	private String typing;
	private String variability;
	private String causality;

	@ManyToOne
	private Model model;

	@OneToMany(mappedBy = "modelinclude", cascade = CascadeType.ALL)
	private List<ModelIncludemodification> modelincludemodifications = new ArrayList<ModelIncludemodification>();

	public List<ModelIncludemodification> getModelincludemodifications() {
		return modelincludemodifications;
	}

	public Modelinclude() {
	}

	public Long getId() {
		return id;
	}

	public String getIfFlagName() {
		return ifFlagName;
	}

	public void setIfFlagName(String ifFlagName) {
		this.ifFlagName = ifFlagName;
	}

	public String getArrayForm() {
		return arrayForm;
	}

	public void setArrayForm(String arrayForm) {
		this.arrayForm = arrayForm;
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

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the protected_
	 */
	public String getProtected_() {
		return protected_;
	}

	/**
	 * @param protected1 the protected_ to set
	 */
	public void setProtected_(String protected1) {
		protected_ = protected1;
	}

	/**
	 * @return the final_
	 */
	public String getFinal_() {
		return final_;
	}

	/**
	 * @param final1 the final_ to set
	 */
	public void setFinal_(String final1) {
		final_ = final1;
	}

	/**
	 * @return the typing
	 */
	public String getTyping() {
		return typing;
	}

	/**
	 * @param typing the typing to set
	 */
	public void setTyping(String typing) {
		this.typing = typing;
	}



	public String getVariability() {
		return variability;
	}

	public void setVariability(String variability) {
		this.variability = variability;
	}

	/**
	 * @return the causality
	 */
	public String getCausality() {
		return causality;
	}

	/**
	 * @param causality the causality to set
	 */
	public void setCausality(String causality) {
		this.causality = causality;
	}

}