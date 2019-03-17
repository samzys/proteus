package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IcongraphicDTO implements IsSerializable, Serializable {
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

}
