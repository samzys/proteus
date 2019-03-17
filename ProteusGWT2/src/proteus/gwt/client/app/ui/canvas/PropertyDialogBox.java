package proteus.gwt.client.app.ui.canvas;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.RefreshCodeCanvasEvent;
import proteus.gwt.client.app.event.RepaintCanvasEvent;
import proteus.gwt.shared.modelica.Component;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class PropertyDialogBox extends WindowBox {
	private String comment;
	private String path;
	private String declName;
	private Component comp;
	private List<ParameterProperty> parameterList;
	private ComponentCanvasItem cci;
	private Widget tbModifiers;

	public final static String woName = "35px", woCheckBox = "10px",
			woTextBox = "160px", woListBox = "50px", woMoreButton = "5px",
			woUnit = "15px", woQuantity = "220px", woButton = "70px";

	public final static String MechanicsRotation = "Modelica.Mechanics.Rotational",
			MechanicsTranslational = "Modelica.Mechanics.Translational";

	public final static int VALUE = 0, START = 1, FIXED = 2, MODIFIER = 3;

	public final static int DialogWidth = 600, DialogHeight = 500;

	public static final Set<String> tabSet = new HashSet<String>();
	public static final Set<String> groupSet = new HashSet<String>();
	static {
		tabSet.add("General");
		tabSet.add("Defaults");
		tabSet.add("Initialization");
		tabSet.add("Animation");
		tabSet.add("Advanced");
		tabSet.add("Assumptions");

		groupSet.add("if animation = true");
		groupSet.add("Dynamics");
		groupSet.add("Environment");
		groupSet.add("Initialization");
		groupSet.add("Advanced");
	}

	public PropertyDialogBox(ComponentCanvasItem cci) {
		this.cci = cci;
		this.comp = cci.getComponent();
		this.path = comp.getCompData().getWholeName();
		this.declName = cci.getName();
		this.comment = comp.getCompData().getComment();
		this.parameterList = comp.getParameterList();

		setText("Properties");
		setSize("", "");

		DockLayoutPanel dockLayoutPanel = new DockLayoutPanel(Unit.PX);
		setWidget(dockLayoutPanel);
		dockLayoutPanel.setSize(DialogWidth + "px", DialogHeight + "px");

		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		dockLayoutPanel.addSouth(horizontalPanel, 30);
		horizontalPanel.setSize("100%", "25px");

		Button btnOk = new Button("OK");
		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				commit();
			}
		});
		horizontalPanel.add(btnOk);
		horizontalPanel.setCellHorizontalAlignment(btnOk,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnOk.setWidth(woButton);

		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		horizontalPanel.add(btnCancel);
		horizontalPanel.setCellHorizontalAlignment(btnCancel,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnCancel.setWidth(woButton);

		DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
		dockLayoutPanel.add(decoratedTabPanel);
		decoratedTabPanel.setSize("99%", 0.95 * DialogHeight + "px");

		VerticalPanel General = createVerticalPanel();
		decoratedTabPanel.add(General, "General", false);

		CaptionPanel cptnpnlComponent = new CaptionPanel("Component");
		cptnpnlComponent.setSize("98%", "100%");
		General.add(cptnpnlComponent);

		FlexTable layout = createFlexTable();

		cptnpnlComponent.setContentWidget(layout);
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
		for (int i = 0; i < 1; i++) {
			cellFormatter.setWidth(i, 0, woName);
		}
		// Add a title to the form
		// Add some standard form options
		layout.setHTML(0, 0, "Name: ");
		layout.setWidget(0, 1, new Label(declName));
		layout.setHTML(1, 0, "Comment: ");
		layout.setWidget(1, 1, new Label(comment));
		layout.setHTML(2, 0, "Path: ");
		layout.setWidget(2, 1, new Label(path));

		ScrollPanel scrollPanel = new ScrollPanel();
		General.add(scrollPanel);
		scrollPanel.setSize("98%", "320px");

		VerticalPanel paraPanel = new VerticalPanel();
		scrollPanel.setWidget(paraPanel);

		// if (path.startsWith(MechanicsRotation)) {
		// TranslationalPanel(paraPanel, decoratedTabPanel);
		// } else if (path.startsWith(MechanicsTranslational)) {
		TranslationalPanel(paraPanel, decoratedTabPanel);
		// } else {
		// OtherPanel(paraPanel);
		// }

		// add modifiers panel
		VerticalPanel vpmodifier = createVerticalPanel();
		VerticalPanel addModifiers = new VerticalPanel();
		addModifiers.setSize("98%", "3%");
		addModifiers.add(new Label(
				"Add new modifiers here. e.g., phi(start=1), w(start=2)"));
		tbModifiers = getWidget(null, MODIFIER);
		addModifiers.add(tbModifiers);
		vpmodifier.add(addModifiers);

		decoratedTabPanel.add(vpmodifier, "Add modifiers", false);

		decoratedTabPanel.selectTab(0);
	}

	private VerticalPanel createVerticalPanel() {
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSize("98%", 0.85 * DialogHeight + "px");
		return verticalPanel;
	}

	private void TranslationalPanel(VerticalPanel defaultTabPanel,
			DecoratedTabPanel decoratedTabPanel) {
		Map<String, FlexTable> flexTableMap = new HashMap<String, FlexTable>();
		Map<String, VerticalPanel> tabMap = new HashMap<String, VerticalPanel>();
		Map<String, FlexTable> generalFlexTableMap = new HashMap<String, FlexTable>();

		int paraCount = 0;
		for (final ParameterProperty para : parameterList) {
			String pname = para.getName();
			String grpAnnotation = para.getAnnotationGroup();
			String tabAnnotation = para.getAnnotationTab();
			boolean paraTag = false, initStartTag = false, initDialogTag = false;
			paraTag = isParaTag(para);
			initStartTag = isInitStartTag(para);
			initDialogTag = isInitDialogTag(para);

			if (!paraTag && !initStartTag && grpAnnotation == null
					&& tabAnnotation == null)
				continue;

			final AdvPropertyDialogBox paraAttribute = new AdvPropertyDialogBox(
					para);

			Widget widget = null;

			if (tabAnnotation == null || tabAnnotation.equals("")
					|| tabAnnotation.equals("General")
					|| tabAnnotation.equals("Defaults")) {

				if (grpAnnotation == null || grpAnnotation.equals("")) {
					if (initStartTag)
						grpAnnotation = "Initialization";
					else
						grpAnnotation = "Parameter";
				}
				grpAnnotation = removeQuotationMark(grpAnnotation);

				FlexTable ftCurrent = getGeneralFlexTable(grpAnnotation,
						generalFlexTableMap);
				int row = ftCurrent.getRowCount();

				if (initStartTag || initDialogTag) {
					ftCurrent.setHTML(row, 0, pname + ".start");
					widget = getWidget(para, FIXED);
					ftCurrent.setWidget(row, 1, widget);
					widget = getWidget(para, START);
					ftCurrent.setWidget(row, 2, widget);
				} else {
					ftCurrent.setHTML(row, 0, pname);
					widget = getWidget(para, VALUE);
					ftCurrent.setWidget(row, 2, widget);
				}

				Button btnMore = new Button();
				btnMore.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						para.parameter2Attribute();
						paraAttribute.center();
					}
				});
				ftCurrent.setWidget(row, 3, btnMore);
				ftCurrent.setHTML(row, 4, para.getUnit());
				String desc = para.getQuantity();
				if (desc == null) {
					desc = para.getDesc();
				}
				ftCurrent.setHTML(row, 5, desc);

				CellFormatter ftCurrentCellFormat = ftCurrent
						.getCellFormatter();
				ftCurrentCellFormat.setWidth(row, 0, woName);
				ftCurrentCellFormat.setWidth(row, 1, woCheckBox);
				ftCurrentCellFormat.setWidth(row, 2, woTextBox);
				ftCurrentCellFormat.setWidth(row, 3, woMoreButton);
				ftCurrentCellFormat.setWidth(row, 4, woUnit);
				ftCurrentCellFormat.setWidth(row, 5, woQuantity);

			} else {
				tabAnnotation = removeQuotationMark(tabAnnotation);
				getVerticalTabPanel(tabAnnotation, flexTableMap, tabMap);
				FlexTable ft = flexTableMap.get(tabAnnotation);
				int row = ft.getRowCount();

				if (initStartTag || initDialogTag) {
					ft.setHTML(row, 0, pname + ".start");
					widget = getWidget(para, FIXED);
					ft.setWidget(row, 1, widget);
					widget = getWidget(para, START);
					ft.setWidget(row, 2, widget);
				} else {
					ft.setHTML(row, 0, pname);
					widget = getWidget(para, VALUE);
					ft.setWidget(row, 2, widget);
				}

				Button btnMore = new Button();
				btnMore.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						paraAttribute.center();
					}
				});

				ft.setWidget(row, 3, btnMore);
				ft.setHTML(row, 4, para.getUnit());
				String desc = para.getQuantity();
				if (desc == null) {
					desc = para.getDesc();
				}
				ft.setHTML(row, 5, desc);
				CellFormatter ftCellFormat = ft.getCellFormatter();
				ftCellFormat.setWidth(paraCount, 0, woName);
				ftCellFormat.setWidth(paraCount, 1, woCheckBox);
				ftCellFormat.setWidth(paraCount, 2, woTextBox);
				ftCellFormat.setWidth(paraCount, 3, woMoreButton);
				ftCellFormat.setWidth(paraCount, 4, woUnit);
				ftCellFormat.setWidth(paraCount, 5, woQuantity);
			}
		}// for map

		String paraTable = "Parameter";
		FlexTable paraFlexTable = generalFlexTableMap.get("Parameter");
		if (paraFlexTable != null) {
			CaptionPanel cp = new CaptionPanel(paraTable);
			cp.setContentWidget(paraFlexTable);
			cp.setWidth("550px");
			defaultTabPanel.add(cp);
		}

		for (String key : generalFlexTableMap.keySet()) {
			if (key.equals(paraTable))
				continue;
			CaptionPanel cp = new CaptionPanel(key);
			cp.setContentWidget(generalFlexTableMap.get(key));
			cp.setWidth("550px");
			defaultTabPanel.add(cp);
		}

		for (String key : tabMap.keySet()) {
			CaptionPanel cp = new CaptionPanel();
			cp.setContentWidget(flexTableMap.get(key));
			cp.setWidth("550px");
			VerticalPanel verticalPanel = tabMap.get(key);
			ScrollPanel sp = new ScrollPanel();
			sp.setSize("98%", "420px");
			sp.setWidget(cp);
			verticalPanel.add(sp);
			String tabName = key;
			tabName = removeQuotationMark(key);

			decoratedTabPanel.add(verticalPanel, tabName, false);
		}
	}

	private String removeQuotationMark(String key) {
		if (key.startsWith("'") || key.startsWith("\"")) {
			key = key.substring(1, key.length() - 1);
		}
		return key;
	}

	private FlexTable getGeneralFlexTable(String grpAnnotation,
			Map<String, FlexTable> generalFlexTableMap) {
		FlexTable ft = generalFlexTableMap.get(grpAnnotation);
		if (ft == null) {
			ft = createFlexTable();
			generalFlexTableMap.put(grpAnnotation, ft);
		}
		return ft;
	}

	private VerticalPanel getVerticalTabPanel(String tabAnnotation,
			Map<String, FlexTable> flexTableMap,
			Map<String, VerticalPanel> tabMap) {
		VerticalPanel v = tabMap.get(tabAnnotation);
		if (v == null) {
			v = createVerticalPanel();
			FlexTable flexTable = createFlexTable();
			tabMap.put(tabAnnotation, v);
			flexTableMap.put(tabAnnotation, flexTable);
		}
		return v;
	}

	private FlexTable createFlexTable() {
		FlexTable initFlexTable = new FlexTable();
		initFlexTable.setWidth("98%");
		initFlexTable.setCellSpacing(6);
		return initFlexTable;
	}

	private void RotationalPanel(VerticalPanel paraPanel) {
		FlexTable paraFlexTable = new FlexTable();
		paraFlexTable.setWidth("98%");
		paraFlexTable.setCellSpacing(6);
		CellFormatter paraCellFormat = paraFlexTable.getCellFormatter();

		FlexTable initFlexTable = new FlexTable();
		initFlexTable.setWidth("98%");
		initFlexTable.setCellSpacing(6);
		CellFormatter initCellFormat = initFlexTable.getCellFormatter();

		int paraCount = 0, initparaCount = 0;
		for (final ParameterProperty para : parameterList) {
			boolean paraTag = false, initStartTag = false, initGroupTag = false, enumTag = false;
			initGroupTag = isInitDialogTag(para);
			paraTag = isParaTag(para);
			initStartTag = isInitStartTag(para);

			if (!paraTag && !initStartTag && !initGroupTag)
				continue;

			final AdvPropertyDialogBox paraAttribute = new AdvPropertyDialogBox(
					para);
			Widget widget = null;
			if (initStartTag || initGroupTag) {
				String pname = para.getName();
				if (initStartTag) {
					initFlexTable.setHTML(initparaCount, 0, pname + ".start");
					widget = getWidget(para, FIXED);
					initFlexTable.setWidget(initparaCount, 1, widget);
					widget = getWidget(para, START);
					initFlexTable.setWidget(initparaCount, 2, widget);
				} else {
					initFlexTable.setHTML(initparaCount, 0, pname);
					// widget = getWidget(para, FIXED);
					// initFlexTable.setWidget(initparaCount, 1, widget);
					widget = getWidget(para, VALUE);
					initFlexTable.setWidget(initparaCount, 2, widget);
				}
				Button btnMore = new Button();
				btnMore.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						para.parameter2Attribute();
						paraAttribute.center();
					}
				});
				initFlexTable.setWidget(initparaCount, 3, btnMore);
				initFlexTable.setHTML(initparaCount, 4, para.getUnit());
				String desc = para.getQuantity();
				if (desc == null) {
					desc = para.getDesc();
				}
				initFlexTable.setHTML(initparaCount, 5, desc);
				initCellFormat.setWidth(initparaCount, 0, woName);
				initCellFormat.setWidth(paraCount, 1, woCheckBox);
				initCellFormat.setWidth(initparaCount, 2, woTextBox);
				paraCellFormat.setWidth(paraCount, 3, woMoreButton);
				initCellFormat.setWidth(initparaCount, 4, woUnit);
				initCellFormat.setWidth(initparaCount, 5, woQuantity);
				initparaCount++;
			} else if (paraTag) {
				String paraName = para.getName();
				paraFlexTable.setHTML(paraCount, 0, paraName);
				widget = getWidget(para, VALUE);
				paraFlexTable.setWidget(paraCount, 1, widget);
				Button btnMore = new Button();
				btnMore.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						paraAttribute.center();
					}
				});

				paraFlexTable.setWidget(paraCount, 2, btnMore);
				paraFlexTable.setHTML(paraCount, 3, para.getUnit());
				String desc = para.getQuantity();
				if (desc == null) {
					desc = para.getDesc();
				}
				paraFlexTable.setHTML(paraCount, 4, desc);
				paraCellFormat.setWidth(paraCount, 0, woName);
				paraCellFormat.setWidth(paraCount, 1, woTextBox);
				paraCellFormat.setWidth(paraCount, 2, woMoreButton);
				paraCellFormat.setWidth(paraCount, 3, woUnit);
				paraCellFormat.setWidth(paraCount, 4, woQuantity);
				paraCount++;
			}
		}

		if (paraCount > 0) {
			CaptionPanel cptnpnlParameters = new CaptionPanel("Parameters");
			paraPanel.add(cptnpnlParameters);
			cptnpnlParameters.setWidth("550px");
			cptnpnlParameters.setContentWidget(paraFlexTable);
		}

		if (initparaCount > 0) {
			CaptionPanel cptnpnlIniti = new CaptionPanel("Initialization");
			paraPanel.add(cptnpnlIniti);
			cptnpnlIniti.setWidth("550px");
			cptnpnlIniti.setContentWidget(initFlexTable);
		}
	}

	private void OtherPanel(VerticalPanel paraPanel) {
		FlexTable paraFlexTable = new FlexTable();
		paraFlexTable.setWidth("98%");
		paraFlexTable.setCellSpacing(6);
		CellFormatter paraCellFormat = paraFlexTable.getCellFormatter();

		FlexTable initFlexTable = new FlexTable();
		initFlexTable.setWidth("98%");
		initFlexTable.setCellSpacing(6);
		CellFormatter initCellFormat = initFlexTable.getCellFormatter();

		int paraCount = 0, initparaCount = 0;
		for (final ParameterProperty para : parameterList) {
			boolean paraTag = false, initStartTag = false, initGroupTag = false, enumTag = false;
			initGroupTag = isInitDialogTag(para);
			paraTag = isParaTag(para);
			initStartTag = isInitStartTag(para);

			if (!paraTag && !initStartTag && !initGroupTag)
				continue;

			final AdvPropertyDialogBox paraAttribute = new AdvPropertyDialogBox(
					para);
			Widget widget = null;
			if (initStartTag || initGroupTag) {
				String pname = para.getName();
				if (initStartTag) {
					initFlexTable.setHTML(initparaCount, 0, pname + ".start");
					widget = getWidget(para, FIXED);
					initFlexTable.setWidget(initparaCount, 1, widget);
					widget = getWidget(para, START);
					initFlexTable.setWidget(initparaCount, 2, widget);
				} else {
					initFlexTable.setHTML(initparaCount, 0, pname);
					// widget = getWidget(para, FIXED);
					// initFlexTable.setWidget(initparaCount, 1, widget);
					widget = getWidget(para, VALUE);
					initFlexTable.setWidget(initparaCount, 2, widget);
				}
				Button btnMore = new Button();
				btnMore.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						para.parameter2Attribute();
						paraAttribute.center();
					}
				});
				initFlexTable.setWidget(initparaCount, 3, btnMore);
				initFlexTable.setHTML(initparaCount, 4, para.getUnit());
				String desc = para.getQuantity();
				if (desc == null) {
					desc = para.getDesc();
				}
				initFlexTable.setHTML(initparaCount, 5, desc);
				initCellFormat.setWidth(initparaCount, 0, woName);
				initCellFormat.setWidth(paraCount, 1, woCheckBox);
				initCellFormat.setWidth(initparaCount, 2, woTextBox);
				paraCellFormat.setWidth(paraCount, 3, woMoreButton);
				initCellFormat.setWidth(initparaCount, 4, woUnit);
				initCellFormat.setWidth(initparaCount, 5, woQuantity);
				initparaCount++;
			} else if (paraTag) {
				String paraName = para.getName();
				paraFlexTable.setHTML(paraCount, 0, paraName);
				widget = getWidget(para, VALUE);
				paraFlexTable.setWidget(paraCount, 1, widget);
				Button btnMore = new Button();
				btnMore.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						paraAttribute.center();
					}
				});

				paraFlexTable.setWidget(paraCount, 2, btnMore);
				paraFlexTable.setHTML(paraCount, 3, para.getUnit());
				String desc = para.getQuantity();
				if (desc == null) {
					desc = para.getDesc();
				}
				paraFlexTable.setHTML(paraCount, 4, desc);
				paraCellFormat.setWidth(paraCount, 0, woName);
				paraCellFormat.setWidth(paraCount, 1, woTextBox);
				paraCellFormat.setWidth(paraCount, 2, woMoreButton);
				paraCellFormat.setWidth(paraCount, 3, woUnit);
				paraCellFormat.setWidth(paraCount, 4, woQuantity);
				paraCount++;
			}
		}

		if (paraCount > 0) {
			CaptionPanel cptnpnlParameters = new CaptionPanel("Parameters");
			paraPanel.add(cptnpnlParameters);
			cptnpnlParameters.setWidth("550px");
			cptnpnlParameters.setContentWidget(paraFlexTable);
		}

		if (initparaCount > 0) {
			CaptionPanel cptnpnlIniti = new CaptionPanel("Initialization");
			paraPanel.add(cptnpnlIniti);
			cptnpnlIniti.setWidth("550px");
			cptnpnlIniti.setContentWidget(initFlexTable);
		}

	}

	protected void commit() {
		hide();
		StringBuilder modStr = new StringBuilder();
		modStr.append("(");
		int i = 0;
		
		String newVal = ((MyTextBox) tbModifiers).getInputValue();
		if (newVal != null && !newVal.equals("")) {
			String newStr = CheckCompatibility(newVal);
			modStr.append(newStr);
			i++;
		}
		
		for (ParameterProperty para : parameterList) {
			para.parameter2Attribute();
			String s = para.commitChange();
			if (s != null) {
				if (i > 0)
					modStr.append(",");
				modStr.append(s);
				i++;
			}
		}
		modStr.append(")");
		System.err.println(modStr.toString());
		cci.setParaModifier(modStr.toString());

		ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
		ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
	}

	private String CheckCompatibility(String newVal) {
		//checking.....
		//finish checking...
		return newVal;
	}

	private Widget getWidget(ParameterProperty para, int widgetType) {
		Widget widget = null;
		if (widgetType == FIXED) {
			MyCheckBox checkBox = new MyCheckBox();
			checkBox.setInputValue(para.getFixed());
			para.setParaFixed(checkBox);
			widget = checkBox;
		} else if (widgetType == VALUE) {
			if (para.getType() != null
					&& (para.getType().equals("Boolean")
							|| para.getType().equals("enumeration") || para
							.getType().equals("StateSelect"))) {
				MyListBox listBox;
				if (para.getType().equals("Boolean")) {
					listBox = new MyListBox(MyListBox.BOOLEAN, "");
				} else if (para.getType().equals("StateSelect")) {
					listBox = new MyListBox(MyListBox.STATESELECT, "");
				} else {
					listBox = new MyListBox(para.getEnumList());
				}
				listBox.setWidth(AdvPropertyDialogBox.InputFieldWidth + 6 + "px");
				listBox.setInputValue(para.getValue());
				para.setParaValue(listBox);
				widget = listBox;
			} else {
				MyTextBox textbox = new MyTextBox();
				textbox.setAlignment(TextAlignment.RIGHT);
				textbox.setWidth(AdvPropertyDialogBox.InputFieldWidth + "px");
				textbox.setInputValue(para.getValue());

				para.setParaValue(textbox);
				widget = textbox;
			}
		} else if (widgetType == START) {
			if (para.getType() != null
					&& (para.getType().equals("Boolean") || para.getType()
							.equals("enumeration"))) {
				MyListBox listBox;
				if (para.getType().equals("Boolean")) {
					listBox = new MyListBox(MyListBox.BOOLEAN, "");
				} else if (para.getType().equals("StateSelect")) {
					listBox = new MyListBox(MyListBox.STATESELECT, "");
				} else {
					listBox = new MyListBox(para.getEnumList());
				}
				listBox.setWidth(AdvPropertyDialogBox.InputFieldWidth + 6 + "px");
				listBox.setInputValue(para.getStart());
				para.setParaStart(listBox);
				widget = listBox;
			} else {
				MyTextBox textbox = new MyTextBox();
				textbox.setAlignment(TextAlignment.RIGHT);
				textbox.setWidth(AdvPropertyDialogBox.InputFieldWidth + "px");
				textbox.setInputValue(para.getStart());
				para.setParaStart(textbox);
				widget = textbox;
			}
		} else if (widgetType == MODIFIER) {
			MyTextBox textbox = new MyTextBox();
			textbox.setAlignment(TextAlignment.LEFT);
			textbox.setWidth("98%");

			widget = textbox;
		}
		return widget;
	}

	private boolean isInitStartTag(ParameterProperty para) {
		if ((!para.isHeader() && para.getStart() != null)) {
			return true;
		}
		return false;
	}

	private boolean isParaTag(ParameterProperty para) {
		if (para.isHeader()) {
			return true;
		}
		return false;
	}

	private boolean isInitDialogTag(ParameterProperty para) {
		String initAnnotation = para.getAnnotationInitDialog();
		if (initAnnotation != null && initAnnotation.contains("true")) {
			return true;
		}
		return false;
	}

}
