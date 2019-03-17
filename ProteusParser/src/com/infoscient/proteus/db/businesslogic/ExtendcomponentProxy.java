package com.infoscient.proteus.db.businesslogic;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.domain.Component;
import com.infoscient.proteus.db.domain.Extendcomponent;
import com.infoscient.proteus.db.domain.Extendmodification;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
public class ExtendcomponentProxy {
	public static void save(Long componentId, String wholename, String extent,
			HashMap<String, String> extendArgumentMap)
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

			Set<Entry<String, String>> extendArgumengts = extendArgumentMap
					.entrySet();
			for (Entry<String, String> argument : extendArgumengts) {

				Extendmodification extendModification = new Extendmodification();
				extendModification.setName(argument.getKey());
				extendModification.setValue(argument.getValue());
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
