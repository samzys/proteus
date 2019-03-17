package proteus.gwt.client.app.ui.canvas;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class AdvPropertyDialogBox extends DialogBox {
	private final ParameterProperty para;
	private String type;
	private String name;
	private String path;
	private String comment;

	public final static int DialogWidth = 400, DialogHeight = 420;

	public final static int InputFieldWidth = 220;

	public AdvPropertyDialogBox(final ParameterProperty para) {
		this.para = para;
		this.name = para.getName();
		this.type = para.getType();
		this.path = para.getPath();
		this.comment = para.getComment();
		setText(name + " Property");
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
				hide();
				para.attribute2Parameter();
			}
		});
		horizontalPanel.add(btnOk);
		horizontalPanel.setCellHorizontalAlignment(btnOk,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnOk.setWidth(PropertyDialogBox.woButton);

		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		horizontalPanel.add(btnCancel);
		horizontalPanel.setCellHorizontalAlignment(btnCancel,
				HasHorizontalAlignment.ALIGN_CENTER);
		btnCancel.setWidth(PropertyDialogBox.woButton);

		DecoratedTabPanel decoratedTabPanel = new DecoratedTabPanel();
		dockLayoutPanel.add(decoratedTabPanel);
		decoratedTabPanel.setSize("99%", "390px");

		VerticalPanel verticalPanel = new VerticalPanel();
		decoratedTabPanel.add(verticalPanel, "General", false);
		verticalPanel.setSize("98%", "340px");

		CaptionPanel cptnpnlComponent = new CaptionPanel("Component");
		verticalPanel.add(cptnpnlComponent);
		cptnpnlComponent.setSize("98%", "98px");

		FlexTable layout = new FlexTable();
		cptnpnlComponent.setContentWidget(layout);
		layout.setWidth("98%");
		layout.setCellSpacing(6);
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
		for (int i = 0; i < 1; i++) {
			cellFormatter.setWidth(i, 0, PropertyDialogBox.woName);
		}
		// Add a title to the form
		// Add some standard form options
		layout.setHTML(0, 0, "Name: ");
		layout.setWidget(0, 1, new Label(name));
		layout.setHTML(1, 0, "Comment: ");
		layout.setWidget(1, 1, new Label(comment));
		layout.setHTML(2, 0, "Path: ");
		layout.setWidget(2, 1, new Label(path));

		ScrollPanel scrollPanel = new ScrollPanel();
		verticalPanel.add(scrollPanel);
		scrollPanel.setSize("98%", "240px");

		VerticalPanel paraPanel = new VerticalPanel();
		scrollPanel.setWidget(paraPanel);

		FlexTable flexTable = new FlexTable();
		flexTable.setWidth("98%");
		flexTable.setCellSpacing(6);
		CellFormatter paraCellFormat = flexTable.getCellFormatter();
		if (type != null)
			if (type.equals("Real")) {
				flexTable.setHTML(0, 0, "displayUnit");
				paraCellFormat.setWidth(0, 0, PropertyDialogBox.woName);
				MyTextBox displayTB = new MyTextBox();
				displayTB.setInputValue(para.getDisplayUnit());
				displayTB.setAlignment(TextAlignment.RIGHT);
				displayTB.setWidth(InputFieldWidth + "px");
				flexTable.setWidget(0, 1, displayTB);
				para.setAttDisplayUnit(displayTB);

				flexTable.setHTML(1, 0, "min");
				paraCellFormat.setWidth(1, 0, PropertyDialogBox.woName);
				MyTextBox minTB = new MyTextBox();
				minTB.setInputValue(para.getMin());
				minTB.setAlignment(TextAlignment.RIGHT);
				minTB.setWidth(InputFieldWidth + "px");
				flexTable.setWidget(1, 1, minTB);
				para.setAttMin(minTB);

				flexTable.setHTML(2, 0, "max");
				paraCellFormat.setWidth(2, 0, PropertyDialogBox.woName);
				MyTextBox maxTB = new MyTextBox();
				maxTB.setInputValue(para.getMax());
				maxTB.setAlignment(TextAlignment.RIGHT);
				maxTB.setWidth(InputFieldWidth + "px");
				flexTable.setWidget(2, 1, maxTB);
				para.setAttMax(maxTB);

				flexTable.setHTML(3, 0, "start");
				paraCellFormat.setWidth(3, 0, PropertyDialogBox.woName);
				MyTextBox startTB = new MyTextBox();
				startTB.setInputValue(para.getStart());
				startTB.setAlignment(TextAlignment.RIGHT);
				startTB.setWidth(InputFieldWidth + "px");
				flexTable.setWidget(3, 1, startTB);
				para.setAttStart(startTB);

				flexTable.setHTML(4, 0, "fixed");
				paraCellFormat.setWidth(4, 0, PropertyDialogBox.woName);
				MyListBox fixedLB = new MyListBox(MyListBox.BOOLEAN, para.getFixed());
				fixedLB.setWidth(getListBoxWidth() + "px");
				fixedLB.setInputValue(para.getFixed());
				flexTable.setWidget(4, 1, fixedLB);
				para.setAttFixed(fixedLB);

				flexTable.setHTML(5, 0, "nominal");
				paraCellFormat.setWidth(5, 0, PropertyDialogBox.woName);
				MyTextBox nominalTB = new MyTextBox();
				nominalTB.setInputValue(para.getNominal());
				nominalTB.setAlignment(TextAlignment.RIGHT);
				nominalTB.setWidth(InputFieldWidth + "px");
				flexTable.setWidget(5, 1, nominalTB );
				para.setAttNominal(nominalTB);

				flexTable.setHTML(6, 0, "stateSelect");
				paraCellFormat.setWidth(6, 0, PropertyDialogBox.woName);
				MyListBox stateSelectLB = new MyListBox(MyListBox.STATESELECT, "");
				stateSelectLB.setInputValue(para.getStateSelect());
				stateSelectLB.setWidth(getListBoxWidth() + "px");
				flexTable.setWidget(6, 1, stateSelectLB);
				para.setAttStateSelect(stateSelectLB);
				
			} else if (type.equals("Integer")) {
				flexTable.setHTML(0, 0, "quantity");
				paraCellFormat.setWidth(0, 0, PropertyDialogBox.woName);
				MyTextBox quantityTB = new MyTextBox();
				quantityTB.setInputValue(para.getQuantity());
				quantityTB.setAlignment(TextAlignment.RIGHT);
				flexTable.setWidget(0, 1, quantityTB);
				para.setAttQuantity(quantityTB);

				flexTable.setHTML(1, 0, "min");
				paraCellFormat.setWidth(1, 0, PropertyDialogBox.woName);
				MyTextBox minTB = new MyTextBox();
				minTB.setInputValue(para.getMin());
				minTB.setAlignment(TextAlignment.RIGHT);
				minTB.setWidth(InputFieldWidth + "px");
				flexTable.setWidget(1, 1, minTB);
				para.setAttMin(minTB);

				flexTable.setHTML(2, 0, "max");
				paraCellFormat.setWidth(2, 0, PropertyDialogBox.woName);
				MyTextBox maxTB = new MyTextBox();
				maxTB.setInputValue(para.getMax());
				maxTB.setAlignment(TextAlignment.RIGHT);
				maxTB.setWidth(InputFieldWidth + "px");
				flexTable.setWidget(2, 1, maxTB);
				para.setAttMax(maxTB);


				flexTable.setHTML(3, 0, "start");
				paraCellFormat.setWidth(3, 0, PropertyDialogBox.woName);
				MyTextBox startTB = new MyTextBox();
				startTB.setAlignment(TextAlignment.RIGHT);
				startTB.setInputValue(para.getValue());
				flexTable.setWidget(3, 1, startTB);
				para.setAttStart(startTB);

				
				flexTable.setHTML(4, 0, "fixed");
				paraCellFormat.setWidth(4, 0, PropertyDialogBox.woName);
				MyListBox fixedLB = new MyListBox(MyListBox.BOOLEAN, "");
				fixedLB.setWidth(getListBoxWidth() + "px");
				fixedLB.setInputValue(para.getFixed());
				flexTable.setWidget(4, 1, fixedLB);
				para.setAttFixed(fixedLB);

			} else if (type.equals("String")) {
				flexTable.setHTML(0, 0, "start");
				paraCellFormat.setWidth(0, 0, PropertyDialogBox.woName);
				MyTextBox startTB= new MyTextBox();
				startTB.setAlignment(TextAlignment.RIGHT);
				startTB.setInputValue(para.getStart());
				flexTable.setWidget(0, 1, startTB);
				para.setAttStart(startTB);

			} else if (type.equals("Boolean")) {
				flexTable.setHTML(0, 0, "quantity");
				paraCellFormat.setWidth(0, 0, PropertyDialogBox.woName);
				MyTextBox quantityTB= new MyTextBox();
				quantityTB.setAlignment(TextAlignment.RIGHT);
				quantityTB.setInputValue(para.getQuantity());
				flexTable.setWidget(0, 1, quantityTB);
				para.setAttQuantity(quantityTB);

				flexTable.setHTML(1, 0, "start");
				paraCellFormat.setWidth(1, 0, PropertyDialogBox.woName);
				MyListBox startLB = new MyListBox(MyListBox.BOOLEAN, "");
				startLB.setInputValue(para.getStart());
				flexTable.setWidget(1, 1, startLB);
				para.setAttStart(startLB);

				flexTable.setHTML(2, 0, "fixed");
				paraCellFormat.setWidth(2, 0, PropertyDialogBox.woName);
				MyListBox fixedLB = new MyListBox(MyListBox.BOOLEAN, "");
				fixedLB.setInputValue(para.getFixed());
				flexTable.setWidget(2, 1, fixedLB);
				para.setAttFixed(fixedLB);
			} else if (type.equals("StateSelect")) {

			} else if (type.equals("Enumeration")) {

			}

		int rowCount = flexTable.getRowCount();
		if (rowCount > 0) {
			CaptionPanel cptnpnlParameters = new CaptionPanel("Parameters");
			paraPanel.add(cptnpnlParameters);
			cptnpnlParameters.setWidth("360px");
			cptnpnlParameters.setContentWidget(flexTable);
		}
		decoratedTabPanel.selectTab(0);
	}

	private int getListBoxWidth() {
		return InputFieldWidth + 6;
	}
}
