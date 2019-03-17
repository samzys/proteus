package com.infoscient.proteus.modelica.types;

import java.util.Map;

public class MEnumeration extends MPrimitive {

	public Map<String, String> enumContent;

	public String comment;

	public void toPrintout() {
		if (enumContent != null) {
			System.out.println("Enumeration Type with values");
			for (Map.Entry entry : enumContent.entrySet()) {
				System.out.println(entry.getKey() + ", " + entry.getValue());
			}
			System.out.println(comment);
		}
	}

	public void updateFields(String argName, String updatedValue) {

	}

	public String getUnit() {
		return null;
	}

	/**
	 * @return the enumContent
	 */
	public Map<String, String> getEnumContent() {
		return enumContent;
	}

	/**
	 * @param enumContent
	 *            the enumContent to set
	 */
	public void setEnumContent(Map<String, String> enumContent) {
		this.enumContent = enumContent;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String getDisplayUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFixed() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMax() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNominal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQuantity() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStart() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStateSelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType() {
		return "enumeration";
	}

}
