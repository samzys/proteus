package proteus.gwt.shared.modelica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import proteus.gwt.client.app.ui.canvas.ComponentCanvasItem;
import proteus.gwt.client.app.ui.canvas.ConnectionItem;
import proteus.gwt.client.app.ui.canvas.ConnectorItem;
import proteus.gwt.client.app.ui.canvas.Icon;
import proteus.gwt.client.app.ui.canvas.ParameterProperty;
import proteus.gwt.client.app.ui.util.Bitmap;
import proteus.gwt.client.app.ui.util.Ellipse;
import proteus.gwt.client.app.ui.util.GraphicCanvas;
import proteus.gwt.client.app.ui.util.GraphicItem;
import proteus.gwt.client.app.ui.util.GraphicUtils;
import proteus.gwt.client.app.ui.util.Line;
import proteus.gwt.client.app.ui.util.Polygon;
import proteus.gwt.client.app.ui.util.Rectangle;
import proteus.gwt.client.app.ui.util.Text;
import proteus.gwt.shared.graphics.Graphics2D;
import proteus.gwt.shared.app.ui.NewClassDefDialog;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO;
import proteus.gwt.shared.datatransferobjects.IcongraphicDTO;
import proteus.gwt.shared.datatransferobjects.ModelDTO;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;
import proteus.gwt.shared.datatransferobjects.ParameterDTO;
import proteus.gwt.shared.modelica.AnnotationParser.GraphicExpression;
import proteus.gwt.shared.modelica.Placement.Transformation;
import proteus.gwt.shared.modelica.parser.OMClassDefinition;
import proteus.gwt.shared.util.Constants;

public class Component extends ClassDef {

	public static final int MODEL_TYPE = 0, DIAGRAM_TYPE = 1,
			COMPONENT_DEMO = 3;

	public ComponentDTO compData;

	private List<Component> compList = new ArrayList<Component>();

	private Component[] components; // 09 Nov 16 : lt

	public int componentType = MODEL_TYPE;
	public String[] connectorString;

	private List<Connector> connList = new ArrayList<Connector>();

	public DemoTrans connTrans[];

	private proteus.gwt.shared.graphics.Rectangle diagramSize;

	public String[] diagramString;

	private int[] extent = { -100, -100, 100, 100 };
	public Icon icon, diagram;

	public String[] iconString;

	public ModelDTO modelDTO;

	private List<ParameterProperty> parameterList = new ArrayList<ParameterProperty>();

	public Component(OMClassDefinition classDefOM) {
		super(classDefOM);
		// this declaration name is to store the declaration name shared by
		// property panel, code canvas
		parameterList.add(new ParameterProperty(Constants.DECL_NAME, true));
	}

	public void addselfConnector(String declarationName, Transformation trans) {
		// updateIconName(declarationName);
		if (restriction.equals(Constants.TYPE_CONNECTOR)
				|| restriction.equals(Constants.TYPE_EXPANDABLECONNECTOR)) {
			// add it self into connector item@@sam comment@@ 9 June, 2011
			connList
					.add(new Connector(declarationName, null, trans, iconString));
		}
	}

	private void addToIconList(ComponentDTO compData2, List<String> iconList) {
		IcongraphicDTO graphic = compData2.getIconGraphicsDTO();
		if (graphic != null)
			iconList.add(graphic.getGraphics());
	}

	private Icon createIcon(Graphics2D g2, String declname) {
		// @@sam comment @@
		// this method is to update the iconString to remove %name, R=%R, and to
		// remove the unnecessary segement if the visibility is not visible, e.g
		// visible=useHeatPort
		Icon.ModelicaImpl _Icon = new Icon.ModelicaImpl(g2, extent);
		GraphicCanvas canvas = _Icon.getCanvas();
		List<GraphicItem> gilistRect = new LinkedList<GraphicItem>();
		List<GraphicItem> textList = new ArrayList<GraphicItem>();
		if (iconString == null)
			return null;
		for (int i = iconString.length - 1; i >= 0; i--) {
			String text = iconString[i];
			try {
				List<GraphicExpression> graphicExpressions = null;
				graphicExpressions = GraphicExpression.parseGraphic(text);
				// this will set the value for graphicExpressions

				// 09 Oct 05 lt : fix for 3.1 graphic annotation
				for (GraphicExpression exp : graphicExpressions) {
					GraphicItem item = null;
					if (exp.name.equals(Line.NAME)) {
						// involves line class, where para arrow maybe an issue.
						item = new Line(canvas, exp.namedArguments,
								getParameterList());
					} else if (exp.name.equals(Polygon.NAME)) {
						item = new Polygon(canvas, exp.namedArguments);
					} else if (exp.name.equals(Rectangle.NAME)) {
						item = new Rectangle(canvas, exp.namedArguments);
					} else if (exp.name.equals(Ellipse.NAME)) {
						item = new Ellipse(canvas, exp.namedArguments);
					} else if (exp.name.equals(Text.NAME)) {
						final Text t = new Text(canvas, exp.namedArguments,
								getParameterList());
						textList.add(t);
						continue;
					} else if (exp.name.equals(Bitmap.NAME)) {
						// String sourceFilePath = null;
						item = new Bitmap(canvas, exp.namedArguments,
								getParameterList());
					}
					if (item != null) {
						gilistRect.add(item);
					}
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}// end of for loop

		canvas.setGraphicItems(gilistRect.toArray(new GraphicItem[0]));
		canvas.setTextItems(textList.toArray(new GraphicItem[0]));
		return _Icon;
	}

	public boolean isMSLmodel() {
		if (compData != null) {
			String wholeName = compData.getWholeName();
			if (wholeName.startsWith("Modelica")
					&& wholeName.contains("Examples")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the compData
	 */
	public ComponentDTO getCompData() {
		return compData;
	}

	public Component[] getComponents() {
		if (components != null) {
			return components;
		}
		return getComponents(compData).toArray(new Component[0]);
	}

	private List<Component> getComponents(ComponentDTO comp) {
		for (ComponentDTO extc : comp.getExtendComps()) {
			getComponents(extc);
		}

		for (ComponentDTO incc : comp.getIncludeComps()) {
			Component c = toComponent(incc);
			if (c != null)
				compList.add(c);
		}
		return compList;
	}

	public List<ConnectorItem> getConnectorItems(Graphics2D g2) {
		List<ConnectorItem> items = new LinkedList<ConnectorItem>();
		if (connList.size() > 0) {
			for (Connector c : connList) {
				items.add(ConnectorItem.Factory.create(c, g2));
			}
		}
		return items;
	}

	/**
	 * @return the connList
	 */
	public List<Connector> getConnList() {
		return connList;
	}

	public Icon getDiagram(Graphics2D g2) {
		diagram = new Icon.ModelicaImpl(g2, diagramString);
		diagram.setIconSize(400, 400);
		return diagram;
	}

	public Icon getDiagram2(Graphics2D g2) {
		diagram = new Icon.ModelicaImpl(g2, diagramString);
		diagram.setIconSize(200, 200);
		return diagram;
	}

	/**
	 * @return the diagramSize
	 */
	public proteus.gwt.shared.graphics.Rectangle getDiagramSize() {
		return diagramSize;
	}

	public Icon getIcon() {
		if (icon != null)
			return icon;
		else
			return null;
	}

	public Icon getIcon(Graphics2D g2, String declname) {
		if (icon != null)
			return icon;
		else
			return icon = createIcon(g2, declname);
	}

	public Icon getIconCanvasIcon(Graphics2D g2) {
		// ModelicaImpl iconBig = new Icon.ModelicaImpl(g2, iconString);
		return createIcon(g2, null);
	}

	/**
	 * @return the parameterList
	 */
	public List<ParameterProperty> getParameterList() {
		return parameterList;
	}

	public void parseComponentEx(ComponentDTO componentDTO) {
		componentType = COMPONENT_DEMO;
	}

	private void parseConnector(ComponentDTO comp) {
		if (comp.getRestriction().equals(Constants.TYPE_EXPANDABLECONNECTOR)) {
			return;
		}
		// parse extend clause first
		for (ComponentDTO extc : comp.getExtendComps()) {
			if (extc.getRestriction().equals(Constants.TYPE_CONNECTOR)) {
				// add connector to the list // prefix is null at the moment.
				connList.add(new Connector(extc.getName(), null, GraphicUtils
						.getTrans(extc.getOrigin(), extc.getExtent(), extc
								.getRotation()), extc.getIconGraphics2()));
			} else {
				parseConnector(extc);
			}
		}

		// check the include components then
		for (ComponentDTO incC : comp.getIncludeComps()) {
			if (incC.getRestriction().equals(Constants.TYPE_CONNECTOR)) {
				connList.add(new Connector(incC.getDeclarationName(), null,
						GraphicUtils.getTrans(incC.getOrigin(), incC
								.getExtent(), incC.getRotation()), incC
								.getIconGraphics2()));
			} else if (incC.getRestriction().equals(
					Constants.TYPE_EXPANDABLECONNECTOR)) {
				// special case;
				connList.add(new Connector(incC.getDeclarationName(), null,
						GraphicUtils.getTrans(incC.getOrigin(), incC
								.getExtent(), incC.getRotation()), incC
								.getIconGraphics2()));
			} else {
				// @@sam@@ It is no need to parse internal connector except it
				// is connector type
				// parseConnector(incC);
			}
		}
	}

	/*
	 * All type diagram paint
	 */
	public void parseDiagram(ComponentDTO comp) {
		componentType = DIAGRAM_TYPE;
		setCompData(comp);

		DiagramgraphicDTO diagramInfo = comp.getDiagramGraphic();

		if (diagramInfo != null && diagramInfo.getGraphicList().size() != 0) {// added
																				// by
																				// Gao
																				// Lei
																				// 29
																				// Sep.
																				// 2011
			String extents = diagramInfo.getExtent();
			if (extents != null)
				this.diagramSize = GraphicUtils.parseRectangle(extents);
			this.diagramString = diagramInfo.getGraphicList().toArray(
					new String[0]);
		}
		// @name is at type StringProperty
		this.name.setValue(comp.getName());
		this.restriction = comp.getRestriction();

		if (comp.getConnects().size() > 0) {
			parseComponentEx(comp);
		} else if (comp.getWholeName().contains("Examples")
				|| comp.getWholeName().contains("proteus")) {
			parseComponentEx(comp);
		}
	}

	private void parseIcon(ComponentDTO comp, List<String> iconList) {
		for (ComponentDTO extc : comp.getExtendComps()) {
			if (extc.getRestriction().equals(Constants.TYPE_CONNECTOR)) {
				// add connector to the list
				// sam added June 30, 2011.
				addToIconList(extc, iconList);
			} else {
				addToIconList(extc, iconList);
				parseIcon(extc, iconList);
			}
		}
	}

	public void parseModel(ComponentDTO modelDTO) {
		componentType = COMPONENT_DEMO;
		this.compData = modelDTO;
	}

	/**
	 * @param compData
	 *            the compData to set
	 */
	public void setCompData(ComponentDTO compData) {
		this.compData = compData;
		// @name is at type StringProperty
		this.name.setValue(compData.getName());
		this.type = compData.getWholeName();
		this.restriction = compData.getRestriction().toString();

		// parser the connector information. and retrive the IconString from the
		// extends components
		List<String> iconList = new ArrayList<String>();
		addToIconList(compData, iconList);

		IcongraphicDTO graphics = compData.getIconGraphicsDTO();
		String m = null;
		if (graphics != null && (m = graphics.getExtent()) != null) {
			extent = GraphicUtils.parseExtent(m);
		}

		parseIcon(compData, iconList);
		this.iconString = iconList.toArray(new String[0]);
		// parseConnector
		parseConnector(compData);
		// @sam Apr 11
		List<ParameterDTO> paraList = compData.getParameters();
		List<ModificationArgument> modifierList = compData.getSuperIncludeArgList();
		for (ParameterDTO para : paraList) {
			ParameterProperty pp = new ParameterProperty();
			pp.setName(para.getName());
			pp.setType(para.getType());
			pp.setAnnotationGroup(para.getAnnotationGroup());
			pp.setHeader(para.getHeader());
			pp.setInherited(para.isInherited());

			pp.setValue(para.getValue());
			pp.setOrignalValue(para.getValue());

			pp.setUnit(para.getUnit());
			pp.setOrignalUnit(para.getUnit());

			pp.setQuantity(para.getQuantity());
			pp.setOrignalQuantity(para.getQuantity());

			pp.setStart(para.getStart());
			pp.setOrignalStart(para.getStart());

			pp.setMin(para.getMin());
			pp.setOrignalMin(para.getMin());

			pp.setMax(para.getMax());
			pp.setOrignalMax(para.getMax());

			pp.setNominal(para.getNominal());
			pp.setOrignalNominal(para.getNominal());

			pp.setStateSelect(para.getStateSelect());
			pp.setOrignalStateSelect(para.getStateSelect());

			pp.setDisplayUnit(para.getDisplayUnit());
			pp.setOrignaldisplayUnit(para.getDisplayUnit());

			pp.setEnumList(para.getEnumList());

			pp.setPath(para.getPath());
			pp.setComment(para.getComment());
			pp.setDesc(para.getDesc());
			pp.setAnnotationTab(para.getAnnotationTab());
			pp.setAnnotationInitDialog(para.getAnnotationInitDialog());
			
			//check supermodifierlist
			for(ModificationArgument modifier: modifierList) {
				if(modifier.getName().equals(pp.getName())) {
					if(modifier.getValue()!=null)
						pp.setOrignalValue(null);
					if(modifier.getStart()!=null)
						pp.setOrignalStart(null);
					if(modifier.getDisplayUnit()!=null)
						pp.setOrignaldisplayUnit(null);
					if(modifier.getFixed()!=null)
						pp.setOrignalFixed(null);
					if(modifier.getMin()!=null)
						pp.setOrignalMin(null);
					if(modifier.getMax()!=null)
						pp.setOrignalMax(null);
					if(modifier.getNominal()!=null)
						pp.setOrignalNominal(null);
					if(modifier.getQuantity()!=null)
						pp.setOrignalQuantity(null);
					if(modifier.getUnit()!=null)
						pp.setOrignalUnit(null);
					if(modifier.getStateSelect()!=null)
						pp.setOrignalStateSelect(null);
					break;
				}
			}
			parameterList.add(pp);
		}
	}

	/**
	 * @param connList
	 *            the connList to set
	 */
	public void setConnList(List<Connector> connList) {
		this.connList = connList;
	}

	/**
	 * @param diagramSize
	 *            the diagramSize to set
	 */
	public void setDiagramSize(proteus.gwt.shared.graphics.Rectangle diagramSize) {
		this.diagramSize = diagramSize;
	}

	/**
	 * @param parameterList
	 *            the parameterList to set
	 */
	public void setParameterList(List<ParameterProperty> parameterList) {
		this.parameterList = parameterList;
	}

	private Component toComponent(ComponentDTO incc) {
		Component comp = (Component) new NewClassDefDialog()
				.getDefaultClassDef(incc.getRestriction());
		if (comp != null)
			comp.setCompData(incc);
		return comp;
	}

}
