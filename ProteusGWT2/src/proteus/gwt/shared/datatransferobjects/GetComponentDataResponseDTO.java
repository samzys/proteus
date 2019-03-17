package proteus.gwt.shared.datatransferobjects;

import proteus.gwt.shared.modelica.DemoTrans;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GetComponentDataResponseDTO implements IsSerializable {
	private boolean success;
	private String message;

	public String[] iconString;

	public String[] diagramString;

	public String[] connectorString;

	public DemoTrans connTrans[];
	
	public String name;
	
	public String restriction;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String[] getIconString() {
		return iconString;
	}

	public void setIconString(String[] iconString) {
		this.iconString = iconString;
	}

	public String[] getDiagramString() {
		return diagramString;
	}

	public void setDiagramString(String[] diagramString) {
		this.diagramString = diagramString;
	}

	public String[] getConnectorString() {
		return connectorString;
	}

	public void setConnectorString(String[] connectorString) {
		this.connectorString = connectorString;
	}

	public DemoTrans[] getConnTrans() {
		return connTrans;
	}

	public void setConnTrans(DemoTrans[] connTrans) {
		this.connTrans = connTrans;
	}

}
