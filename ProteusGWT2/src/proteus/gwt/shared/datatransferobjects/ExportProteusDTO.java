package proteus.gwt.shared.datatransferobjects;

import proteus.gwt.shared.util.ExportType;

import com.google.gwt.user.client.rpc.IsSerializable;


public class ExportProteusDTO implements IsSerializable {
	private int[] rgbArray;
	private int cropX;
	private int cropY;
	private int cropWidth;
	private int cropHeight;
	private ExportType type;
	private String code;
	private String title;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ExportType getType() {
		return type;
	}

	public void setType(ExportType type) {
		this.type = type;
	}

	public int[] getRgbArray() {
		return rgbArray;
	}

	public void setRgbArray(int[] rgbArray) {
		this.rgbArray = rgbArray;
	}

	public int getCropX() {
		return cropX;
	}

	public void setCropX(int cropX) {
		this.cropX = cropX;
	}

	public int getCropY() {
		return cropY;
	}

	public void setCropY(int cropY) {
		this.cropY = cropY;
	}

	public int getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(int cropWidth) {
		this.cropWidth = cropWidth;
	}

	public int getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(int cropHeight) {
		this.cropHeight = cropHeight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	

}
