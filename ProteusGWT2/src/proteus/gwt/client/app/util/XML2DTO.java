package proteus.gwt.client.app.util;

import proteus.gwt.client.app.AppController;
import proteus.gwt.client.proxy.AppContext;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ConnectDTO;
import proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO;
import proteus.gwt.shared.datatransferobjects.IcongraphicDTO;
import proteus.gwt.shared.datatransferobjects.ParameterDTO;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
/**
 * @author Gao Lei Created Aug 24, 2011
 */
public class XML2DTO {
	public static void parseComponentXML(Element element, ComponentDTO componentDTO){
		String name = element.getElementsByTagName("wholeName").item(0).getFirstChild().getNodeValue() ;
		AppController.logger.info("wholeName:\t"+name);
	}
	public static void parseComponentXML(Node node, ComponentDTO componentDTO) {
		// logger.info(node.getNodeName());

		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			String nodeName = child.getNodeName();
			if (nodeName.equals("name")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.setName(value);
			} else if (nodeName.equals("ID")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.setID(Long.valueOf(value));
			} else if (nodeName.equals("wholeName")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.setWholeName(value);
			} else if (nodeName.equals("extent")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.setExtent(value);
			} else if (nodeName.equals("origin")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.setOrigin(value);
			} else if (nodeName.equals("rotation")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.setRotation(value);
			} else if (nodeName.equals("restriction")) {
				String value = child.getFirstChild().getNodeValue();
				// logger.info(value);
				componentDTO.setRestriction(value);
			} else if (nodeName.equals("declarationName")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.setDeclarationName(value);
			} else if (nodeName.equals("arrayFormList")) {
				String value = child.getFirstChild().getNodeValue();
				componentDTO.getArrayFormList().add(value);
			} else if (nodeName.equals("diagramGraphic")) {
				NodeList diagramChildren = child.getChildNodes();
				DiagramgraphicDTO diagramgraphicDTO = new DiagramgraphicDTO();
				for (int j = 0; j < diagramChildren.getLength(); j++) {
					Node diagramChild = diagramChildren.item(j);
					String diagramName = diagramChild.getNodeName();
					if (diagramName.equals("extent")) {
						String value = diagramChild.getFirstChild()
								.getNodeValue();
						diagramgraphicDTO.setExtent(value);
					} else if (diagramName.equals("graphicList")) {
						String value = diagramChild.getFirstChild()
								.getNodeValue();
						diagramgraphicDTO.getGraphicList().add(value);

					} else if (diagramName.equals("preserveAspectRatio")) {
						String value = diagramChild.getFirstChild()
								.getNodeValue();
						diagramgraphicDTO.setPreserveAspectRatio(value);
					}
				}
				componentDTO.setDiagramGraphic(diagramgraphicDTO);
			} else if (nodeName.equals("parameters")) {
				NodeList paraChildren = child.getChildNodes();
				ParameterDTO parameterDTO = new ParameterDTO();
				for (int j = 0; j < paraChildren.getLength(); j++) {

					Node paraChild = paraChildren.item(j);
					String paraName = paraChild.getNodeName();
					if (paraName.equals("inherited")) {
						boolean inherited = paraChild.getFirstChild()
								.getNodeValue().equals("true") ? true : false;
						parameterDTO.setInherited(inherited);
					} else if (paraName.equals("name")) {
						String value = paraChild.getFirstChild().getNodeValue();
						parameterDTO.setName(value);
					} else if (paraName.equals("value")) {
						String value = paraChild.getFirstChild().getNodeValue();
						parameterDTO.setValue(value);
					}
				}
				componentDTO.getParameters().add(parameterDTO);
			} else if (nodeName.equals("extendComps")) {

				ComponentDTO extendComponentDTO = new ComponentDTO();
				parseComponentXML(child, extendComponentDTO);
				componentDTO.getExtendComps().add(extendComponentDTO);

			} else if (nodeName.equals("includeComps")) {
				ComponentDTO includeComponentDTO = new ComponentDTO();
				parseComponentXML(child, includeComponentDTO);
				componentDTO.getIncludeComps().add(includeComponentDTO);
			} else if (nodeName.equals("connects")) {
				ConnectDTO connectDTO = new ConnectDTO();
				NodeList connectChildren = child.getChildNodes();
				for (int j = 0; j < connectChildren.getLength(); j++) {
					Node connectChild = connectChildren.item(j);
					String connectName = connectChild.getNodeName();
					if (connectName.equals("startpin")) {
						String value = connectChild.getFirstChild()
								.getNodeValue();
						connectDTO.setStartpin(value);
					} else if (connectName.equals("endpin")) {
						String value = connectChild.getFirstChild()
								.getNodeValue();
						connectDTO.setEndpin(value);
					} else if (connectName.equals("value")) {
						String value = connectChild.getFirstChild()
								.getNodeValue();
						connectDTO.setValue(value);
					}
				}
				componentDTO.getConnects().add(connectDTO);
			} else if (nodeName.equals("iconGraphicList")) {
				IcongraphicDTO icongraphicDTO = new IcongraphicDTO();
				NodeList iconChildren = child.getChildNodes();
				for (int j = 0; j < iconChildren.getLength(); j++) {

					Node iconChild = iconChildren.item(j);
					String iconName = iconChild.getNodeName();
					if (iconName.equals("extent")) {
						String iconExtent = iconChild.getFirstChild()
								.getNodeValue();

						icongraphicDTO.setExtent(iconExtent);
					} else if (iconName.equals("graphics")) {
						String iconGraphics = iconChild.getFirstChild()
								.getNodeValue();

						icongraphicDTO.setGraphics(iconGraphics);
					} else if (iconName.equals("preserveAspectRatio")) {
						String iconPreserveAspectRatio = iconChild
								.getFirstChild().getNodeValue();

						icongraphicDTO
								.setPreserveAspectRatio(iconPreserveAspectRatio);
					}
				}
				componentDTO.getIconGraphicList().add(icongraphicDTO);
			}
		}// for
	}// parseComponentXML
}
