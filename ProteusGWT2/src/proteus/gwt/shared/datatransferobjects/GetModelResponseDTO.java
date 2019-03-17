package proteus.gwt.shared.datatransferobjects;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GetModelResponseDTO implements IsSerializable {

	private String message;
	private boolean success;
	private long modelID;
	/**
	 * @return the modelID
	 */
	public long getModelID() {
		return modelID;
	}

	/**
	 * @param modelID the modelID to set
	 */
	public void setModelID(long modelID) {
		this.modelID = modelID;
	}

	private ComponentDTO componentDTO;

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	public ComponentDTO getComponentDTO() {
		return componentDTO;
	}

	public void setComponentDTO(ComponentDTO componentDTO) {
		this.componentDTO = componentDTO;
	}

	
}
