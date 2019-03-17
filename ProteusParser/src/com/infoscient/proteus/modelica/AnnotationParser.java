/**
 * 
 */
package com.infoscient.proteus.modelica;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import com.infoscient.proteus.modelica.ClassDef.Annotation;
import com.infoscient.proteus.modelica.ClassDef.NamedArgument;
import com.infoscient.proteus.modelica.parser.ModelicaParser;
import com.infoscient.proteus.modelica.parser.OMArgument;
import com.infoscient.proteus.modelica.parser.OMArgumentList;
import com.infoscient.proteus.modelica.parser.OMElementModification;
import com.infoscient.proteus.modelica.parser.OMExpression;
import com.infoscient.proteus.modelica.parser.OMFunctionArguments;
import com.infoscient.proteus.modelica.parser.OMModification;
import com.infoscient.proteus.modelica.parser.OMNamedArgument;
import com.infoscient.proteus.modelica.parser.OMPrimary;
import com.infoscient.proteus.modelica.parser.ParseException;

/**
 * @author Lei Ting
 *
 */
public class AnnotationParser {
	
	public static void parseConAnnotation(OMArgument omarg, Annotation a) {
		// @sam 3 layers
 		OMElementModification em = getElementModificationFromArgument(omarg);

		String argName = em.componentRef.name;
		OMModification m = em.modification;
		a.map.put(argName, m);
		if (m != null) {
			List<OMArgument> args = getArgumentsFromModification(m);
			if (args != null) {
				for (OMArgument arg : args) {
					OMElementModification em2 = getElementModificationFromArgument(arg);
					String argName2 = argName + "." + em2.componentRef.name;
					OMModification m2 = em2.modification;
					a.map.put(argName2, m2);
					if (m2 != null) {
						List<OMArgument> args2 = getArgumentsFromModification(m2);
						if (args2 != null) {
							for (OMArgument arg2 : args2) {
								OMElementModification em3 = getElementModificationFromArgument(arg2);
								String argName3 = argName2 + "."
										+ em3.componentRef.name;
								OMModification m3 = em3.modification;
								a.map.put(argName3, m3);
							}
						}
					}
				}
			}
		}

	}
	public static void parsePlacement(OMModification m, Annotation a) {
		a.map.put("Placement", m);
		List<OMArgument> args = getArgumentsFromModification(m);
		if (args != null) {
			for (OMArgument arg : args) {	// transformation, iconTransformation...
				OMElementModification em = getElementModificationFromArgument(arg);
				String argName = "Placement" + "." + em.componentRef.name;
				OMModification m2 = em.modification;
				a.map.put(argName, m2);
				if (m2 != null) {
					List<OMArgument> args2 = getArgumentsFromModification(m2);
					if (args2 != null) {
						for (OMArgument arg2 : args2) {
							OMElementModification em2 = getElementModificationFromArgument(arg2);
							String argName2 = argName + "." + em2.componentRef.name;
							OMModification m3 = em2.modification;
							a.map.put(argName2, m3);
						}
					}
				}
			}
		}
	}
	
	public static void parseIcon(OMModification m, Annotation a, String name) {
		a.map.put(name, m);
		List<OMArgument> args = getArgumentsFromModification(m);
		if (args != null) {
			for (OMArgument arg : args) {	// coordinateSys, graphics...
				OMElementModification em = getElementModificationFromArgument(arg);
				String argName = name + "." + em.componentRef.name;
				OMModification m2 = em.modification;
				a.map.put(argName, m2);
				if (m2 != null) {
					List<OMArgument> args2 = getArgumentsFromModification(m2);
					if (args2 != null) {	// in coordinateSys
						for (OMArgument arg2 : args2) {
							OMElementModification em2 = getElementModificationFromArgument(arg2);
							String argName2 = argName + "." + em2.componentRef.name;
							OMModification m3 = em2.modification;
							a.map.put(argName2, m3);
						}
					} else if (m2.expression != null) { // in graphics
						// not to deal with expression in here
					}
				}
			}
		}
	}
	public static void parseDiagram(OMModification m, Annotation a,
			String name) {
		a.map.put(name, m);
		List<OMArgument> args = getArgumentsFromModification(m);
		if (args != null) {
			for (OMArgument arg : args) {	// coordinateSys, graphics...
				OMElementModification em = getElementModificationFromArgument(arg);
				String argName = name + "." + em.componentRef.name;
				OMModification m2 = em.modification;
				a.map.put(argName, m2);
				if (m2 != null) {
					List<OMArgument> args2 = getArgumentsFromModification(m2);
					if (args2 != null) {	// in coordinateSys
						for (OMArgument arg2 : args2) {
							OMElementModification em2 = getElementModificationFromArgument(arg2);
							String argName2 = argName + "." + em2.componentRef.name;
							OMModification m3 = em2.modification;
							a.map.put(argName2, m3);
						}
					} else if (m2.expression != null) { // in graphics
						// not to deal with expression in here
					}
				}
			}
		}
	}	
	
	public static List<OMArgument> getArgumentsFromModification(OMModification m) {
		if (m.classModification != null && m.classModification.argList != null) {
			OMArgumentList argList = m.classModification.argList;
			return argList.argList;
		}
		return null;
	}
	
	public static OMElementModification getElementModificationFromArgument(OMArgument arg) {
		if (arg.elementModificationOrReplaceable != null && 
			arg.elementModificationOrReplaceable.elementModification != null) {
			return arg.elementModificationOrReplaceable.elementModification;
		}
		return null;
	}
	
	public static OMExpression getExpressionFromElementModification(OMElementModification em) {
		if (em.modification != null && em.modification.expression != null) {
			return em.modification.expression;
		}
		return null;
	}

	public static class GraphicExpression {
		public String name;
		public List<NamedArgument> namedArguments;
		
		public GraphicExpression(String name, OMNamedArgument[] namedArguments) {
			this.name = name;
			this.namedArguments = new LinkedList<NamedArgument>();
			for (OMNamedArgument arg : namedArguments) {
				this.namedArguments.add(new NamedArgument(arg));
			}
		}
		
		/*
		 * parse the 'graphics={}' section in annotation.Icon or Diagram 
		 */
		public static List<GraphicExpression> parseGraphic(OMModification m) {
			if (m.expression == null)
				return new LinkedList<GraphicExpression>();
		
			String text = m.expression.toCode();
			return parseGraphic(text);
		}
		
		public static List<GraphicExpression> parseGraphic(String text) {
			ModelicaParser parser = new ModelicaParser(new StringReader(
					text));
			try {
				OMModification m = parser.modification();
				if (m.expression != null) {
					text = m.expression.toCode();
					parser = new ModelicaParser(new StringReader(text));
				}
			} catch (ParseException e) {}
			try {
				OMPrimary primary = parser.primary();
				List<GraphicExpression> res = new LinkedList<GraphicExpression>();

				OMFunctionArguments funcArgs = primary.funcArgs;
				while (funcArgs != null) {
					if (funcArgs.expression != null) {
						OMExpression exp = funcArgs.expression;
						if (exp.simpleExpression != null && exp.simpleExpression.logicalExpression != null && 
								exp.simpleExpression.logicalExpression.logicalTerms != null &&
								exp.simpleExpression.logicalExpression.logicalTerms.size() > 0 &&
								exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors != null &&
								exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors.size() > 0 &&
								exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors.get(0).relation != null &&
								exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors.get(0).relation.arithmeticExpression1 != null &&
								exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors.get(0).relation.arithmeticExpression1.term != null &&
								exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors.get(0).relation.arithmeticExpression1.term.factor != null && 
								exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors.get(0).relation.arithmeticExpression1.term.factor.primary != null) 
						{
							OMPrimary p = exp.simpleExpression.logicalExpression.logicalTerms.get(0).logicalFactors.get(0).relation.arithmeticExpression1.term.factor.primary;
							if (p.funcName != null && p.funcCallArgs != null && 
									p.funcCallArgs.funcArgs != null && p.funcCallArgs.funcArgs.namedArguments != null) 
							{
								res.add(new GraphicExpression(p.funcName, p.funcCallArgs.funcArgs.namedArguments));
							}
						} else {
							System.err.println("unknown annotation format for graphics: " + exp.toCode());
						}
					}
					funcArgs = funcArgs.next;
				}

				if (res.size() == 0) {
					System.out.println("warning: no arguments in annoation for graphics: " + primary.toCode());
				}
				return res;
			}
			catch (ParseException e1) {
				e1.printStackTrace();
			}
			return new LinkedList<GraphicExpression>();
		}
	}


}
