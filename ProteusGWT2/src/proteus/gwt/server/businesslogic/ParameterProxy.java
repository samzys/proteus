package proteus.gwt.server.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.modelica.RefResolver;

import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Parameter;
import proteus.gwt.server.exceptions.ComponentNotFoundException;

public class ParameterProxy {

	public static void save(Long componentId,
			com.infoscient.proteus.modelica.Component.Parameter[] parameters) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		if (!contains(componentId, em)) {
			Long maxId = getMaxId(em);
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			if (parameters != null)
				for (com.infoscient.proteus.modelica.Component.Parameter parameter : parameters) {
					Long typeId = null;
					String compName = parameter.getWholename();
					if (!RefResolver.primitiveSet.contains(compName)) {
						try {
							typeId = DomainComponentProxy.getId(
									parameter.getWholename(), em);
						} catch (ComponentNotFoundException e) {
							e.printStackTrace();
						}
					}
					Parameter parameterDB = new Parameter();
					parameterDB.setId(++maxId);
					parameterDB.setComponent(new Component(componentId));

					parameterDB.setName(parameter.name);
					parameterDB.setValue(parameter.defaultValue);

					if (typeId != null) {
						parameterDB.setType_id(typeId);
					}
					if (parameter.primitiveType != null) {
						parameterDB.setUnit(parameter.primitiveType.getUnit());
						parameterDB.setQuantity(parameter.primitiveType
								.getQuantity());
						parameterDB
								.setStart(parameter.primitiveType.getStart());
						parameterDB.setDisplayUnit(parameter.primitiveType
								.getDisplayUnit());
						parameterDB
								.setFixed(parameter.primitiveType.getFixed());
						parameterDB.setMax(parameter.primitiveType.getMax());
						parameterDB.setMin(parameter.primitiveType.getMin());
						parameterDB.setNominal(parameter.primitiveType
								.getNominal());
						parameterDB.setStateSelect(parameter.primitiveType
								.getStateSelect());
						parameterDB.setType(parameter.primitiveType.getType());
					}
					parameterDB.setAnnotationGroup(parameter.getGroup());
					parameterDB.setAnnotationTab(parameter.getTab());
					parameterDB.setAnnotationInitDialog(parameter
							.getInitDialog());
					parameterDB.setHeader(parameter.header);
					parameterDB.setParaDesc(parameter.description);
					parameterDB.setCausality(parameter.getCausality());
					parameterDB.setVariability(parameter.getVariability());
					parameterDB.setTyping(parameter.getTyping());
					
					em.persist(parameterDB);
				}
			tx.commit();
		}
		em.close();
		emf.close();
	}

	@Deprecated
	public static void save(Long componentId,
			com.infoscient.proteus.modelica.Component.Parameter[] parameters,
			EntityManager em) throws ComponentNotFoundException {
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			Long maxId = getMaxId(em);
			tx.begin();
			for (com.infoscient.proteus.modelica.Component.Parameter parameter : parameters) {

				Parameter parameterDB = new Parameter();
				parameterDB.setId(++maxId);
				parameterDB.setComponent(new Component(componentId));
				// parameterDB.setTypeId(DomainComponentProxy.getId(parameter.type));
				parameterDB.setName(parameter.name);
				parameterDB.setValue(parameter.defaultValue);
				if (parameter.primitiveType != null) {
					parameterDB.setUnit(parameter.primitiveType.getUnit());
					parameterDB.setQuantity(parameter.primitiveType
							.getQuantity());
					parameterDB.setStart(parameter.primitiveType.getStart());
				}
				parameterDB.setHeader(parameter.header);
				em.persist(parameterDB);
			}
			tx.commit();
		}
	}

	private static Long getMaxId(EntityManager em) {
		Query query = em.createQuery("select max(p.id) " + " from Parameter p");
		Long maxId = (Long) query.getSingleResult();
		if (maxId == null) {
			maxId = 1L;
		}
		return maxId;
	}

	public static boolean contains(Long componentId, EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em.createQuery("select para " + "from Parameter para "
				+ "where para.component= :component ");
		query.setParameter("component", component);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}
}
