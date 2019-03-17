package proteus.gwt.server.businesslogic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Componentconnect;
import proteus.gwt.server.domain.Diagramgraphic;
import proteus.gwt.server.domain.Diagramgraphicsmodification;
import proteus.gwt.server.domain.Enumeration;
import proteus.gwt.server.domain.Extendcomponent;
import proteus.gwt.server.domain.Extendmodification;
import proteus.gwt.server.domain.Icongraphic;
import proteus.gwt.server.domain.Includecomponent;
import proteus.gwt.server.domain.Includemodification;
import proteus.gwt.server.domain.Parameter;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ConnectDTO;
import proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO;
import proteus.gwt.shared.datatransferobjects.EnumerationDTO;
import proteus.gwt.shared.datatransferobjects.IcongraphicDTO;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;
import proteus.gwt.shared.datatransferobjects.ModificationData;
import proteus.gwt.shared.datatransferobjects.ParameterDTO;

/**
 * @author Gao Lei
 */
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

	public static ComponentDTO getComponent(String wholeName, EntityManager em)
			throws ComponentNotFoundException {
		ComponentDTO componentDTO = new ComponentDTO();
		Long id = getId(em, wholeName);
		ModificationData parentdata = new ModificationData(id);
		componentDTO = getComponent(parentdata, null, em);

		// List<ParameterDTO> paralIst = componentDTO.getParameters();
		// if(paralIst!=null&&paralIst.size()>0) {
		// for(ParameterDTO p: paralIst) {
		// System.err.println(p.getName()+", "+p.getPath());
		// }
		// }
		return componentDTO;
	}

	public static Component getComponent(Long id, EntityManager em)
			throws ComponentNotFoundException {
		Component comp = null;
		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.id= :id ");
		query.setParameter("id", id);

		try {
			comp = (Component) query.getSingleResult();
		} catch (NoResultException e) {
			throw new ComponentNotFoundException();
		}
		return comp;
	}

	/**
	 * @param modificationdata
	 * @param inheritParameterList
	 * @param em
	 * @return
	 * @throws ComponentNotFoundException
	 * @extend clause can generate inheritParameterList and modificationdata
	 * @Include clause can only generate modification data.
	 */
	@SuppressWarnings("unchecked")
	public static ComponentDTO getComponent(ModificationData modificationdata,
			List<ParameterDTO> inheritParameterList, EntityManager em)
			throws ComponentNotFoundException {

		if (inheritParameterList == null) {
			inheritParameterList = new ArrayList<ParameterDTO>();
		}

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
		componentDTO.setComment(component.getComment());
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
			ModificationData includedata = new ModificationData();
			String ifFlagName = includeComponent.getIfFlagName();

			if (ifFlagName != null) {
				boolean ifFlagTrue = isIfFlagTrue(ifFlagName,
						component.getParameters(), modificationdata.argsList);

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
				String start = modification.getStart();
				includedata.argsList.add(new ModificationArgument(name, value,
						start, modification.getFixed(), modification.getMin(),
						modification.getMax(), modification.getUnit(),
						modification.getDisplayUnit(), modification
								.getQuantity(), modification.getNominal(),
						modification.getStateSelect()));
			}
			componentDTO.getIncludeComps().add(
					getComponent(includedata, null, em));
		}

		// extend component
		for (Extendcomponent extendComponent : component.getExtendComponents()) {
			ModificationData extendData = new ModificationData();
			extendData.id = extendComponent.getTypeId();
			extendData.extent = extendComponent.getExtent();
			extendData.rotation = extendComponent.getRotation();
			for (Extendmodification modification : extendComponent
					.getExtendmodifications()) {
				String name = modification.getName();
				String value = modification.getValue();
				String start = modification.getStart();
				extendData.argsList.add(new ModificationArgument(name, value,
						start, modification.getFixed(), modification.getMin(),
						modification.getMax(), modification.getUnit(),
						modification.getDisplayUnit(), modification
								.getQuantity(), modification.getNominal(),
						modification.getStateSelect()));

				// extendData.arguments.put(name, value);
			}
			extendData.argsList.addAll(modificationdata.argsList);

			componentDTO.getExtendComps().add(
					getComponent(extendData, inheritParameterList, em));
		}
		// System.err.println(modificationdata.declarationName);

		// inherit parameters
		for (ParameterDTO parameter : inheritParameterList) {
			ParameterDTO parameterDTO = new ParameterDTO();
			parameterDTO.setName(parameter.getName());
			parameterDTO.setValue(parameter.getValue());
			parameterDTO.setInherited(true);
			parameterDTO.setUnit(parameter.getUnit());
			parameterDTO.setQuantity(parameter.getQuantity());
			parameterDTO.setStart(parameter.getStart());
			parameterDTO.setHeader(parameter.getHeader());
			parameterDTO.setFixed(parameter.getFixed());
			parameterDTO.setMin(parameter.getMin());
			parameterDTO.setMax(parameter.getMax());
			parameterDTO.setDisplayUnit(parameter.getDisplayUnit());
			parameterDTO.setNominal(parameter.getNominal());
			parameterDTO.setStateSelect(parameter.getStateSelect());
			parameterDTO.setType(parameter.getType());
			parameterDTO.setAnnotationGroup(parameter.getAnnotationGroup());
			parameterDTO.setAnnotationTab(parameter.getAnnotationTab());
			parameterDTO.setPath(parameter.getPath());
			parameterDTO.setComment(parameter.getComment());
			parameterDTO.setDesc(parameter.getDesc());
			parameterDTO.setEnumList(parameter.getEnumList());
			parameterDTO.setAnnotationInitDialog(parameter
					.getAnnotationInitDialog());
			parameterDTO.setTyping(parameter.getTyping());
			parameterDTO.setCausality(parameter.getCausality());
			parameterDTO.setVariability(parameter.getVariability());
			parameterDTO.setWholename(parameter.getWholename());
			// parameterDTO
			componentDTO.getParameters().add(parameterDTO);
		}
		List<ModificationArgument> parentParameter = modificationdata.argsList;

		// Internal parameters.
		// sort this list in order to display the parameter list
		List<Parameter> paraHolder = component.getParameters();
		Collections.sort(paraHolder, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Parameter p1 = (Parameter) o1;
				Parameter p2 = (Parameter) o2;
				return (int) (p1.getId() - p2.getId());
			}
		});

		for (int i = 0; i < paraHolder.size(); i++) {
			Parameter parameter = paraHolder.get(i);
			String name = parameter.getName();
			String value = parameter.getValue();
			String start = parameter.getStart();
			String unit = parameter.getUnit();
			String fixed = parameter.getFixed();
			String displayUnit = parameter.getDisplayUnit();
			String min = parameter.getMin();
			String max = parameter.getMax();
			String nominal = parameter.getNominal();
			String stateSelect = parameter.getStateSelect();
			String quantity = parameter.getQuantity();
			String type = parameter.getType();
			String group = parameter.getAnnotationGroup();
			String tab = parameter.getAnnotationTab();
			String initDialog = parameter.getAnnotationInitDialog();
			String typing = parameter.getTyping();
			String causality = parameter.getCausality();
			String variability = parameter.getVariability();

			Long typeID = parameter.getType_id();
			String typeName = null;
			if (typeID != null) {
				try {
					typeName = getTypeWholename(em, typeID);
				} catch (Exception e) {
				}
			}
			if (typeName == null)
				typeName = parameter.getType();
			String wholename = typeName;
			
			if (parentParameter != null && name != null) {
				ModificationArgument arg = containsParameter(parentParameter,
						name);
				if (arg != null) {
					// modified = true;
					if (arg.value != null)
						value = arg.value;
					if (arg.start != null)
						start = arg.start;
					if (arg.fixed != null)
						fixed = arg.fixed;
					if (arg.unit != null)
						unit = arg.unit;
					if (arg.displayUnit != null)
						displayUnit = arg.displayUnit;
					if (arg.min != null)
						min = arg.min;
					if (arg.max != null)
						max = arg.max;
					if (arg.nominal != null)
						nominal = arg.nominal;
					if (arg.stateSelect != null)
						stateSelect = arg.stateSelect;
					if (arg.quantity != null)
						quantity = arg.quantity;
				}
			}

			ParameterDTO parameterDTO = new ParameterDTO();
			parameterDTO.setHeader(parameter.isHeader());
			parameterDTO.setInherited(false);
			parameterDTO.setName(name);
			parameterDTO.setValue(value);
			parameterDTO.setUnit(unit);
			parameterDTO.setQuantity(quantity);
			parameterDTO.setStart(start);
			parameterDTO.setFixed(fixed);
			parameterDTO.setDisplayUnit(displayUnit);
			parameterDTO.setMin(min);
			parameterDTO.setMax(max);
			parameterDTO.setNominal(nominal);
			parameterDTO.setStateSelect(stateSelect);
			parameterDTO.setType(type);
			parameterDTO.setAnnotationGroup(group);
			parameterDTO.setAnnotationTab(tab);
			parameterDTO.setDesc(parameter.getParaDesc());
			parameterDTO.setAnnotationInitDialog(initDialog);

			parameterDTO.setTyping(typing);
			parameterDTO.setCausality(causality);
			parameterDTO.setVariability(variability);
			parameterDTO.setWholename(wholename);

			List<EnumerationDTO> enList = new ArrayList<EnumerationDTO>();
			String path = null, comment = null;
			if (type != null) {
				Long typeId = parameter.getType_id();
				if (typeId != null) {
					Component comp = getComponent(typeId, em);
					List<Enumeration> enumList = comp.getEnumList();
					// save enumlist
					if (enumList != null && enumList.size() > 0) {
						for (Enumeration e : enumList) {
							enList.add(new EnumerationDTO(e.getName(), e
									.getComment()));
						}
					}
					// save path and comment
					path = comp.getWholename();
					comment = comp.getComment();
				} else {
					path = parameter.getType();
				}
				parameterDTO.setPath(path);
				parameterDTO.setComment(comment);
			}

			if (enList.size() > 0) {
				parameterDTO.setEnumList(enList);
			}

			componentDTO.getParameters().add(parameterDTO);

			ParameterDTO inheritParameterDTO = new ParameterDTO();
			inheritParameterDTO.setInherited(true);
			inheritParameterDTO.setHeader(parameter.isHeader());
			inheritParameterDTO.setName(name);
			inheritParameterDTO.setValue(value);
			inheritParameterDTO.setUnit(unit);
			inheritParameterDTO.setQuantity(quantity);
			inheritParameterDTO.setStart(start);
			inheritParameterDTO.setFixed(fixed);
			inheritParameterDTO.setDisplayUnit(displayUnit);
			inheritParameterDTO.setMin(min);
			inheritParameterDTO.setMax(max);
			inheritParameterDTO.setNominal(nominal);
			inheritParameterDTO.setStateSelect(stateSelect);
			inheritParameterDTO.setType(type);
			inheritParameterDTO.setAnnotationGroup(group);
			inheritParameterDTO.setAnnotationTab(tab);
			inheritParameterDTO.setDesc(parameter.getParaDesc());
			inheritParameterDTO.setPath(path);
			inheritParameterDTO.setComment(comment);
			inheritParameterDTO.setAnnotationInitDialog(initDialog);

			inheritParameterDTO.setTyping(typing);
			inheritParameterDTO.setCausality(causality);
			inheritParameterDTO.setVariability(variability);
			inheritParameterDTO.setWholename(wholename);

			if (enList.size() > 0) {
				inheritParameterDTO.setEnumList(enList);
			}
			if (inheritParameterList != null) {
				inheritParameterList.add(inheritParameterDTO);
			}
		}

		return componentDTO;
	}

	private static String getTypeWholename(EntityManager em, Long typeID)
			throws ComponentNotFoundException {
		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.id= :id ");
		query.setParameter("id", typeID);
		Component component = null;
		try {
			component = (Component) query.getSingleResult();
		} catch (NoResultException e) {
			throw new ComponentNotFoundException();
		}
		return component.getWholename();
	}

	private static ModificationArgument containsParameter(
			List<ModificationArgument> parentParameter, String name) {
		if (parentParameter != null && parentParameter.size() > 0) {
			for (ModificationArgument modArg : parentParameter) {
				if (modArg.name.equals(name)) {
					return modArg;
				}
			}
		}
		return null;
	}

	public static ComponentDTO getComponent(String wholeName)
			throws ComponentNotFoundException {
		ComponentDTO componentDTO = new ComponentDTO();
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		componentDTO = getComponent(wholeName, em);
		if (em.isOpen())
			em.close();

		return componentDTO;
	}

	public static String getWholeName(Long id)
			throws ComponentNotFoundException {
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.id= :id ");
		query.setParameter("id", id);

		try {
			return ((Component) query.getSingleResult()).getWholename();
		} catch (NoResultException e) {
			throw new ComponentNotFoundException();
		}
	}

	public static long getId(EntityManager em, String wholename)
			throws ComponentNotFoundException {
		Query query = em.createQuery("select comp " + "from Component comp "
				+ "where comp.wholename= :wholename ");
		query.setParameter("wholename", wholename);

		try {
			return ((Component) query.getSingleResult()).getId();
		} catch (NoResultException e) {
			throw new ComponentNotFoundException();
		}
	}

	public static Long getId(String wholename)
			throws ComponentNotFoundException {
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		return getId(em, wholename);
	}

	private static boolean isIfFlagTrue(String ifFlagname,
			List<Parameter> parameters, List<ModificationArgument> argsList) {

		boolean ret = true;
		String flag = "";
		for (Parameter pa : parameters) {
			String name = pa.getName();
			if (name.equals(ifFlagname)) {
				flag = pa.getValue();
				if (flag == null)
					flag = pa.getStart();
			}
		}
		// if this variable is set in argslist..this value should override
		// parameter value
		if (argsList != null && ifFlagname != null) {
			ModificationArgument arg = containsParameter(argsList, ifFlagname);

			if (arg != null) {
				if (arg.start != null)
					flag = arg.start;
				if (arg.value != null)
					flag = arg.value;
			}
		}
		if (flag == null) {
			System.err.println(ifFlagname);
		}

		if (flag.equals("true")) {
			ret = true;
		} else if (flag.equals("false")) {
			ret = false;
		}
		return ret;
	}

}