package proteus.gwt.shared.datatransferobjects;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Gao Lei Created Apr 15, 2011
 */
public class ConnectDTO implements IsSerializable,Serializable {
	private String value;

	private String startpin;
	private String endpin;

	
	public ConnectDTO(String value, String startpin, String endpin) {
		super();
		this.value = value;
		this.startpin = startpin;
		this.endpin = endpin;
	}

	public ConnectDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStartpin() {
		return startpin;
	}

	public void setStartpin(String startpin) {
		this.startpin = startpin;
	}

	public String getEndpin() {
		return endpin;
	}

	public void setEndpin(String endpin) {
		this.endpin = endpin;
	}

}
