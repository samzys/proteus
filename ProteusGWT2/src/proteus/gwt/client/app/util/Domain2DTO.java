package proteus.gwt.client.app.util;

import com.google.gwt.core.client.GWT;

import proteus.gwt.shared.datatransferobjects.ModificationArgument;

public class Domain2DTO {

	public static proteus.gwt.shared.datatransferobjects.ComponentDTO convert(
			proteus.gwt.client.app.jsonObject.ComponentDTO component) {
		proteus.gwt.shared.datatransferobjects.ComponentDTO componentDTO = new proteus.gwt.shared.datatransferobjects.ComponentDTO();

		if (component.getName() != null)
			componentDTO.setName(component.getName());
		if (component.getWholeName() != null)
			componentDTO.setWholeName(component.getWholeName());
		componentDTO.setComment(component.getComment());
		if (component.getExtent() != null)
			componentDTO.setExtent(component.getExtent());
		if (component.getOrigin() != null)
			componentDTO.setOrigin(component.getOrigin());
		if (component.getRotation() != null)
			componentDTO.setRotation(component.getRotation());
		if (component.getRestriction() != null)
			componentDTO.setRestriction(component.getRestriction());
		if (component.getDeclarationName() != null)
			componentDTO.setDeclarationName(component.getDeclarationName());
		if (component.getID() != null)
			componentDTO.setID(new Long(component.getID()));
		//added  6 March, 2012 by sam
		componentDTO.setProtected_(component.getProtected_());
		componentDTO.setFinal_(component.getFinal_());
		componentDTO.setTyping(component.getTyping());
		componentDTO.setVariability(component.getVariability());
		componentDTO.setCausality(component.getCausality());

		for (int i = 0; i < component.getArrayFormList().length(); i++) {
			String str = component.getArrayFormList().get(i);
			componentDTO.getArrayFormList().add(str);
		}

		// icon
		for (int i = 0; i < component.getIconGraphicList().length(); i++) {
			proteus.gwt.client.app.jsonObject.IcongraphicDTO icon = component
					.getIconGraphicList().get(i);
			proteus.gwt.shared.datatransferobjects.IcongraphicDTO iconDTO = new proteus.gwt.shared.datatransferobjects.IcongraphicDTO();
			if (icon.getExtent() != null)
				iconDTO.setExtent(icon.getExtent());
			if (icon.getGraphics() != null)
				iconDTO.setGraphics(icon.getGraphics());
			if (icon.getGrid() != null)
				iconDTO.setGrid(icon.getGrid());
			if (icon.getPreserveAspectRatio() != null)
				iconDTO.setPreserveAspectRatio(icon.getPreserveAspectRatio());
			componentDTO.getIconGraphicList().add(iconDTO);
		}

		// diagram
		proteus.gwt.client.app.jsonObject.DiagramgraphicDTO diagram = component
				.getDiagramGraphic();
		if (diagram != null) {
			proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO diagramDTO = new proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO();
			if (diagram.getExtent() != null)
				diagramDTO.setExtent(diagram.getExtent());
			if (diagram.getGrid() != null)
				diagramDTO.setGrid(diagram.getGrid());
			if (diagram.getPreserveAspectRatio() != null)
				diagramDTO.setPreserveAspectRatio(diagram
						.getPreserveAspectRatio());
			for (int i = 0; i < diagram.getGraphicList().length(); i++) {
				String str = diagram.getGraphicList().get(i);
				diagramDTO.getGraphicList().add(str);
			}
			componentDTO.setDiagramGraphic(diagramDTO);
		}
		// parameters
		for (int i = 0; i < component.getParameters().length(); i++) {
			proteus.gwt.client.app.jsonObject.ParameterDTO parameter = component
					.getParameters().get(i);
			proteus.gwt.shared.datatransferobjects.ParameterDTO parameterDTO = new proteus.gwt.shared.datatransferobjects.ParameterDTO();
			if (parameter.getName() != null)
				parameterDTO.setName(parameter.getName());
			if (parameter.getValue() != null)
				parameterDTO.setValue(parameter.getValue());
			parameterDTO.setInherited(parameter.isInherited());
			if (parameter.getUnit() != null)
				parameterDTO.setUnit(parameter.getUnit());
			if (parameter.getQuantity() != null)
				parameterDTO.setQuantity(parameter.getQuantity());
			
			parameterDTO.setHeader(parameter.isHeader());
			
			parameterDTO.setStart(parameter.getStart());
			parameterDTO.setFixed(parameter.getFixed());
			parameterDTO.setMin(parameter.getMin());
			parameterDTO.setMax(parameter.getMax());
			parameterDTO.setDisplayUnit(parameter.getDisplayUnit());
			parameterDTO.setNominal(parameter.getNominal());
			parameterDTO.setStateSelect(parameter.getStateSelect());
			parameterDTO.setType(parameter.getType());
			parameterDTO.setAnnotationGroup(parameter.getAnnotationGroup());
			parameterDTO.setAnnotationTab(parameter.getAnnotationTab());
			
			parameterDTO.setVariability(parameter.getVariability());
			parameterDTO.setCausality(parameter.getCausality());
			parameterDTO.setTyping(parameter.getTyping());
			parameterDTO.setWholename(parameter.getWholename());
			
			for (int j = 0; j < parameter.getEnumList().length(); j++) {
				proteus.gwt.client.app.jsonObject.EnumerationDTO enu = parameter
						.getEnumList().get(j);
				proteus.gwt.shared.datatransferobjects.EnumerationDTO enumDTO = new proteus.gwt.shared.datatransferobjects.EnumerationDTO(
						enu.getName(), enu.getComment());
				parameterDTO.getEnumList().add(enumDTO);
			}
			parameterDTO.setPath(parameter.getPath());
			// System.out.println(parameter.getName()+", "+parameter.getPath());
			parameterDTO.setComment(parameter.getComment());
			parameterDTO.setDesc(parameter.getDesc());
			parameterDTO.setAnnotationInitDialog(parameter
					.getAnnotationInitDialog());

			// if (componentDTO.getWholeName().equals(
			// "Modelica.Mechanics.Rotational.Components.Damper")) {
			// System.err.println(parameter.getName() + ", "
			// + parameter.getValue() + ", " + parameter.getStart());
			// }
			componentDTO.getParameters().add(parameterDTO);
		}

		// modification data
		// Window.alert("before ");
		if (component.getSuperIncludeArgList() != null
				&& component.getSuperIncludeArgList().length() > 0)
			for (int i = 0; i < component.getSuperIncludeArgList().length(); i++) {
				proteus.gwt.client.app.jsonObject.ModificationArgument modification = component
						.getSuperIncludeArgList().get(i);
				proteus.gwt.shared.datatransferobjects.ModificationArgument modificationDTO = new ModificationArgument();
				modificationDTO.setName(modification.getName());
				modificationDTO.setValue(modification.getValue());
				modificationDTO.setStart(modification.getStart());
				modificationDTO.setFixed(modification.getFixed());
				modificationDTO.setDisplayUnit(modification.getDisplayUnit());
				modificationDTO.setMax(modification.getMax());
				modificationDTO.setMin(modification.getMin());
				modificationDTO.setNominal(modification.getNominal());
				modificationDTO.setQuantity(modification.getQuantity());
				modificationDTO.setUnit(modification.getUnit());
				modificationDTO.setStateSelect(modification.getStateSelect());

				// if (componentDTO.getWholeName().equals(
				// "Modelica.Mechanics.Rotational.Components.Damper")) {
				// System.err.println(modificationDTO.getName() + ", "
				// + modificationDTO.getValue() + ", "
				// + modificationDTO.getStart());
				// }
				componentDTO.getSuperIncludeArgList().add(modificationDTO);
			}

		// Window.alert("end");
		// connect
		for (int i = 0; i < component.getConnects().length(); i++) {
			proteus.gwt.client.app.jsonObject.ConnectDTO connect = component
					.getConnects().get(i);
			proteus.gwt.shared.datatransferobjects.ConnectDTO connectDTO = new proteus.gwt.shared.datatransferobjects.ConnectDTO();
			connectDTO.setStartpin(connect.getStartpin());
			connectDTO.setEndpin(connect.getEndpin());
			if (connect.getValue() != null)
				connectDTO.setValue(connect.getValue());
			componentDTO.getConnects().add(connectDTO);
		}

		// include
		for (int i = 0; i < component.getIncludeComps().length(); i++) {
			proteus.gwt.shared.datatransferobjects.ComponentDTO includeComponentDTO = convert(component
					.getIncludeComps().get(i));
			componentDTO.getIncludeComps().add(includeComponentDTO);
		}

		// if (Utils.DEBUG)
		// GWT.log(component.getExtendComps().length()+"");
		// extend
		for (int i = 0; i < component.getExtendComps().length(); i++) {
			proteus.gwt.shared.datatransferobjects.ComponentDTO extendComponentDTO = convert(component
					.getExtendComps().get(i));
			componentDTO.getExtendComps().add(extendComponentDTO);
		}

		return componentDTO;
	}
}
