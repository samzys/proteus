/**
 * 
 */
package com.infoscient.proteus.modelica.runtime;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.infoscient.proteus.modelica.ClassDef;

/**
 * SymbolTable 
 * 
 * 
 * @author Lei Ting
 *
 */
public class SymbolTable extends OverflowHashMap {
	public static final Set<String> primitiveSet = new HashSet<String>();

	static {
		primitiveSet.add("Real");
		primitiveSet.add("Integer");
		primitiveSet.add("Boolean");
		primitiveSet.add("String");
	}

	public SymbolTable() {
		for (String name : primitiveSet) {
			Symbol s = new Symbol(name, Category.Predefined, 
					Scope.getInvisibleScope(), null);
			insert(name, s);
		}
	}
	
	protected static SymbolTable globalTable;
	
	public static void initGlobalTable() {
		globalTable = new SymbolTable();
		
		
	}
	
	public static SymbolTable getGlobalTable() {
		if (globalTable == null) {
			initGlobalTable();
		}
		
		return globalTable;
	}
	
	//TODO optimize global name (Modelica.*) lookup
	/*
	 * 1. look up for composite name : A.B.C
	 * 2. look up for simple name : without '.'
	 * 
	 * @param name the name to be looked up
	 * @param scope current scope
	 */
	public Symbol lookup(String name, Scope scope) {
		if (name.indexOf('.') != -1) {
			String[] names = name.split("\\.");
			Symbol symbol = null;
			if (scope != null) {
				for (String n : names) {
					symbol = lookupSimpleName(n, scope);
					if (symbol == null || symbol.getValue() == null) 
						return null;
					ClassDef classDef = (ClassDef)symbol.getValue();
					scope = classDef.scope;
				}
			} else {	// look up in all scopes, return the first match
				for (String n : names) {
					Symbol [] list = lookupSimpleName(n);
					if (list != null && list.length > 0) {
						symbol = null;
						for (Symbol sym : list) {
							if (name.startsWith(sym.name)) {
								symbol = sym;
							}
						}
					} else {
						return null;
					}
				}
			}
			return symbol;
		} else {
			if (scope != null) {
				return lookupSimpleName(name, scope);
			} else {	// look up in all scopes, return the first match
				Symbol [] list = lookupSimpleName(name);
				if (list != null && list.length > 0) {
					return list[0];
				} else {
					return null;
				}
			}
		}	
	}
	
	public Symbol lookupSimpleName(String name, Scope scope) {
		Symbol[] res = lookupSimpleName(name);
		
		while (scope != null && res != null) {
			for (Symbol s : res) {
				if (s.scope == scope) {
					return s;
				}
			}
			scope = scope.parent;
		}
		return null;
	}
	
	public Symbol[] lookupSimpleName(String name) {
		Object obj = table.get(name);
		
		if (obj instanceof Symbol) {
			return new Symbol[] { (Symbol)obj };
		} else if (obj instanceof List) {
			return ((List<Symbol>)obj).toArray(new Symbol[0]);
		} else {
			return null;
		}
	}
	
	/**
	 * Symbol
	 * 
	 * For component, value is associated with their class definitions
	 * 
	 * For user defined classes, a empty environment is created for 
	 * supporting lookup of composite names. (See ModelicaSpecification3.1 
	 * Section 5.3.2)
	 * 
	 * @author Lei Ting
	 */
	public static class Symbol {
		public  String   name;
		public  Category category;
		public  Scope    scope;
		public  Symbol   type;
		private Object   value;
		private Object   data;
		
		public Symbol(String name, Category category, 
				Scope scope, Symbol type) {
			this.name = name;
			this.category = category;
			/*
			 * for classes :
			 * symbol.scope == ((ClassDef)(symbol.value)).scope.parent
			 * 
			 */
			this.scope = scope;
			this.type = type;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(
					name + " " + category + " " + 
					scope + " " + (type==null?null:type.name));
			
			if (value != null && value instanceof ClassDef) {
				SymbolTable symbolTable = null;//((ClassDef)value).symbolTable;
				for (Object obj : symbolTable.values()) {
					if (obj instanceof Symbol) {
						Symbol sym = (Symbol) obj;
						if (sym.category == Category.Component) {
							sb.append("\n  " + sym.toString());
						}
					} else if (obj instanceof List) {
						List<Symbol> list = (List<Symbol>) obj;
						for (Symbol sym : list) {
							if (sym.category == Category.Component) {
								sb.append("\n  " + sym.toString());
							}
						}
					}
				}
			}
			
			return sb.toString();
		}

		/**
		 * @param value the value to set
		 */
		public void setValue(Object value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public Object getValue() {
			return value;
		}

		/**
		 * @param data the data to set
		 */
		public void setData(Object data) {
			this.data = data;
		}

		/**
		 * @return the data
		 */
		public Object getData() {
			return data;
		}
	}
	
	public static enum Category {
		Predefined,		// primitive types
		Defined,		// user defined class/model/function...etc
		ImportName, 
		Component, 		
	}
	
	/**
	 * Scope
	 * 
	 * The scope relationship in the symbol table:
	 * 
	 *        invisibleScope
	 *         /         \
	 *        /           \
	 * globalScope    top level package scope
	 *                  /     \            \
	 *                 /       \            \   
	 *         class scope   package scope  ....
	 *            /            / \
	 *           /            /   \
	 *       .....          ...   ...
	 *            
	 * @author Lei Ting
	 *
	 */
	public static class Scope implements Serializable {
		public Scope  parent;
		public Symbol symbol;
		final static Scope invisible = new Scope(null);
//		final static Scope global = new Scope(invisible);
		
		public Scope(Scope parent) {
			this.parent = parent;
		}
		
		public static Scope getInvisibleScope() {
			return invisible;
		}
		
//		public static Scope getGlobalScope() {
//			return global;
//		}
		
		@Override
		public String toString() {
			if (parent == null) {
				return "s";
			} else {
				return parent.toString() + "s"; 
			}
		}
	}
	
	public static void main(String[] args) {
		SymbolTable symbolTable = SymbolTable.getGlobalTable();
		
		String[] set = { "aa", "bb", "cc" };
		Symbol symbol = symbolTable.lookup("Real", Scope.getInvisibleScope());
		Symbol symbol2 = symbolTable.lookup("Integer", Scope.getInvisibleScope());
		Scope lastScope = Scope.getInvisibleScope();
		Scope scope = null;
		
		for (String name : set) {
			scope = new Scope(lastScope);
			lastScope = scope;
			Symbol s = new Symbol(name, Category.Component, 
					scope, symbol);
			symbolTable.insert(name, s);
			Symbol s2 = new Symbol(name, Category.Component, 
					scope, symbol2);
			symbolTable.insert(name, s2);
		}
		
		for (String name : primitiveSet) {
			Symbol s = symbolTable.lookup(name, Scope.getInvisibleScope());
			System.out.println(s);
		}
		
		while (scope != null) {
			for (String name : set) {
				Symbol s = symbolTable.lookup(name, scope);
				if (s != null) {
					System.out.println(s);
				}
			}
			scope = scope.parent;
		}
		
		System.out.println("finished");
	}
}
