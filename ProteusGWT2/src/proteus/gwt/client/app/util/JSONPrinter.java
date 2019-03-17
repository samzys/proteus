package proteus.gwt.client.app.util;

import proteus.gwt.client.app.AppController;
import proteus.gwt.client.app.jsonObject.ComponentDTO;
import proteus.gwt.client.app.jsonObject.ConnectDTO;
import proteus.gwt.client.app.jsonObject.DiagramgraphicDTO;
import proteus.gwt.client.app.jsonObject.IcongraphicDTO;
import proteus.gwt.client.app.jsonObject.ParameterDTO;

public class JSONPrinter {
	public static void print(ComponentDTO component) {
		StringBuffer str = new StringBuffer();

//		str.append("*******************DiagramGraphic*****************\n");
//		DiagramgraphicDTO diagram = component.getDiagramGraphic();
//
//		for (int i = 0; i < diagram.getGraphicList().length(); i++) {
//			str.append("GraphicList\t" + diagram.getGraphicList().get(i) + "\n");
//		}
//		str.append("Extent\t" + diagram.getExtent() + "\n");
//		str.append("Grid\t" + diagram.getGrid() + "\n");
//		str.append("PreserveAspectRatio\t" + diagram.getPreserveAspectRatio()
//				+ "\n");
//
//		str.append("*******************IconGraphicList*****************" + "\n");
//		for (int i = 0; i < component.getIconGraphicList().length(); i++) {
//			IcongraphicDTO icon = component.getIconGraphicList().get(i);
//
//			str.append("Extent\t" + icon.getGraphics() + "\n");
//
//			str.append("Extent\t" + icon.getExtent() + "\n");
//			str.append("Grid\t" + icon.getGrid() + "\n");
//			str.append("PreserveAspectRatio\t" + icon.getPreserveAspectRatio()
//					+ "\n");
//
//		}
//
//		str.append("*******************Parameters*****************" + "\n");
//		for (int i = 0; i < component.getParameters().length(); i++) {
//			ParameterDTO parameter = component.getParameters().get(i);
//			str.append("Name\t" + parameter.getName() + "\n");
//			str.append("Value\t" + parameter.getValue() + "\n");
//			str.append("herited\t" + parameter.isInherited() + "\n\n");
//
//		}
//
//		str.append("*******************Connect*****************\n");
//		for (int i = 0; i < component.getConnects().length(); i++) {
//			ConnectDTO connect = component.getConnects().get(i);
//			str.append("StartPin\t" + connect.getStartpin() + "\n");
//			str.append("EndPin\t" + connect.getEndpin() + "\n");
//			str.append("Value\t" + connect.getValue() + "\n\n");
//
//		}

//		str.append("*******************IncludeComps*****************\n");
		for (int i = 0; i < component.getIncludeComps().length(); i++) {
			//str.append(component.getIncludeComps().get(i).getDeclarationName() + "\n");
			print(component.getIncludeComps().get(i));
		}

//		AppController.logger.finest(str.toString());
	}
}
