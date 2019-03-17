package com.infoscient.proteus;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import com.infoscient.proteus.modelica.ClassDef;
import com.infoscient.proteus.modelica.Component;
import com.infoscient.proteus.modelica.RefResolver;

public class LocalUA  {
	/**
	 * @throws Exception
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TreeNode libRoot;


	public DefaultMutableTreeNode getLibTreeNode() {
		return readObject();
	}

	public TreeNode buildLibrary() {
		if (libRoot == null) {
			try {
				libRoot = buildLibrary(createResources(getClass()
						.getResourceAsStream("/Library.resources")));
				RefResolver.resolveRefs(libRoot);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return libRoot;
	}

	private Resource createResources(InputStream in) throws Exception {
		BufferedReader r = new BufferedReader(new InputStreamReader(in));
		String line;
		Resource root = null;
		Map<String, Resource> resMap = new HashMap<String, Resource>();
		root = new Resource("Library", true);
		resMap.put("/Library", root);
		while ((line = r.readLine()) != null) {
			boolean isDirectory = line.endsWith(PACKAGE_FILE);
			int index;
			String name, parentName;
			if (isDirectory) {
				line = line.substring(1);
				int index0 = line.lastIndexOf("/");
				index = (line.substring(0, index0)).lastIndexOf("/");
				name = line.substring(index + 1, index0);
				parentName = line.substring(0, index);
				line = line.substring(0, index0);
			} else {
				line = line.substring(1);
				index = line.lastIndexOf("/");
				name = line.substring(index + 1);
				parentName = line.substring(0, index);
			}

			Resource res = new Resource(name, isDirectory);
			if (root == null) {
				root = res;
			} else {
				// System.out.println(line);
				Resource parent = resMap.get(parentName);
				if (parent == null) {
					throw new Exception("Parent not found");
				}
				parent.add(res);
			}
			resMap.put(line, res);
		}
		return root;
	}

	// private Resource oldcreateResources(InputStream in) throws Exception {
	// BufferedReader r = new BufferedReader(new InputStreamReader(in));
	// String line;
	// Resource root = null;
	// Map<String, Resource> resMap = new HashMap<String, Resource>();
	// while ((line = r.readLine()) != null) {
	// boolean isDirectory = line.charAt(0) == 'D';
	// line = line.substring(1);
	// int index = line.lastIndexOf("/");
	// String name = line.substring(index + 1), parentName = line
	// .substring(0, index);
	//
	// Resource res = new Resource(name, isDirectory);
	// if (root == null) {
	// root = res;
	// } else {
	// Resource parent = resMap.get(parentName);
	// if (parent == null) {
	// throw new Exception("Parent not found");
	// }
	// parent.add(res);
	// }
	// resMap.put(line, res);
	// }
	// return root;
	// }

	private static class Resource implements Serializable {
		private final String name;

		private final boolean isDirectory;

		private final List<Resource> children = new LinkedList<Resource>();

		private Resource parent;

		public Resource(String name, boolean isDirectory) {
			this.name = name;
			this.isDirectory = isDirectory;
		}

		public String getName() {
			return name;
		}

		public boolean isDirectory() {
			return isDirectory;
		}

		public Resource getParent() {
			return parent;
		}

		public void setParent(Resource parent) {
			this.parent = parent;
		}

		public void add(Resource r) {
			r.setParent(this);
			children.add(r);
		}

		public Resource[] listFiles() {
			return children.toArray(new Resource[0]);
		}

		public String getAbsolutePath() {
			return parent == null ? "/" + name : parent.getAbsolutePath() + "/"
					+ name;
		}
	}

	private static final String PACKAGE_FILE = "package.mo";

	private MutableTreeNode buildLibrary(Resource dir) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(dir.getName());

		String[] ignoreList = { "icons", ".svn" };
		Resource[] files = dir.listFiles();
		String f = dir.getAbsolutePath() + "/package.mo";

		try {
			root = ProteusParserInterfaceImpl.get().newreadLib(new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream(f))),
					root, null, f);
		} catch (Exception e) {
			System.err.println("In File: " + f);
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		FILE_LOOP: for (int i = 0; i < files.length; i++) {
			final Resource file = files[i];
			for (String item : ignoreList) {
				if (file.getName().equals(item)) {
					continue FILE_LOOP;
				}
			}
			if (file.isDirectory()) {
				root.add(buildLibrary(file));
			} else {
				String name = file.getName();
				if (!name.equals(PACKAGE_FILE) && name.endsWith(".mo")) {
					// deal with the other mo files (except package.mo) under
					// current directory
					// ClassDef[]
					// cds = null;
					try {
						// System.out.println(file.getAbsolutePath());
						// f = file.getAbsolutePath();
						root = ProteusParserInterfaceImpl.get().newreadLib(new BufferedReader(
								new InputStreamReader(getClass()
										.getResourceAsStream(
												file.getAbsolutePath()))),
								root, null, file.getAbsolutePath());
					} catch (Exception e) {
						System.err.println("In File: " + file.getName());
						System.err.println(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		//		RefResolver.resolveRefs();
		return root;
	}

	public DefaultMutableTreeNode buildStaticLibrary(Resource dir) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(dir.getName());

		String[] ignoreList = { "icons", ".svn" };
		Resource[] files = dir.listFiles();
		String f = dir.getAbsolutePath() + "/package.mo";

		try {
			root = ProteusParserInterfaceImpl.get().newreadLib(new BufferedReader(
					new InputStreamReader(getClass().getResourceAsStream(f))),
					root, null, f);
		} catch (Exception e) {
			System.err.println("In File: " + f);
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		FILE_LOOP: for (int i = 0; i < files.length; i++) {
			final Resource file = files[i];
			for (String item : ignoreList) {
				if (file.getName().equals(item)) {
					continue FILE_LOOP;
				}
			}
			if (file.isDirectory()) {
				root.add(buildLibrary(file));
			} else {
				String name = file.getName();
				if (!name.equals(PACKAGE_FILE) && name.endsWith(".mo")) {
					// deal with the other mo files (except package.mo) under
					// current directory
					// ClassDef[]
					// cds = null;
					try {
						// System.out.println(file.getAbsolutePath());
						// f = file.getAbsolutePath();
						root = ProteusParserInterfaceImpl.get().newreadLib(new BufferedReader(
								new InputStreamReader(getClass()
										.getResourceAsStream(
												file.getAbsolutePath()))),
								root, null, file.getAbsolutePath());
					} catch (Exception e) {
						System.err.println("In File: " + file.getName());
						System.err.println(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		return root;
	}

	public DefaultMutableTreeNode writeO() {
		try {
			return buildStaticLibrary((createResources(getClass()
					.getResourceAsStream("/Library.resources"))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void writeObject() {
		try {
			DefaultMutableTreeNode node = buildStaticLibrary((createResources(getClass()
					.getResourceAsStream("/Library.resources"))));

			// Write to disk with FileOutputStream
			FileOutputStream f_out = new FileOutputStream("LibRoot.data");

			// Write object with ObjectOutputStream
			ObjectOutputStream obj_out = new ObjectOutputStream(f_out);

			// Write object out to disk
			obj_out.writeObject(node);
			obj_out.close();
			f_out.close();

			FileInputStream f_in = new FileInputStream("LibRoot.data");

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream(f_in);

			// Read an object
			Object obj = null;

			obj = obj_in.readObject();
			obj_in.close();
			f_in.close();

			// ByteArrayOutputStream memOut = new ByteArrayOutputStream();
			// ObjectOutputStream os = new ObjectOutputStream(memOut);
			// os.writeObject(node);
			//			          
			// ByteArrayInputStream memIn = new
			// ByteArrayInputStream(memOut.toByteArray());
			// ObjectInputStream is = new ObjectInputStream(memIn);
			// Object obj = is.readObject();
			// if(obj!=null){
			// System.out.println("");
			// }
		} catch (Exception e) {
		}
	}

	public DefaultMutableTreeNode readObject() {
		try {

			// Read from disk using FileInputStream
			FileInputStream f_in = new FileInputStream("LibRoot.data");

			// Read object using ObjectInputStream
			ObjectInputStream obj_in = new ObjectInputStream(f_in);

			// Read an object
			Object obj = null;
			obj = obj_in.readObject();

			obj_in.close();
			f_in.close();

			if (obj instanceof DefaultMutableTreeNode) {
				// Cast object to a Vector
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) obj;
				// System.out.println("ua"+ua.toString());
				return node;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	private void printTree(TreeNode root, String indent) {
		System.out.println(indent + root);
		for (int i = 0; i < root.getChildCount(); i++) {
			printTree(root.getChildAt(i), indent + "\t");
		}
	}

//	private ResourceGetter createRG(final File file) {
//		return new ResourceGetter() {
//			public byte[] getResource(String name) {
//				try {
//					String filename = file.getParent() + "/" + name;
//					return IOUtils.readFully(new FileInputStream(filename));
//				} catch (Exception e) {
//					// ignore
//				}
//				return null;
//			}
//		};
//	}

	public static void main(String[] args) throws Exception {
		System.out.println("Build library started");
		new LocalUA().buildLibrary();
		System.out.println("Build library finished");

		ClassDef cd = RefResolver
				.forName("Modelica.Electrical.Analog.Interfaces.OnePort");//Basic.Capacitor");;;
		
		Component comp = (Component) cd;

//		for (ComponentClause c : comp.compList) {
////			System.out.println(c.typeName.get());
//			for(ComponentDecl decl: c.declList) {
//				System.out.println(decl.comment.annotation.getObjectModel().toCode());
//			}
//		}
	}

}
