package proteus.gwt.shared.modelica;

import proteus.gwt.shared.datatransferobjects.GetComponentDataResponseDTO;

public class DemoIcon {
	static String[] resistorDiagram = {
			"= {Rectangle(extent = {{-70, 30}, {70, -30}}, lineColor = {0, 0, 255}), Line(points = {{-96, 0}, {-70, 0}}, color = {0, 0, 255}), Line(points = {{70, 0}, {96, 0}}, color = {0, 0, 255})} ",
			" = {Line(points = {{-110, 20}, {-85, 20}}, color = {160, 160, 164}),Polygon(points = {{-95, 23}, {-85, 20}, {-95, 17}, {-95, 23}}, lineColor = {160, 160, 164}, fillColor = {160, 160, 164}, fillPattern = FillPattern.Solid), Line(points = {{90, 20}, {115, 20}}, color = {160, 160, 164}), Line(points = {{-125, 0}, {-115, 0}}, color = {160, 160, 164}), Line(points = {{-120, -5}, {-120, 5}}, color = {160, 160, 164}), Text(extent = {{-110, 25}, {-90, 45}}, lineColor = {160, 160, 164}, textString = \"i\"), Polygon(points = {{105, 23}, {115, 20}, {105, 17}, {105, 23}}, lineColor = {160, 160, 164}, fillColor = {160, 160, 164}, fillPattern = FillPattern.Solid), Line(points = {{115, 0}, {125, 0}}, color = {160, 160, 164}), Text(extent = {{90, 45}, {110, 25}}, lineColor = {160, 160, 164}, textString = \"i\")}",
			"={Rectangle(extent={{-104,4},{-96,-4}},lineColor={0, 0, 255},fillColor={0, 0, 255},fillPattern=FillPattern.Solid),Text(extent={{-116,11},{-96,5}},lineColor={0, 0, 255},textString=\"p\")}",
			"={Rectangle(extent={{96,4},{104,-4}},lineColor={0, 0, 255},fillColor={255, 255, 255},fillPattern=FillPattern.Solid),Text(extent={{96,11},{116,5}},textString=\"n\",lineColor={0, 0, 255})}" };

	static String[] libIcon = { "= {Rectangle(extent = {{-100, -100}, {80, 50}}, fillColor = {235, 235, 235}, fillPattern = FillPattern.Solid, lineColor = {0, 0, 255}), Polygon(points = {{-100, 50}, {-80, 70}, {100, 70}, {80, 50}, {-100, 50}}, fillColor = {235, 235, 235}, fillPattern = FillPattern.Solid, lineColor = {0, 0, 255}), Polygon(points = {{100, 70}, {100, -80}, {80, -100}, {80, 50}, {100, 70}}, fillColor = {235, 235, 235}, fillPattern = FillPattern.Solid, lineColor = {0, 0, 255}), Text(extent = {{-85, 35}, {65, -85}}, lineColor = {0, 0, 255}, textString = \"Library\"), Text(extent = {{-120, 122}, {120, 73}}, lineColor = {255, 0, 0}, textString = \"%name\")}" };

	static String[] resistorIcon = new String[] {
			"={Rectangle(extent={{-70,30},{70,-30}},lineColor={0,0,255},fillColor={255,255,255},fillPattern=FillPattern.Solid),Line(points={{-90,0},{-70,0}}, color={0,0,255}),Line(points={{70,0},{90,0}}, color={0,0,255}),Text(extent={{-144,-40},{142,-72}},lineColor={0,0,0},textString=\"R=%R\"),Text(extent={{-152,87},{148,47}},textString=\"%name\",lineColor={0,0,255})}"};
			

	static String[] resistorConnector = new String[] {
			"={Rectangle(extent={{-100,100},{100,-100}},lineColor={0,0,255},fillColor={0,0,255},fillPattern=FillPattern.Solid)}",
			"={Rectangle(extent={{-100,100},{100,-100}},lineColor={0,0,255},fillColor={255,255,255},fillPattern=FillPattern.Solid)}" };

	// capacitor
	static String[] capacitorIcon = new String[] {
			"={Line(points={{-14,28},{-14,-28}},thickness=0.5,color={0,0,255}),Line(points={{14,28},{14,-28}},thickness=0.5,color={0,0,255}),Line(points={{-90,0},{-14,0}}, color={0,0,255}),Line(points={{14,0},{90,0}}, color={0,0,255}),Text(extent={{-136,-60},{136,-92}},lineColor={0,0,0},textString=\"C=%C\"),Text(extent={{-150,85},{150,45}},textString=\"%name\",lineColor={0,0,255})}"};			
	static String[] capacitorConnector = new String[] {
			"={Rectangle(extent={{-100,100},{100,-100}},lineColor={0,0,255},fillColor={0,0,255},fillPattern=FillPattern.Solid)}",
			"={Rectangle(extent={{-100,100},{100,-100}},lineColor={0,0,255},fillColor={255,255,255},fillPattern=FillPattern.Solid)}" };

	public static final String RESISTOR = "Modelica.Electrical.Analog.Basic.Resistor",
			LIBRARY = "library",
			CAPACITOR = "Modelica.Electrical.Analog.Basic.Capacitor";
	private static GetComponentDataResponseDTO resistorComp, libraryComp,
			capacitorComp;

	static {
		DemoTrans connTrans[] = new DemoTrans[2];
		connTrans[0] = new DemoTrans();
		connTrans[1] = new DemoTrans();

		connTrans[0].extent = "{{-110,-10},{-90,10}}";
		connTrans[0].rotation = "0";
		connTrans[1].extent = "{{110,-10},{90,10}}";
		connTrans[1].rotation = "0";

		// resistor
		resistorComp = new GetComponentDataResponseDTO();
		resistorComp.name = RESISTOR;
		resistorComp.restriction = "model";
		resistorComp.iconString = resistorIcon;
		resistorComp.connectorString = resistorConnector;
		resistorComp.connTrans = connTrans;
		resistorComp.diagramString = resistorDiagram;
		resistorComp.setSuccess(true);
		// capacitor
		capacitorComp = new GetComponentDataResponseDTO();
		capacitorComp.name = "Capacitor";
		capacitorComp.restriction = "model";
		capacitorComp.iconString = capacitorIcon;
		capacitorComp.connectorString = capacitorConnector;
		capacitorComp.connTrans = connTrans;
//		capacitorComp.setSuccess(true);
		// library
		libraryComp = new GetComponentDataResponseDTO();
		libraryComp.name = "Library";
		libraryComp.restriction = "model";
		libraryComp.iconString = libIcon;
	}

	public static GetComponentDataResponseDTO getComponent(String c) {

		if (c.equals(RESISTOR)) {
			return resistorComp;
		} else if (c.equals(LIBRARY)) {
			return libraryComp;
		} else if (c.equals(CAPACITOR)) {
			return capacitorComp;
		}
		return null;
	}

}
