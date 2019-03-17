package proteus.gwt.server.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Includecomponent;
import proteus.gwt.server.domain.Includemodification;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;

public class IncludecomponentProxy {

	public static void save(Long componentId, String wholename, String extent,
			String origin, String rotation, String varName, String ifFlagName,
			String arrayForm, List<ModificationArgument> includeArgumentMap)
			throws ComponentNotFoundException {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		tx.begin();
		Long typeId = DomainComponentProxy.getId(wholename, em);
		if (!contains(componentId, typeId, varName, em)) {

			Includecomponent includeComp = new Includecomponent();
			Long id = getMaxId(em) + 1;

			includeComp.setId(id);
			includeComp.setComponent(new Component(componentId));
			includeComp.setExtent(extent);
			includeComp.setOrigin(origin);
			includeComp.setName(varName);
			includeComp.setRotation(rotation);
			includeComp.setIfFlagName(ifFlagName);
			includeComp.setArrayForm(arrayForm);

			includeComp.setTypeId(typeId);


			for (ModificationArgument argument : includeArgumentMap) {

				Includemodification includeModification = new Includemodification();
				includeModification.setName(argument.getName());
				includeModification.setValue(argument.getValue());
				includeModification.setStart(argument.getStart());
				includeModification.setFixed(argument.getFixed());
				includeModification.setUnit(argument.getUnit());
				includeModification.setDisplayUnit(argument.getDisplayUnit());
				includeModification.setQuantity(argument.getQuantity());
				includeModification.setMin(argument.getMin());
				includeModification.setMax(argument.getMax());
				includeModification.setNominal(argument.getNominal());
				includeModification.setStateSelect(argument.getStateSelect());
				
				includeModification.setIncludecomponent(includeComp);
				includeComp.getIncludemodifications().add(includeModification);
			}
			em.merge(includeComp);
		}
		tx.commit();
		em.close();
		emf.close();
	}

	public static boolean contains(Long componentId, Long typeId, String name,
			EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em.createQuery("select comp "
				+ " from Includecomponent comp "
				+ " where comp.component= :component "
				+ " and comp.typeId=:typeId " + " and comp.name=:name");
		query.setParameter("component", component);
		query.setParameter("typeId", typeId);
		query.setParameter("name", name);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}

	public static Long getMaxId(EntityManager em) {
		Query query = em.createQuery("select max(comp.id) "
				+ " from Includecomponent comp ");
		Long maxId=(Long) query.getSingleResult();
		if(maxId==null){
			maxId=1L;
		}
		return maxId;
	}

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		System.out.println(getMaxId(em));
		em.close();
		emf.close();
	}
}
