package com.infoscient.proteus.db.businesslogic;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.domain.Restriction;
import com.infoscient.proteus.db.exceptions.RestrictionNotFoundException;

public class RestrictionProxy {
	public static Long getId(String name) throws RestrictionNotFoundException {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		Query query = em.createQuery("select rest " + "from Restriction rest "
				+ "where rest.name= :name ");
		query.setParameter("name", name);

		try {
			return ((Restriction) query.getSingleResult()).getId();
		} catch (NoResultException e) {
			throw new RestrictionNotFoundException();
		}
	}

	public static Long getId(String name, EntityManager em)
			throws RestrictionNotFoundException {

		Query query = em.createQuery("select rest " + "from Restriction rest "
				+ "where rest.name= :name ");
		query.setParameter("name", name);

		try {
			return ((Restriction) query.getSingleResult()).getId();
		} catch (NoResultException e) {
			throw new RestrictionNotFoundException();
		}
	}
}
