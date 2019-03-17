package proteus.gwt.server.businesslogic;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Extendcomponent;
import proteus.gwt.server.domain.Extendmodification;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;

public class ExtendcomponentProxy {
	public static void save(Long componentId, String wholename, String extent,
			List<ModificationArgument> extendArgumentMap)
			throws ComponentNotFoundException {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		
		Long typeId = DomainComponentProxy.getId(wholename, em);
		if (!contains(componentId, typeId, em)) {
			Extendcomponent extendComp = new Extendcomponent();
			Long id = getMaxId(em) + 1;

			extendComp.setId(id);
			extendComp.setComponent(new Component(componentId));
			extendComp.setExtent(extent);
			extendComp.setTypeId(typeId);


			for(ModificationArgument argument: extendArgumentMap) {
				Extendmodification extendModification = new Extendmodification();
				extendModification.setName(argument.getName());
				extendModification.setValue(argument.getValue());
				extendModification.setStart(argument.getStart());
				extendModification.setFixed(argument.getFixed());
				extendModification.setUnit(argument.getUnit());
				extendModification.setDisplayUnit(argument.getDisplayUnit());
				extendModification.setQuantity(argument.getQuantity());
				extendModification.setMin(argument.getMin());
				extendModification.setMax(argument.getMax());
				extendModification.setNominal(argument.getNominal());
				extendModification.setStateSelect(argument.getStateSelect());
				
				extendModification.setExtendcomponent(extendComp);
				extendComp.getExtendmodifications().add(extendModification);
			}

			em.merge(extendComp);

		}
		tx.commit();
		em.close();
		emf.close();
	}

	public static Long getMaxId(EntityManager em) {
		Query query = em.createQuery("select max(comp.id) "
				+ " from Extendcomponent comp ");
		Long maxId = (Long) query.getSingleResult();
		if (maxId == null) {
			maxId = 1L;
		}
		return maxId;
	}

	public static boolean contains(Long componentId, Long typeId,
			EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em.createQuery("select extendComp "
				+ "from Extendcomponent extendComp "
				+ "where extendComp.component= :component "
				+ "and extendComp.typeId= :typeId");
		query.setParameter("component", component);
		query.setParameter("typeId", typeId);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		//System.out.println(getMaxId(em));
		System.out.println(contains(47L,1L,em));
		em.close();
		emf.close();
	}
}
