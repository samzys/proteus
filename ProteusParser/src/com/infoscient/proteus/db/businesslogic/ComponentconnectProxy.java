package com.infoscient.proteus.db.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.domain.Component;
import com.infoscient.proteus.db.domain.Componentconnect;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;

/**
 * @author Gao Lei Created Apr 20, 2011
 */
public class ComponentconnectProxy {
	public static void save(Long componentId, String value,String startPin,String endPin)
			 {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();

		
		if (!contains(componentId,startPin,endPin, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			
			Componentconnect connect = new Componentconnect();
			connect.setComponent(new Component(componentId));
			connect.setValue(value.getBytes());
			connect.setStartpin(startPin);
			connect.setEndpin(endPin);
			
			em.persist(connect);

			tx.commit();
		}
		em.close();
		emf.close();
	}

	public static boolean contains(Long componentId, String startpin, String endpin,EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em.createQuery("select cc " + "from Componentconnect cc "
				+ "where cc.component= :component "
				+ "and cc.startpin=:startpin "
				+ "and cc.endpin=:endpin");
		query.setParameter("component", component);
		query.setParameter("startpin", startpin);
		query.setParameter("endpin", endpin);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}
}
