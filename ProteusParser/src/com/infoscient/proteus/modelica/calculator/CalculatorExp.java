package com.infoscient.proteus.modelica.calculator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Map;

import com.infoscient.proteus.modelica.DoubleRecord;

public class CalculatorExp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		c.demoVariables();
//		c.demoCalc();
		String text = "2*Modelica.Math.asin(1.0)+Modelica.Math.cos(1.0)-Modelica.Math.sin(1.0)";
		String text2 = "sqrt(1 - 0.0667)";
		simpleFunctions(text);
	}

	public static _Start simpleNumberCal(String line) {
		//calculate value 
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(line
					.getBytes("UTF-8"));
			Calculator parser = new Calculator(is);
			_Start v = parser.Start();
			v.jjtAccept(new CalculatorLoader(), v);
			return v;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void demoVariables() {
		try {

			BufferedReader br = new BufferedReader(new FileReader(
					"simplevariables.txt"));
			String line = null;
			while ((line = br.readLine()) != null) {
				ByteArrayInputStream is = new ByteArrayInputStream(line
						.getBytes("UTF-8"));
				Calculator parser = new Calculator(is);
				 _Variables v = parser.Variables();
				v.jjtAccept(new CalculatorLoader(), v);
				System.out.println(line);
				for(Map.Entry<String, DoubleRecord> e: v.varList.entrySet()){
					System.err.print(e.getKey()+"/");
				}
				if(v.varList.size()>0) System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void demoCalc() {
		try {

			BufferedReader br = new BufferedReader(new FileReader(
					"simplenumber.txt"));
			String line = null;
			while ((line = br.readLine()) != null) {
				ByteArrayInputStream is = new ByteArrayInputStream(line
						.getBytes("UTF-8"));
				Calculator parser = new Calculator(is);
				_Start node = parser.Start();
				node.jjtAccept(new CalculatorLoader(), node);
				System.err.println(line+"= ");
//				for (int i = 0; i < node.eList.size(); i++) {
//					_Expressions exps = node.eList.get(i);
//					for (int j = 0; j < exps.valueList.size(); j++) {
//						System.out.print(exps.valueList.get(j).val+" ");
//					}
//					System.out.println();
//				}
//				System.out.println(line+"=");
				for(Double d: node.doubleList){
					System.out.println(d.toString());
				}
				//			if(v.varList.size()>0) System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, DoubleRecord> simpleVariables(String text) {
		//extract variables
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(text
					.getBytes("UTF-8"));
			Calculator parser = new Calculator(is);
			_Variables v = parser.Variables();
			return v.varList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, DoubleRecord> simpleFunctions(String text){
		
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(text
					.getBytes("UTF-8"));
			Calculator parser = new Calculator(is);
			 _Functions v = parser.Functions();
			return v.varList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
