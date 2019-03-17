package com.infoscient.proteus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.infoscient.proteus.modelica.ClassDef;

public class shortClassResolver {
	private static Map<String, String> primitiveMap = new HashMap<String, String>();
	// Above section are modified by Sam 21 Nov, 2010
	// Add primitive types
	public static final Set<String> primitiveSet = new HashSet<String>();

	static {
		primitiveSet.add("Real");
		primitiveSet.add("Integer");
		primitiveSet.add("Boolean");
		primitiveSet.add("String");
	}
	
	public shortClassResolver() {
		// TODO Auto-generated constructor stub
	
	}
	
	public void resolvingType(ClassDef [] cds){
		for (ClassDef cd : cds) {
			//check only restriction type first
			System.err.println(cd.type);
			if(cd.restriction.equals(Constants.TYPE_TYPE)){
				if(cd.refName!=null){
					put(cd.type, cd.refName);
				}
			}
			
		}
	}
	
	private void put(String type, String otherClass) {
		if(primitiveSet.contains(otherClass)){
			primitiveMap.put(type, otherClass);
			System.out.println(otherClass+", "+type);
		}else{
			String key = primitiveMap.get(otherClass);
			if(key!=null) put(type, key);
		}
	}

	public static void resolveShortClasses(String type, String otherClassString ) {

		if (primitiveSet.contains(otherClassString)) {
			System.out.println(type+": "+otherClassString);
			primitiveMap.put(type, otherClassString);
		}else{
			if(otherClassString!=null){ String s1 = primitiveMap.get(otherClassString);
			resolveShortClasses(type, s1);}
		}
	}
}
