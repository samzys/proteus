package proteus.gwt.server.businesslogic;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Enumeration;

import com.infoscient.proteus.modelica.types.MEnumeration;

public class EnumerationProxy {

	public static void save(Long componentId, MEnumeration me) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		Map<String, String> m = me.getEnumContent();
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			Long maxId = getMaxId(em);
			tx.begin();
			for (String key : m.keySet()) {
				Enumeration enDB = new Enumeration();
				enDB.setId(++maxId);
				enDB.setComponent(new Component(componentId));
				enDB.setName(key);
				enDB.setComment(m.get(key));
				em.persist(enDB);
			}
			tx.commit();
		}
	}

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		Long componentId = 233L;
		String key = "new", value ="balance";
		Long maxId = getMaxId(em);
		
		tx.begin();
			Enumeration enDB = new Enumeration();
			enDB.setId(++maxId);
			enDB.setComponent(new Component(componentId));
			enDB.setName(key);
			enDB.setComment(value);
			em.persist(enDB);
		tx.commit();

	}

	private static Long getMaxId(EntityManager em) {
		Query query = em.createQuery("select max(e.id) from Enumeration e");
		Long maxId = (Long) query.getSingleResult();
		if (maxId == null) {
			maxId = 1L;
		}
		return maxId;
	}

	public static boolean contains(Long componentId, EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em
				.createQuery("select enu from Enumeration enu where enu.component= :component");
		query.setParameter("component", component);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}

}
