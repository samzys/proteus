package com.infoscient.proteus.db.businesslogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.datatransferobjects.ComponentDTO;
import com.infoscient.proteus.db.datatransferobjects.ConnectDTO;
import com.infoscient.proteus.db.datatransferobjects.DiagramgraphicDTO;
import com.infoscient.proteus.db.datatransferobjects.IcongraphicDTO;
import com.infoscient.proteus.db.datatransferobjects.ParameterDTO;
import com.infoscient.proteus.db.domain.Component;
import com.infoscient.proteus.db.domain.Componentconnect;
import com.infoscient.proteus.db.domain.Diagramgraphic;
import com.infoscient.proteus.db.domain.Diagramgraphicsmodification;
import com.infoscient.proteus.db.domain.Extendcomponent;
import com.infoscient.proteus.db.domain.Extendmodification;
import com.infoscient.proteus.db.domain.Icongraphic;
import com.infoscient.proteus.db.domain.Includecomponent;
import com.infoscient.proteus.db.domain.Includemodification;
import com.infoscient.proteus.db.domain.Parameter;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
public class DomainComponentProxy {

	public static Long getId(String wholename, EntityManager em)
			throws ComponentNotFoundException {

		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.wholename= :wholename ");
		query.setParameter("wholename", wholename);

		try {
			return ((Component) query.getSingleResult()).getId();
		} catch (NoResultException e) {
			System.err.println(wholename);
			throw new ComponentNotFoundException();
		}
	}

	public static Long getId(String wholename)
			throws ComponentNotFoundException {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.wholename= :wholename ");
		query.setParameter("wholename", wholename);

		try {
			return ((Component) query.getSingleResult()).getId();
		} catch (NoResultException e) {
			System.err.println(wholename);
			throw new ComponentNotFoundException();
		}
	}

	public static String getWholeName(Long id)
			throws ComponentNotFoundException {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.id= :id ");
		query.setParameter("id", id);

		try {
			return ((Component) query.getSingleResult()).getWholename();
		} catch (NoResultException e) {
			throw new ComponentNotFoundException();
		}
	}

	public static ComponentDTO getComponent(String wholeName, EntityManager em)
			throws ComponentNotFoundException {
		ComponentDTO componentDTO = new ComponentDTO();
		Long id = getId(wholeName);
		ModifacationData parentdata = new ModifacationData(id);

		componentDTO = getComponent(parentdata, null, em);

		return componentDTO;
	}

	public static ComponentDTO getComponent(ModifacationData modificationdata,
			List<ParameterDTO> inheritParameterList, EntityManager em)
			throws ComponentNotFoundException {
		if (inheritParameterList == null) {
			inheritParameterList = new ArrayList<ParameterDTO>();
		}
		// if (modificationdata.declarationName != null
		// && modificationdata.declarationName.equals("brake1")) {
		//
		// System.out.println(modificationdata.declarationName);
		// System.out.println(modificationdata.id);
		// System.out.println(modificationdata.arguments);
		// }

		ComponentDTO componentDTO = new ComponentDTO();
		Component component;

		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.id= :id ");
		query.setParameter("id", modificationdata.id);
		try {
			component = (Component) query.getSingleResult();
		} catch (NoResultException e) {
			throw new ComponentNotFoundException();
		}

		componentDTO.setID(component.getId());
		componentDTO.setName(component.getName());
		componentDTO.setWholeName(component.getWholename());
		componentDTO.setExtent(modificationdata.extent);
		componentDTO.setOrigin(modificationdata.origin);
		componentDTO.setRotation(modificationdata.rotation);
		componentDTO.setDeclarationName(modificationdata.declarationName);
		componentDTO.setRestriction(component.getRestriction().getName());

		if (modificationdata.arrayForm != null
				&& !modificationdata.arrayForm.equals("")) {
			String list[] = modificationdata.arrayForm.split(",");
			for (String str : list) {
				componentDTO.getArrayFormList().add(str);
			}
		}
		// icon

		// new way add extent etc.
		for (Icongraphic icon : component.getIcongraphics()) {
			IcongraphicDTO iconDTO = new IcongraphicDTO();
			iconDTO.setGraphics(new String(icon.getValue()));
			iconDTO.setExtent(icon.getExtent());
			iconDTO.setPreserveAspectRatio(icon.getPreserveAspectRatio());
			iconDTO.setGrid(icon.getGrid());
			componentDTO.getIconGraphicList().add(iconDTO);
		}

		// diagram

		// new way add extent etc.
		DiagramgraphicDTO diagramDTO = new DiagramgraphicDTO();
		Diagramgraphicsmodification diagramModification = component
				.getDiagramgraphicsmodification();
		if (diagramModification != null) {
			diagramDTO.setExtent(component.getDiagramgraphicsmodification()
					.getExtent());
			diagramDTO.setGrid(component.getDiagramgraphicsmodification()
					.getGrid());
			diagramDTO.setPreserveAspectRatio(component
					.getDiagramgraphicsmodification().getPreserveAspectRatio());
		}
		for (Diagramgraphic diagram : component.getDiagramgraphics()) {
			diagramDTO.getGraphicList().add(new String(diagram.getValue()));
		}
		componentDTO.setDiagramGraphic(diagramDTO);
		// connect
		for (Componentconnect connect : component.getComponentConnect()) {

			ConnectDTO connectDTO = new ConnectDTO();
			connectDTO.setValue(new String(connect.getValue()));
			connectDTO.setStartpin(connect.getStartpin());
			connectDTO.setEndpin(connect.getEndpin());
			componentDTO.getConnects().add(connectDTO);

		}

		// include component
		for (Includecomponent includeComponent : component
				.getIncludeComponents()) {
			ModifacationData includedata = new ModifacationData();
			String ifFlagName = includeComponent.getIfFlagName();

			if (ifFlagName != null) {
				boolean ifFlagTrue = isIfFlagTrue(ifFlagName,
						component.getParameters(), modificationdata.arguments);
				//		if (modificationdata.id.equals(1773l)) {
				//			System.out.println(ifFlagName);
				//			System.out.println(ifFlagTrue);
				//		}
				if (!ifFlagTrue)
					continue;
			}
			includedata.id = includeComponent.getTypeId();
			includedata.extent = includeComponent.getExtent();
			includedata.origin = includeComponent.getOrigin();
			includedata.rotation = includeComponent.getRotation();
			includedata.declarationName = includeComponent.getName();
			includedata.arrayForm = includeComponent.getArrayForm();
			for (Includemodification modification : includeComponent
					.getIncludemodifications()) {
				String name = modification.getName();
				String value = modification.getValue();
				includedata.arguments.put(name, value);
			}
			//recursive call to parse all levels comps.
			componentDTO.getIncludeComps().add(
					getComponent(includedata, null, em));
		}

		// extend component
		for (Extendcomponent extendComponent : component.getExtendComponents()) {
			ModifacationData extendData = new ModifacationData();
			extendData.id = extendComponent.getTypeId();
			extendData.extent = extendComponent.getExtent();
			extendData.rotation = extendComponent.getRotation();
			for (Extendmodification modification : extendComponent
					.getExtendmodifications()) {
				String name = modification.getName();
				String value = modification.getValue();
				extendData.arguments.put(name, value);
			}
			extendData.arguments.putAll(modificationdata.arguments);

			componentDTO.getExtendComps().add(
					getComponent(extendData, inheritParameterList, em));
		}

		// parameters
		for (ParameterDTO parameter : inheritParameterList) {
			ParameterDTO parameterDTO = new ParameterDTO();
			parameterDTO.setName(parameter.getName());
			parameterDTO.setValue(parameter.getValue());
			parameterDTO.setInherited(true);
			componentDTO.getParameters().add(parameterDTO);
		}

		Map<String, String> parentParameter = modificationdata.arguments;
		for (Parameter parameter : component.getParameters()) {

			String name = parameter.getName();
			String value = parameter.getValue();
			if (parentParameter != null && parentParameter.containsKey(name)) {
				value = parentParameter.get(name);
			}

			ParameterDTO parameterDTO = new ParameterDTO();
			parameterDTO.setName(name);
			parameterDTO.setValue(value);
			parameterDTO.setInherited(false);

			componentDTO.getParameters().add(parameterDTO);

			ParameterDTO inheritParameterDTO = new ParameterDTO();
			inheritParameterDTO.setName(name);
			inheritParameterDTO.setValue(value);
			inheritParameterDTO.setInherited(true);
			if (inheritParameterList != null) {
				inheritParameterList.add(inheritParameterDTO);
			}
		}
		// if (modificationdata.declarationName != null
		// && modificationdata.declarationName.equals("brake1")) {
		//
		// System.out.println(componentDTO.getParameters());
		//
		// }
		return componentDTO;
	}

	public static ComponentDTO getComponent(String wholeName)
			throws ComponentNotFoundException {
		ComponentDTO componentDTO = new ComponentDTO();

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		componentDTO = getComponent(wholeName, em);
		em.close();
		emf.close();

		return componentDTO;
	}

	

	

	private static boolean isIfFlagTrue(String ifFlagname,
			List<Parameter> parameters, Map<String, String> arguments) {

		boolean ret = true;
		String flag = "";
		for (Parameter pa : parameters) {
			String name = pa.getName();
			if (name.equals(ifFlagname)) {
				flag = pa.getValue();
			}
		}

		if (arguments != null && arguments.containsKey(ifFlagname)) {
			flag = arguments.get(ifFlagname);
		}
		if (flag.equals("true")) {
			ret = true;
		} else if (flag.equals("false")) {
			ret = false;
		}
		return ret;
	}
}

class ModifacationData {
	public Long id;
	public String extent;
	public String origin;
	public String rotation;
	public String declarationName;
	public String arrayForm;
	public Map<String, String> arguments = new HashMap<String, String>();

	public ModifacationData() {
	}

	public ModifacationData(Long id) {
		this.id = id;
	}
}
