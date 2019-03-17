package proteus.gwt.client.app.ui.canvas;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;

public class AttributeDialogBox extends WindowBox {

	private static String woButton = "70px", woName = "50px";
	public final static int DialogWidth = 300, DialogHeight = 480;
	private CaptionRadioGroup varibilityGrp;
	private CaptionRadioGroup typingGrp;
	private CaptionRadioGroup causalityGrp;
	private CaptionCheckBoxGroup propertyGrp;
	private ArrayList<FieldInterface> fieldList;
	private ComponentCanvasItem cci;

	public AttributeDialogBox(ComponentCanvasItem cci) {
		setText("change attributes");
		setSize("", "");

		this.cci = cci;

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
				hide();
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
				cancel();
			}
		});
		horizontalPanel.add(btnCancel);
		horizontalPanel.setCellHorizontalAlignment(btnCancel,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnCancel.setWidth(woButton);

		VerticalPanel general = new VerticalPanel();
		general.setSize("98%", 0.95 * DialogHeight + "px");
		dockLayoutPanel.add(general);

		// add five fields: variability of component, properties, constraining
		// clause, dynamic typing, causality
		String rbOptions[] = { "constant", "parameter", "discrete",
				"Unspecified" };
		varibilityGrp = new CaptionRadioGroup("Variability of component",
				"varibility", rbOptions, "Unspecified");
		CaptionPanel cptnpnlVaribility = varibilityGrp
				.getCaptionPanelRadioGroup();
		general.add(cptnpnlVaribility);

		String[] ckOptions = { "Final", "Protected", "Replaceable" };
		propertyGrp = new CaptionCheckBoxGroup("Properties", "properties",
				ckOptions);
		CaptionPanel cptnpnlProperties = propertyGrp.getProperties();
		general.add(cptnpnlProperties);

		// CaptionPanel cptnpnlConstraining = getConstraining();
		// general.add(cptnpnlConstraining);

		String[] rbTyping = { "Normal", "Inner", "Outer" };
		typingGrp = new CaptionRadioGroup("Dynamic typing", "typing", rbTyping,
				"Normal");
		CaptionPanel cptnpnlDynamicTyping = typingGrp
				.getCaptionPanelRadioGroup();
		general.add(cptnpnlDynamicTyping);

		String rbCausality[] = { "None", "Input", "Output" };
		causalityGrp = new CaptionRadioGroup("Causality", "causality",
				rbCausality, "None");
		CaptionPanel cptnpnlCausality = causalityGrp
				.getCaptionPanelRadioGroup();
		general.add(cptnpnlCausality);

		fieldList = new ArrayList<FieldInterface>();
		fieldList.add(varibilityGrp);
		fieldList.add(typingGrp);
		fieldList.add(causalityGrp);
		fieldList.add(propertyGrp);

		// set default value;
		String value = cci.causalityProperty.getValue();
		if (value == null) {
			causalityGrp.setDefaultOpt();
		} else {
			causalityGrp.setOpt(value);
		}

		value = cci.varibilityProperty.getValue();
		if (value == null) {
			varibilityGrp.setDefaultOpt();
		} else {
			varibilityGrp.setOpt(value);
		}

		value = cci.typingProperty.getValue();
		if (value == null) {
			typingGrp.setDefaultOpt();
		} else {
			typingGrp.setOpt(value);
		}

		String final_ = cci.finalProperty.getValue();
		propertyGrp.setDefaultOpt();
		if (final_ != null) {
			propertyGrp.getCheckBox(final_).setValue(true);
		}
		String protected_ = cci.protectedProperty.getValue();
		if (protected_ != null)
			propertyGrp.getCheckBox(protected_).setValue(true);
	}

	protected void commit() {
		String[] var = varibilityGrp.getSelectedOpts();
		if (var != null && var.length == 1 && var[0] != null) {
			if (varibilityGrp.isDefaultOptSelected(var[0])) {
				cci.varibilityProperty.setValue(null);
			} else
				cci.varibilityProperty.setValue(var[0].toLowerCase());
		}

		var = typingGrp.getSelectedOpts();
		if (var != null && var.length == 1 && var[0] != null) {
			if (typingGrp.isDefaultOptSelected(var[0])) {
				cci.typingProperty.setValue(null);
			} else
				cci.typingProperty.setValue(var[0].toLowerCase());
		}

		var = causalityGrp.getSelectedOpts();
		if (var != null && var.length == 1 && var[0] != null) {
			if (causalityGrp.isDefaultOptSelected(var[0])) {
				cci.causalityProperty.setValue(null);
			} else
				cci.causalityProperty.setValue(var[0].toLowerCase());
		}

		var = propertyGrp.getSelectedOpts();
		if (var != null && var.length > 0) {
			cci.finalProperty.setValue(null);
			cci.protectedProperty.setValue(null);
			for (String s : var) {
				if (s.equals("Final")) {
					cci.finalProperty.setValue("final");
				} else if (s.equals("Protected")) {
					cci.protectedProperty.setValue("protected");
				} else if (s.equals("Replaceable")) {
					cci.replaceableProperty.setValue("replaceable");
				}
			}
			
		} else {
			cci.finalProperty.setValue(null);
			cci.protectedProperty.setValue(null);
		}
		if (cci.protectedProperty.getValue() != null) {
			cci.change2Protected();
		} else {
			cci.change2Public();
		}
	}

	protected void cancel() {
		for (FieldInterface f : fieldList) {
			f.restoreOpts();
		}
	}

	private CaptionPanel getConstraining() {
		CaptionPanel cptnpnlComponent = new CaptionPanel("Constraining clause");
		cptnpnlComponent.setSize("94%", "100%");
		FlexTable layout = createFlexTable();
		cptnpnlComponent.setContentWidget(layout);

		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
		for (int i = 0; i < 1; i++) {
			cellFormatter.setWidth(i, 0, woName);
		}
		layout.setHTML(0, 0, "Name: ");
		layout.setWidget(0, 1, new Label("name"));

		return cptnpnlComponent;
	}

	private FlexTable createFlexTable() {
		FlexTable initFlexTable = new FlexTable();
		initFlexTable.setWidth("98%");
		// initFlexTable.setCellSpacing(2);
		return initFlexTable;
	}

	class CaptionCheckBoxGroup extends CaptionField implements FieldInterface {
		private List<CheckBox> ckList = new ArrayList<CheckBox>();

		public CaptionCheckBoxGroup(String caption, String grp,
				String[] cbOptions) {
			this.caption = caption;
			this.grp = grp;
			this.listOpts = cbOptions;
		}

		public CaptionPanel getProperties() {

			CaptionPanel cptnpnlComponent = new CaptionPanel(caption);
			cptnpnlComponent.setSize("94%", "95%");
			FlexTable layout = createFlexTable();
			cptnpnlComponent.setContentWidget(layout);

			FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
			for (int i = 0; i < 1; i++) {
				cellFormatter.setWidth(i, 0, woName);
			}
			for (int i = 0; i < listOpts.length; i++) {
				String string = listOpts[i];
				CheckBox ck = new CheckBox(string);
				if (string.equals("Replaceable")
				// || string.equals("Protected")
				) {
					ck.setEnabled(false);
				}
				layout.setWidget(i, 0, ck);
				ckList.add(ck);
			}
			return cptnpnlComponent;
		}

		public CheckBox getCheckBox(String text) {
			for (CheckBox ck : ckList) {
				if (ck.getText().equalsIgnoreCase(text)) {
					return ck;
				}
			}
			return null;
		}

		public void setDefaultOpt() {
			clearBox();
		}

		@Override
		public String[] getSelectedOpts() {
			List<String> optArray = new ArrayList<String>();
			for (CheckBox ck : ckList) {
				if (ck.getValue()) {
					optArray.add(ck.getText());
				}
			}
			selOpts = optArray.toArray(new String[0]);
			return selOpts;
		}

		@Override
		public void setOpts(String[] opts) {
			clearBox();

			for (CheckBox ck : ckList) {
				if (opts != null && opts.length > 0) {
					for (String s : opts) {
						if (ck.getText().equalsIgnoreCase(s)) {
							ck.setValue(true);
							break;
						}
					}
				}
			}
		}

		private void clearBox() {
			for (CheckBox c : ckList) {
				c.setValue(false);
			}
		}

		@Override
		public void restoreOpts() {
			setOpts(selOpts);
		}
	}

	class CaptionRadioGroup extends CaptionField implements FieldInterface {
		private List<RadioButton> rbList = new ArrayList<RadioButton>();
		private String defaultOpt;

		public CaptionRadioGroup(String caption, String grp,
				String[] rbOptions, String defaultOpt) {
			this.caption = caption;
			this.grp = grp;
			this.listOpts = rbOptions;
			this.defaultOpt = defaultOpt;
		}

		private CaptionPanel getCaptionPanelRadioGroup() {
			CaptionPanel cptnpnlComponent = new CaptionPanel(caption);
			cptnpnlComponent.setSize("94%", "94%");
			FlexTable layout = createFlexTable();
			cptnpnlComponent.setContentWidget(layout);

			for (int i = 0; i < listOpts.length; i++) {
				String string = listOpts[i];
				RadioButton rb = new RadioButton(grp, string);
				layout.setWidget(i, 0, rb);
				rbList.add(rb);
				if (listOpts[i].equalsIgnoreCase(defaultOpt)) {
					rb.setValue(true);
				}
			}
			return cptnpnlComponent;
		}

		public void setDefaultOpt() {
			setOpts(new String[] { defaultOpt });
		}

		public boolean isDefaultOptSelected(String opt) {
			return defaultOpt.equalsIgnoreCase(opt);
		}

		@Override
		public String[] getSelectedOpts() {
			String opt = null;
			for (RadioButton rb : rbList) {
				if (rb.getValue()) {
					opt = rb.getText();
					break;
				}
			}
			selOpts = new String[] { opt };
			return selOpts;
		}

		public void setOpt(String opt) {
			setOpts(new String[] { opt });
		}

		@Override
		public void setOpts(String[] opts) {
			clearBox();
			for (RadioButton r : rbList) {
				if (opts != null && opts.length == 1) {
					if (r.getText().equalsIgnoreCase(opts[0])) {
						r.setValue(true);
						break;
					}
				}
			}
		}

		private void clearBox() {
			for (RadioButton r : rbList) {
				r.setValue(false);
			}
		}

		@Override
		public void restoreOpts() {
			setOpts(selOpts);
		}
	}

	interface FieldInterface {
		public String[] getSelectedOpts();

		public void setOpts(String[] opts);

		public void restoreOpts();
	}

	class CaptionField {
		protected String caption;
		protected String grp;
		protected String[] listOpts;
		protected String[] selOpts;
	}
}
