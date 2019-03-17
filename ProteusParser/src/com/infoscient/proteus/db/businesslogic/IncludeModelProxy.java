package com.infoscient.proteus.db.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.domain.Model;
import com.infoscient.proteus.db.domain.Modelinclude;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;

public class IncludeModelProxy {

	public static void save(Long componentId, String wholename, String extent,
			String origin, String rotation, String varName)
			throws ComponentNotFoundException {

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		Long typeId = DomainComponentProxy.getId(wholename, em);
		if (!contains(componentId, typeId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			Modelinclude includeComp = new Modelinclude();
			includeComp.setModel(new Model(componentId));
			includeComp.setExtent(extent);
			includeComp.setOrigin(origin);
			includeComp.setName(varName);
			includeComp.setRotation(rotation);

			includeComp.setTypeId(typeId);
			em.persist(includeComp);

			tx.commit();
		}
		em.close();
		emf.close();

	}

	public static boolean contains(Long componentId, Long typeId,
			EntityManager em) {
		if(true) return true;
		boolean ret = false;
		Query query1  = em.createQuery("select comp from modelinclude comp");
		Query query = em.createQuery("select comp "
				+ "from modelinclude comp "
				+ "where comp.modelId= :component "
				+ " and comp.typeId=:typeId");
		query.setParameter("component", componentId);
		query.setParameter("typeId", typeId);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}
}
