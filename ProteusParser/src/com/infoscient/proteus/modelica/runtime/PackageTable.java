/**
 * 
 */
package com.infoscient.proteus.modelica.runtime;

import java.util.List;

import com.infoscient.proteus.modelica.runtime.SymbolTable.Symbol;

/**
 * @author Lei Ting
 *
 */
public class PackageTable extends OverflowHashMap {
	private static PackageTable instance;
	
	public static PackageTable getInstance() {
		if (instance == null) {
			instance = new PackageTable();
		}
		
		return instance;
	}
	
	/**
	 * lookup 
	 * 
	 * lookup for all symbols of classes in a package
	 * 
	 * @param name the package name
	 * @return the list of all symbols
	 */
	public Symbol[] lookup(String name) {
		Object obj = table.get(name);
		
		if (obj instanceof Symbol) {
			return new Symbol[] { (Symbol)obj };
		} else if (obj instanceof List) {
			return ((List<Symbol>)obj).toArray(new Symbol[0]);
		} else {
			return null;
		}
	}
}
