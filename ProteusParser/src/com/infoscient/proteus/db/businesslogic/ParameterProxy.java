package com.infoscient.proteus.db.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.domain.Component;
import com.infoscient.proteus.db.domain.Icongraphic;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;

import com.infoscient.proteus.types.StringProperty;

public class ParameterProxy {

	public static void save(Long componentId,
			com.infoscient.proteus.modelica.Component.Parameter[] parameters)
			 {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			for (com.infoscient.proteus.modelica.Component.Parameter parameter : parameters) {

				com.infoscient.proteus.db.domain.Parameter parameterDB = new com.infoscient.proteus.db.domain.Parameter();

				parameterDB.setComponent(new Component(componentId));

				parameterDB.setName(parameter.name);
				parameterDB.setValue(parameter.defaultValue);
				if(parameter.primitiveType!=null) {
					parameterDB.setUnit(parameter.primitiveType.getUnit());
					parameterDB.setQuantity(parameter.primitiveType.getQuantity());
					parameterDB.setStart(parameter.primitiveType.getStart());
				}
				parameterDB.setHeader(parameter.header);
				//System.err.println("ddd"+parameter.defaultValue);;
				em.persist(parameterDB);
			}
			tx.commit();
		}
		em.close();
		emf.close();
	}

	public static void save(Long componentId,
			com.infoscient.proteus.modelica.Component.Parameter[] parameters,
			EntityManager em) throws ComponentNotFoundException {
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			for (com.infoscient.proteus.modelica.Component.Parameter parameter : parameters) {

				com.infoscient.proteus.db.domain.Parameter parameterDB = new com.infoscient.proteus.db.domain.Parameter();

				parameterDB.setComponent(new Component(componentId));
				//parameterDB.setTypeId(DomainComponentProxy.getId(parameter.type)); 
				parameterDB.setName(parameter.name);
				parameterDB.setValue(parameter.defaultValue);
				if(parameter.primitiveType!=null) {
					parameterDB.setUnit(parameter.primitiveType.getUnit());
					parameterDB.setQuantity(parameter.primitiveType.getQuantity());
					parameterDB.setStart(parameter.primitiveType.getStart());
				}
				parameterDB.setHeader(parameter.header);
				em.persist(parameterDB);
			}
			tx.commit();
		}

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
