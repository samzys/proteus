package com.infoscient.proteus.db.datatransferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IcongraphicDTO implements Serializable{
	private String graphics;
	private String extent;
	private String preserveAspectRatio;
	private String grid;

	public String getGraphics() {
		return graphics;
	}

	public void setGraphics(String graphics) {
		this.graphics = graphics;
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
		return graphics + "\n" + extent + "\n" + preserveAspectRatio + "\n"
				+ grid;
	}
}
