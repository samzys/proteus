package proteus.gwt.shared.modelica;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DemoTrans implements IsSerializable {

	public String orgin;
	public String extent;
	public String rotation;

	public DemoTrans() {
	}

	public DemoTrans(String extent) {
		this.extent = extent;
	}
}
