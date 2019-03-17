package proteus.gwt.shared.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ComponentsNameMap implements IsSerializable {
	private HashMap<String, ArrayList<ArrayList<Integer>>> nameMap = new HashMap<String, ArrayList<ArrayList<Integer>>>();

	public HashMap<String, ArrayList<ArrayList<Integer>>> getNameMap() {
		return nameMap;
	}

	public void insertIntoNameMap(String name, ArrayList<Integer> indexList) {
		name = name.toLowerCase();
		if (nameMap.containsKey(name)) {
			nameMap.get(name).add(indexList);
		} else {
			ArrayList<ArrayList<Integer>> valueList = new ArrayList<ArrayList<Integer>>();
			valueList.add(indexList);
			nameMap.put(name, valueList);
		}
	}
}
