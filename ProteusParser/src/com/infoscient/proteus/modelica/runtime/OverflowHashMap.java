/**
 * 
 */
package com.infoscient.proteus.modelica.runtime;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.infoscient.proteus.modelica.runtime.SymbolTable.Symbol;

/**
 * @author Lei Ting
 *
 */
public class OverflowHashMap {
	Map<String, Object> table = new HashMap<String, Object>();

	/**
	 * insert
	 * 
	 * insert a symbol into the table.
	 * if the name already exists in the table, 
	 * it will try to build a list for all symbols bearing
	 * the same name and insert the list into the table 
	 *  
	 * @param name
	 * @param symbol
	 */
	public void insert(String name, Symbol symbol) {
		if (!table.containsKey(name)) {
			table.put(name, symbol);
		} else {
			Object obj = table.get(name);
			if (obj instanceof Symbol) {
				List<Symbol> list = new LinkedList<Symbol>();
				list.add((Symbol)obj);
				list.add(symbol);
				table.put(name, list);
			} else if (obj instanceof List) {
				List<Symbol> list = (List<Symbol>)obj;
				list.add(symbol);
				table.put(name, list);
			} else {
				System.err.println("SymbolTable: error" +
						" in insert");
			}
		}
	}
	
	public void remove(String name) {
		table.remove(name);
	}
	
	public void clear() {
		table.clear();
	}
	
	public Set keySet() {
		return table.keySet();
	}
	
	public Collection values() {
		return table.values();
	}
		
}
