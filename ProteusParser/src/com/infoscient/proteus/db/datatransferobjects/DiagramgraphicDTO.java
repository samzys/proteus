package com.infoscient.proteus.db.datatransferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiagramgraphicDTO implements Serializable {
	private ArrayList<String> graphicList = new ArrayList<String>();
	private String extent;
	private String preserveAspectRatio;
	private String grid;

	public ArrayList<String> getGraphicList() {
		return graphicList;
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

	public String toString() {
		String ret = graphicList.size()+"\n";
		for (String s : graphicList) {
			ret += s + "\n";
		}
		return ret + extent + "\n" + preserveAspectRatio + "\n" + grid;
	}

}
