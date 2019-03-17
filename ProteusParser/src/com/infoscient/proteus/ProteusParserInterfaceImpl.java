package com.infoscient.proteus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.ModelicaLoader;
import com.infoscient.proteus.modelica.parser.ModelicaParser;
import com.infoscient.proteus.modelica.parser.ParseException;

public class ProteusParserInterfaceImpl implements ProteusParserInterface {

	private static final ProteusParserInterfaceImpl singleton = new ProteusParserInterfaceImpl();

	public static ProteusParserInterfaceImpl get() {
		return singleton;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.infoscient.proteus.ProteusParserInterface#paserMO(java.lang.String)
	 */
	public ClassDef[] paserMO(String template) {
		ModelicaParser parser = new ModelicaParser(new StringReader(template));
		ClassDef[] cds = null;
		try {
			cds = (ClassDef[]) parser.stored_definition().jjtAccept(
					new ModelicaLoader(), "");
		} catch (com.infoscient.proteus.modelica.parser.ParseException e) {
			e.printStackTrace();
		}
		return cds;
	}

	public static  String read(String filePath) throws IOException {
		FileReader fr = new FileReader(filePath);
		BufferedReader br = new BufferedReader(fr);
		String strLine = "";
		StringBuffer allLine = new StringBuffer();
		while ((strLine = br.readLine()) != null) {
			allLine.append(strLine + "\n");
		}
		return allLine.toString();
	}

	public  DefaultMutableTreeNode newreadLib(BufferedReader reader,
			DefaultMutableTreeNode root, ResourceGetter rg, String path) {
		try {
			return readLib(reader, root, rg, path, null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public  DefaultMutableTreeNode readLib(BufferedReader reader,
			DefaultMutableTreeNode root, ResourceGetter rg, String path,
			shortClassResolver scr) throws IOException, ParseException {
		ModelicaParser parser = new ModelicaParser(reader);
		try {
			ClassDef[] cds = (ClassDef[]) parser.stored_definition().jjtAccept(
					new ModelicaLoader(), path);
			// RefResolver.findRefs(cds);
			if (root != null) {
				root = buildTree(root, cds, rg);
			}
			return root;
		} catch (ParseException e) {
			e.printStackTrace();
			throw new ParseException("Invalid modelica file"
					+ "invalid modelica file");
		}
	}

	public static void main(String[] args) {
		String path = "C:\\tmp\\1.mo";
		try {
			ProteusParserInterfaceImpl ppi = new ProteusParserInterfaceImpl();
			String template = read(path);
			ClassDef[] cds = ppi.paserMO(template);
			System.err.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private  DefaultMutableTreeNode buildTree(
			DefaultMutableTreeNode root, ClassDef[] cds, ResourceGetter rg) {
		for (ClassDef cd : cds) {
			if (cd.type
					.equalsIgnoreCase("Modelica.Fluid.Pipes.BaseClasses.WallFriction.TestWallFrictionAndGravity")
					|| cd.type
							.equalsIgnoreCase("Modelica.Fluid.Examples.DrumBoiler")) {
				continue;
			}
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(cd);

			if (cd.childrenList.size() > 0) {
				// if (cd.elements != null) {
				List<ClassDef> ccdList = new LinkedList<ClassDef>();
				for (ClassDef childCd : cd.childrenList) {
					// for (Element el : cd.elements) {
					// if (el.classDef != null) {
					ccdList.add(childCd);
					// ccdList.add(el.classDef);
					// }
				}
				buildTree(node, ccdList.toArray(new ClassDef[0]), rg);
			}

			if (((String) cd.getName().get()).equalsIgnoreCase(root.toString())) {
				root = node;
			} else {
				root.add(node);
			}
		}
		return root;
	}
}
