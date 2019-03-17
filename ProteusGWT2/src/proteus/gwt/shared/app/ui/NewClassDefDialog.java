package proteus.gwt.shared.app.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proteus.gwt.shared.modelica.ClassDef;
import proteus.gwt.shared.modelica.ModelicaLoader;
import proteus.gwt.shared.modelica.Templates;
import proteus.gwt.shared.modelica.parser.ModelicaParser;
import proteus.gwt.shared.modelica.parser.OMClassDefinition;
import proteus.gwt.shared.util.Constants;
import proteus.gwt.shared.util.StringReader;

public class NewClassDefDialog {

	public static final String CLASS_PROP_FINAL = "Final",
			CLASS_PROP_PARTIAL = "Partial",
			CLASS_PROP_ENCAPSULATED = "Encapsulated";

	protected ClassDef _classDef;

	private SelectType selectType;

	private ConfClassDef confClassDef;

	private ConfAnnotation confAnnotation;

	private Map<String, Object> classPropsMap = new HashMap<String, Object>();

	public NewClassDefDialog() {
		// super(JOptionPane.getFrameForComponent(owner),
		// "New Class Definition");
		// getContentPane().setPreferredSize(new Dimension(550, 550));
		//
		// WizardPanel[] wizardPanels = new WizardPanel[] {
		// welcome = new Welcome(this),
		selectType = new SelectType(this);
		confClassDef = new ConfClassDef(this);
		// initComponents("New Class Definition", new ImageIcon(getClass()
		// .getResource("resources/new_class_def.png")), wizardPanels);
	}

	public Object getClassProperty(String key) {
		return classPropsMap.get(key);
	}

	public void setClassProperty(String key, Object value) {
		classPropsMap.put(key, value);
	}

	public String getSelectedType() {
		return selectType.getSelectedType();
	}

	public ClassDef getClassDef() {
		return confClassDef.getClassDef();
	}

	public void finish() {
		_classDef = getClassDef();
	}

	public ClassDef showDialog(String classType) {
		if (classType != null) {
			selectType.setSelectedType(classType);
			// setCurrentId(2);
		}
		// setVisible(true);
		return _classDef;
	}

	public ClassDef getDefaultClassDef(String classType) {
		if (classType != null) {
			selectType.setSelectedType(classType);
			confClassDef.doingNext();
		}
		// get classDef definition
		finish();
		// setVisible(true);
		return _classDef;
	}

	private static class SelectType {
		private List<String> typesList;
		private String selectedType;

		public SelectType(NewClassDefDialog newClassDefDialog) {
			typesList = new LinkedList<String>();

			String[] types = new String[] { Constants.TYPE_CLASS,
					Constants.TYPE_MODEL, Constants.TYPE_CONNECTOR,
					Constants.TYPE_RECORD, Constants.TYPE_BLOCK,
					Constants.TYPE_FUNCTION, Constants.TYPE_PACKAGE };
			for (String type : types) {
				typesList.add(type);
			}

		}

		public String getSelectedType() {
			return selectedType;
		}

		public void setSelectedType(String type) {
			if (typesList.contains(type.toLowerCase())) {
				this.selectedType = type.toLowerCase();
			} else {
				this.selectedType = Constants.TYPE_MODEL;
			}
		}

	}

	private static class ConfClassDef {

		private ClassDef classDef;

		private NewClassDefDialog ncdd;

		public ConfClassDef(NewClassDefDialog newClassDefDialog) {
			this.ncdd = newClassDefDialog;
		}

		public ClassDef getClassDef() {
			return classDef;
		}

		public void doingNext() {// WizardPanel previous) {
			// NewClassDefDialog ncdd = (NewClassDefDialog) wizard;
			String type = ncdd.getSelectedType();
			String template = Templates.MODEL;
			if (type.equals(Constants.TYPE_CLASS)) {
				template = Templates.CLASS;
			} else if (type.equals(Constants.TYPE_MODEL)) {
				template = Templates.MODEL;
			} else if (type.equals(Constants.TYPE_CONNECTOR)) {
				template = Templates.CONNECTOR;
			}
			ModelicaParser parser = new ModelicaParser(new StringReader(
					template));
			try {
				OMClassDefinition omcd = parser.class_definition();
				Object[] data = { "" };
				classDef = (ClassDef) omcd
						.jjtAccept(new ModelicaLoader(), data);
				// b = (Boolean) ncdd.getClassProperty(CLASS_PROP_FINAL);
				// if (b != null) {
				// classDef.final_.set(b);
				// classDef.final_.readonly = true;
				// }
				// b = (Boolean) ncdd.getClassProperty(CLASS_PROP_PARTIAL);
				// if (b != null) {
				// classDef.partial.set(b);
				// classDef.partial.readonly = true;
				// }
				// b = (Boolean) ncdd.getClassProperty(CLASS_PROP_ENCAPSULATED);
				// if (b != null) {
				// classDef.encapsulated.set(b);
				// classDef.encapsulated.readonly = true;
				// }
				// add(new
				// AutoPanel(PropertyFactory.createProperties(classDef)));
				// revalidate();
				// repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static class ConfAnnotation // extends WizardPanel
	{

		// @TextType(name = "Documentation")
		// public String documentation;
		//
		// @IntegerType(name = "Icon Width")
		// public int iconWidth;
		//
		// @IntegerType(name = "Icon Height")
		// public int iconHeight;
		//
		// public ConfAnnotation(Wizard w) {
		// super(w);
		// setLayout(new GridLayout(1, 2, 10, 10));
		// setDescription("Configure Annotation");
		//
		// JPanel propsPanel = new AutoPanel(PropertyFactory
		// .createProperties(this));
		// add(propsPanel);
		//
		// JPanel iconPanel = new JPanel(new BorderLayout(10, 10));
		// JButton editIcon = new JButton("Edit Icon");
		// editIcon.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// ImageEditor ie = new ImageEditor(wizard);
		// ie.showDialog(300, 300);
		// }
		// });
		// iconPanel.add(editIcon, BorderLayout.NORTH);
		// JPanel iconDrawing = new JPanel();
		// iconDrawing.setBorder(BorderFactory
		// .createBevelBorder(BevelBorder.LOWERED));
		// iconPanel.add(iconDrawing);
		// add(iconPanel);
		// }
		//
		// public boolean canNext() {
		// return true;
		// }
		//
		// public boolean canPrevious() {
		// return true;
		// }
	}
	//
	// public static void main(String[] args) throws Exception {
	// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	// NewClassDefDialog ncdd = new NewClassDefDialog(null);
	// ncdd.setClassProperty(CLASS_PROP_FINAL, true);
	// ClassDef cd = ncdd.showDialog(null);
	// }
}
