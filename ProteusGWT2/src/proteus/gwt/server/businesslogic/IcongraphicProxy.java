package proteus.gwt.server.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Icongraphic;
import proteus.gwt.shared.datatransferobjects.IcongraphicDTO;

public class IcongraphicProxy {

	public static void save(Long componentId, List<IcongraphicDTO> iconList) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			for (IcongraphicDTO icon : iconList) {

				Icongraphic iconGraphic = new Icongraphic();
				iconGraphic.setValue(icon.getGraphics().getBytes());
				iconGraphic.setExtent(icon.getExtent());
				iconGraphic.setPreserveAspectRatio(icon
						.getPreserveAspectRatio());
				iconGraphic.setGrid(icon.getGrid());
				iconGraphic.setComponent(new Component(componentId));
				em.persist(iconGraphic);
			}
			tx.commit();
		}
		em.close();
		emf.close();

	}

	public static void save(Long componentId, List<String> icons,
			EntityManager em) {
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			for (String icon : icons) {

				Icongraphic iconGraphic = new Icongraphic();
				iconGraphic.setValue(icon.getBytes());
				iconGraphic.setComponent(new Component(componentId));
				em.persist(iconGraphic);
			}
			tx.commit();
		}

	}

	public static boolean contains(Long componentId, EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em.createQuery("select icon " + "from Icongraphic icon "
				+ "where icon.component= :component ");
		query.setParameter("component", component);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}
}
