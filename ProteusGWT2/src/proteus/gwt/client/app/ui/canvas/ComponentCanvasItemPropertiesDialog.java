package proteus.gwt.client.app.ui.canvas;

import java.util.List;

import proteus.gwt.client.app.ProteusGWT;
import proteus.gwt.client.app.event.RefreshCodeCanvasEvent;
import proteus.gwt.client.app.event.RepaintCanvasEvent;
import proteus.gwt.client.proxy.AppContext;
import proteus.gwt.shared.datatransferobjects.EnumerationDTO;
import proteus.gwt.shared.modelica.Component;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

@Deprecated
public class ComponentCanvasItemPropertiesDialog extends WindowBox {

	private String wholeName = "";
	private String declarationName = "";
	private ComponentCanvasItem cci;
	final String prefixStr = "&nbsp;&nbsp;";
	final String commentSuffix = "&nbsp;&nbsp;&nbsp;&nbsp;";
	final String pathSuffix = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	final String nameprefix = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";

	
	public String getWholeName() {
		return wholeName;
	}

	public String getDeclarationName() {
		return declarationName;
	}

	public void showBox() {
		if (cci == null)
			return;

		Component component = cci.getComponent();
		final List<ParameterProperty> paraList = component.getParameterList();
		for (final ParameterProperty para : paraList) {
			boolean paraTag = false, initParaTag = false;
			if (para.isHeader() && !para.isInherited()) {
				paraTag = true;
			}
			if (!para.isHeader() && para.getStart() != null) {
				initParaTag = true;
			}
			if (!paraTag && !initParaTag)
				continue;

			para.updateValue();
		}

		center();
	}

	public ComponentCanvasItemPropertiesDialog(
			ComponentCanvasItem componentCanvasItem) {
		super.setResizable(true);
		this.setAnimationEnabled(true);
		this.cci = componentCanvasItem;
		Component component = componentCanvasItem.getComponent();
		wholeName = component.compData.getWholeName();
		final List<ParameterProperty> paraList = component.getParameterList();
		declarationName = componentCanvasItem.getName();
		String imgURL = AppContext
				.pathToServerUrl("/resources/images/classname/large/")
				+ wholeName + ".png";

		String comment = "";
		setHTML("Properties");
		VerticalPanel shellHolder = new VerticalPanel();
		ScrollPanel sp = new ScrollPanel();
		sp.setSize("350px", "350px");
		
		shellHolder.add(sp);
		setWidget(shellHolder);
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setBorderWidth(2);
		mainPanel.setSpacing(3);
		sp.add(mainPanel);

		HTML htmlNewHtml = new HTML(
				"<SPAN style=\"COLOR: #0046d5\">Component</SPAN>", true);
		HTML htmlName = new HTML(prefixStr + "Name:" + nameprefix
				+ declarationName + prefixStr);
		HTML htmlComment = new HTML(prefixStr + "Comment:" + commentSuffix
				+ comment);
		HTML htmlPath = new HTML(prefixStr + "Path:" + pathSuffix + wholeName
				+ prefixStr);

		VerticalPanel namevp = new VerticalPanel();
		namevp.setSpacing(5);
		namevp.add(htmlNewHtml);
		namevp.add(htmlName);
		namevp.add(htmlComment);
		namevp.add(htmlPath);
		mainPanel.add(namevp);

		HTML htmlparameters = new HTML(
				"<SPAN style=\"COLOR: #0046d5\">Parameters</SPAN>", true);
		HTML htmlstartparameters = new HTML(
				"<SPAN style=\"COLOR: #0046d5\">Initialization</SPAN>", true);
		VerticalPanel paraVerticalPanel = new VerticalPanel();
		paraVerticalPanel.setSpacing(5);
		VerticalPanel InitVerticalPanel = new VerticalPanel();
		InitVerticalPanel.setSpacing(5);

		paraVerticalPanel.add(htmlparameters);
		InitVerticalPanel.add(htmlstartparameters);

		int paraCount = 0, initparaCount = 0;
		for (final ParameterProperty para : paraList) {
			boolean paraTag = false, initStartTag = false, initGroupTag = false, enumTag = false;

			String initAnnotation = para.getAnnotationGroup();
			if (initAnnotation != null
					&& initAnnotation.contains("Initialization")) {
				initGroupTag = true;
			}
			if (para.isHeader() && !para.isInherited()) {
				paraTag = true;
			}
			if ((!para.isHeader() && para.getStart() != null)) {
				initStartTag = true;
			}

			if (!paraTag && !initStartTag && !initGroupTag)
				continue;

			final AttributeDialog paraAttribute = new AttributeDialog(para);

			HorizontalPanel paraPanel = new HorizontalPanel();
			String paraName = para.getName();
			HTML htmlParaName = new HTML(prefixStr + paraName);
			htmlParaName.setSize("85px", "20px");
			if (initStartTag) {
				htmlParaName = new HTML(prefixStr + paraName + ".start");
				htmlParaName.setSize("85px", "20px");
			}

			paraPanel.add(htmlParaName);

			HorizontalPanel paraInputPanel = new HorizontalPanel();
			final PushButton pbadv = new PushButton(".");
			pbadv.setSize("1px", "10px");
			pbadv.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					para.resetAttBox();
					paraAttribute.center();
				}
			});

			MyCheckBox ckParaFixed = new MyCheckBox();
			TextBox tbParaStart = new TextBox();
//			para.setCkParaFixed(ckParaFixed);
			para.setTbParaStart(tbParaStart);

			ListBox eList = null, bList = null;

			if (para.getType().equals("enumeration")
					&& para.getEnumList().size() > 0) {
				eList = new ListBox();
				eList.addItem("");
				List<EnumerationDTO> elist = para.getEnumList();
				for (EnumerationDTO e : elist) {
					eList.addItem(e.getName(), e.getComment());
				}
			} else if (para.getType().equals("Boolean")) {
				bList = new ListBox();
				bList.addItem("");
				bList.addItem("false", "false");
				bList.addItem("true", "true");
			}

			if (initStartTag) {
				initparaCount++;

				ckParaFixed
						.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
							@Override
							public void onValueChange(
									ValueChangeEvent<Boolean> event) {
								para.setAttFixed(event);
								para.setFixedVar(true);
							}
						});

				tbParaStart
						.addValueChangeHandler(new ValueChangeHandler<String>() {
							@Override
							public void onValueChange(
									ValueChangeEvent<String> event) {
								para.setAttStart(event);
							}
						});

				if (para.getFixed() != null) {
					if (para.getFixed().equals("true")) {
						ckParaFixed.setValue(true);
					} else if (para.getFixed().equals("false")) {
						ckParaFixed.setValue(false);
					}
				}
				paraInputPanel.add(ckParaFixed);
				paraInputPanel.add(new HTML("&nbsp;&nbsp;"));
				if (eList != null) {
					paraInputPanel.add(eList);
				} else if (bList != null) {
					paraInputPanel.add(bList);
				} else {
					tbParaStart.setSize("100px", "16px");
					tbParaStart.setText(para.getStart());
					paraInputPanel.add(tbParaStart);
				}

				paraInputPanel.add(pbadv);
			} else if (paraTag) {
				if (initGroupTag) {
					initparaCount++;
				} else {
					paraCount++;
				}
				if (eList != null) {
					paraInputPanel.add(eList);
				} else if (bList != null) {
					paraInputPanel.add(bList);
				} else {
					TextBox tbParaValue = new TextBox();
					para.setTbParaValue(tbParaValue);
					tbParaValue.setSize("120px", "16px");
					tbParaValue.setText(para.getValue());
					paraInputPanel.add(tbParaValue);
				}
				paraInputPanel.add(pbadv);
			}

			paraPanel.add(paraInputPanel);

			HTML htmlUnit = new HTML(
					(para.getUnit() == null ? "&nbsp;&nbsp;&nbsp;"
							: "&nbsp;&nbsp;&nbsp;" + para.getUnit())
							+ "&nbsp;");
			htmlUnit.setSize("75px", "20px");
			HTML htmlQuantity = new HTML(para.getQuantity() == null ? "" : para
					.getQuantity());
			paraPanel.add(htmlUnit);
			paraPanel.add(htmlQuantity);
			if (initStartTag || initGroupTag) {
				InitVerticalPanel.add(paraPanel);
			} else if (paraTag) {
				paraVerticalPanel.add(paraPanel);
			}
		}// end of for loop

		if (paraCount > 0)
			mainPanel.add(paraVerticalPanel);
		if (initparaCount > 0)
			mainPanel.add(InitVerticalPanel);

		HorizontalPanel buttonPanel = new HorizontalPanel();

		Button btnOk = new Button("OK");
		buttonPanel.add(btnOk);

		btnOk.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ComponentCanvasItemPropertiesDialog.this.hide();
				StringBuilder modStr = new StringBuilder();
				modStr.append("(");
				int i = 0;

				for (ParameterProperty para : paraList) {
					String s = para.commitChange();
					if (s != null) {
						if (i > 0)
							modStr.append(",");
						modStr.append(s);
						i++;
					}
				}
				modStr.append(")");
				cci.setParaModifier(modStr.toString());

				ProteusGWT.eventBus.fireEvent(new RefreshCodeCanvasEvent());
				ProteusGWT.eventBus.fireEvent(new RepaintCanvasEvent());
			}
		});

		Button btnCancel = new Button("Cancel");
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ComponentCanvasItemPropertiesDialog.this.hide();
			}
		});
		buttonPanel.add(btnCancel);
		shellHolder.add(buttonPanel);
	}

	public class AttributeDialog extends DialogBox {

		private TextBox tbAttStart, tbMin, tbMax, tbDisplayUnit, tbNominal,
				tbQuantity;
		private ListBox lbFixed, lbStateSelect, lbStart;

		private VerticalPanel vp;

		private ParameterProperty para;

		public AttributeDialog(final ParameterProperty para) {
			this.para = para;
			this.setAutoHideEnabled(true);
			this.setAnimationEnabled(true);
			this.setGlassEnabled(true);
			this.setText("Variable declaration");
			// this.setTitle(para.getName());
			vp = new VerticalPanel();
			this.setWidget(vp);

			HTML htmlNewHtml = new HTML(
					"<SPAN style=\"COLOR: #0046d5\">Model------------------------------------------------------------------</SPAN>",
					true);
			HTML htmlPath = new HTML(prefixStr + "Path:" + pathSuffix + "&&&&&"
					+ prefixStr);
			HTML htmlComment = new HTML(prefixStr + "Comment:" + commentSuffix
					+ "&&&");
			HTML htmlparameters = new HTML(
					"<SPAN style=\"COLOR: #0046d5\">Parameters-----------------------------------------------------------</SPAN>",
					true);

			vp.add(htmlNewHtml);
			vp.add(htmlPath);
			vp.add(htmlComment);
			vp.add(htmlparameters);

			HorizontalPanel atthorizontalpanel = new HorizontalPanel();
			vp.add(atthorizontalpanel);

			VerticalPanel attNamevertialpanel = new VerticalPanel();
			VerticalPanel attBoxvertialpanel = new VerticalPanel();
			atthorizontalpanel.add(attBoxvertialpanel);

			String namespace = "&nbsp;&nbsp;&nbsp;";
			// define box
			tbMin = new TextBox();
			tbMax = new TextBox();
			tbDisplayUnit = new TextBox();
			tbNominal = new TextBox();
			tbAttStart = new TextBox();
			lbFixed = new ListBox();
			lbStateSelect = new ListBox();
			// for integer
			tbQuantity = new TextBox();
			// for Boolean
			lbStart = new ListBox();

			// set init value of box
			tbMin.setValue(para.getMin());
			tbMax.setValue(para.getMax());
			tbDisplayUnit.setValue(para.getDisplayUnit());
			tbNominal.setValue(para.getNominal());
			tbAttStart.setValue(para.getStart());
			tbQuantity.setText(para.getQuantity());
			lbFixed.addItem("", "");
			lbFixed.addItem("false", "false");
			lbFixed.addItem("true", "true");
			if (para.getFixed() != null) {
				if (para.getFixed().equals("true")) {
					lbFixed.setSelectedIndex(2);
				} else {
					lbFixed.setSelectedIndex(1);
				}
			} else {
				lbFixed.setSelectedIndex(0);
			}
			lbStart.addItem("", "");
			lbStart.addItem("false", "false");
			lbStart.addItem("true", "true");

			lbStateSelect.addItem("", "");
			lbStateSelect.addItem("never(Do not use as state at all)",
					"StateSelect.never");
			lbStateSelect.addItem("avoid(Use as state if can't be avoided)",
					"StateSelect.avoid");
			lbStateSelect.addItem("default(Use as state if appropriate)",
					"StateSelect.default");
			lbStateSelect.addItem("prefer(Prefer it as state over default)",
					"StateSelect.prefer");
			lbStateSelect.addItem("always(Use as state always)",
					"StateSelect.always");
			if (para.getStateSelect() != null) {
				String tempState = para.getStateSelect();
				if (tempState.equals("StateSelect.never")) {
					lbStateSelect.setSelectedIndex(1);
				} else if (tempState.equals("StateSelect.avoid")) {
					lbStateSelect.setSelectedIndex(2);
				} else if (tempState.equals("StateSelect.default")) {
					lbStateSelect.setSelectedIndex(3);
				} else if (tempState.equals("StateSelect.prefer")) {
					lbStateSelect.setSelectedIndex(4);
				} else if (tempState.equals("StateSelect.always")) {
					lbStateSelect.setSelectedIndex(5);
				}
			} else {
				lbStateSelect.setSelectedIndex(0);
			}

			// pass box to parameterProperty
			para.setTbMin(tbMin);
			para.setTbMax(tbMax);
			para.setTbDisplayUnit(tbDisplayUnit);
			para.setTbNominal(tbNominal);
			para.setLbStateSelect(lbStateSelect);
			para.setTbAttStart(tbAttStart);
			para.setLbFixed(lbFixed);
			para.setTbQuantity(tbQuantity);
			para.setLbStart(lbStart);

			if (para.getType() != null) {
				HorizontalPanel paraHp;
				HTML html;
				if (para.getType().equals("Real")) {
					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "displayUnit");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbDisplayUnit);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "min");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbMin);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "max");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbMax);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "start");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbAttStart);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "fixed");
					html.setSize("83px", "16px");
					paraHp.add(html);
					paraHp.add(lbFixed);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "nominal");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbNominal);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "stateSelect");
					html.setSize("83px", "16px");
					paraHp.add(html);
					paraHp.add(lbStateSelect);
					attBoxvertialpanel.add(paraHp);

				} else if (para.getType().equals("Integer")) {
					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "quantity");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbQuantity);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "min");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbMin);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "max");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbMax);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "start");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbAttStart);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "fixed");
					html.setSize("83px", "16px");
					paraHp.add(html);
					paraHp.add(lbFixed);
					attBoxvertialpanel.add(paraHp);
				} else if (para.getType().equals("String")) {
					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "start");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbAttStart);
					attBoxvertialpanel.add(paraHp);

				} else if (para.getType().equals("Boolean")) {
					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "quantity");
					html.setSize("85px", "16px");
					paraHp.add(html);
					paraHp.add(tbQuantity);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "start");
					html.setSize("83px", "16px");
					paraHp.add(html);
					paraHp.add(lbStart);
					attBoxvertialpanel.add(paraHp);

					paraHp = new HorizontalPanel();
					html = new HTML(namespace + "fixed");
					html.setSize("83px", "16px");
					paraHp.add(html);
					paraHp.add(lbFixed);
					attBoxvertialpanel.add(paraHp);
				} else if (para.getType().equals("StateSelect")) {
					// no field for user to edit
				} else if (para.getType().equals("Enumeration")) {

				}
			} else {
				System.err.println(wholeName);
			}
			Button okButton = new Button();
			okButton.setText("OK");
			okButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					para.attribute2Parameter();
					hide();
				}
			});

			Button cancelButton = new Button();
			cancelButton.setText("Cancel");
			cancelButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});
			HorizontalPanel hpButton = new HorizontalPanel();
			hpButton.setHorizontalAlignment(HorizontalAlignmentConstant
					.endOf(Direction.RTL));
			hpButton.add(okButton);
			hpButton.setSpacing(10);
			hpButton.add(cancelButton);

			vp.add(hpButton);
		}

		/**
		 * @return the tbStart
		 */
		public TextBox getTbStart() {
			return tbAttStart;
		}

		/**
		 * @param tbStart
		 *            the tbStart to set
		 */
		public void setTbStart(TextBox tbStart) {
			this.tbAttStart = tbStart;
		}

		/**
		 * @return the tbMin
		 */
		public TextBox getTbMin() {
			return tbMin;
		}

		/**
		 * @param tbMin
		 *            the tbMin to set
		 */
		public void setTbMin(TextBox tbMin) {
			this.tbMin = tbMin;
		}

		/**
		 * @return the tbMax
		 */
		public TextBox getTbMax() {
			return tbMax;
		}

		/**
		 * @param tbMax
		 *            the tbMax to set
		 */
		public void setTbMax(TextBox tbMax) {
			this.tbMax = tbMax;
		}

		/**
		 * @return the tbDisplayUnit
		 */
		public TextBox getTbDisplayUnit() {
			return tbDisplayUnit;
		}

		/**
		 * @param tbDisplayUnit
		 *            the tbDisplayUnit to set
		 */
		public void setTbDisplayUnit(TextBox tbDisplayUnit) {
			this.tbDisplayUnit = tbDisplayUnit;
		}

		/**
		 * @return the tbNominal
		 */
		public TextBox getTbNominal() {
			return tbNominal;
		}

		/**
		 * @param tbNominal
		 *            the tbNominal to set
		 */
		public void setTbNominal(TextBox tbNominal) {
			this.tbNominal = tbNominal;
		}

		/**
		 * @return the vp
		 */
		public VerticalPanel getVp() {
			return vp;
		}

		/**
		 * @param vp
		 *            the vp to set
		 */
		public void setVp(VerticalPanel vp) {
			this.vp = vp;
		}
	}
}
