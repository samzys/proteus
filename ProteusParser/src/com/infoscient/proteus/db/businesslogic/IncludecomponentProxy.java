package com.infoscient.proteus.db.businesslogic;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.domain.Component;
import com.infoscient.proteus.db.domain.Extendcomponent;
import com.infoscient.proteus.db.domain.Includecomponent;
import com.infoscient.proteus.db.domain.Includemodification;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
public class IncludecomponentProxy {

	public static void save(Long componentId, String wholename, String extent,
			String origin, String rotation, String varName, String ifFlagName,
			String arrayForm, HashMap<String, String> includeArgumentMap)
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

			Set<Entry<String, String>> includeArgumengts = includeArgumentMap
					.entrySet();
			for (Entry<String, String> argument : includeArgumengts) {

				Includemodification includeModification = new Includemodification();
				includeModification.setName(argument.getKey());
				includeModification.setValue(argument.getValue());
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
