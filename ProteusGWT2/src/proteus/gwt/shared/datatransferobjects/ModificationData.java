package proteus.gwt.shared.datatransferobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModificationData {
	public Long id;
	public String extent;
	public String origin;
	public String rotation;
	public String declarationName;
	public String arrayForm;

//	public Map<String, String> arguments = new HashMap<String, String>();

	public List<ModificationArgument> argsList = new ArrayList<ModificationArgument>();

	public ModificationData() {
	}

	public ModificationData(Long id) {
		this.id = id;
	}
}