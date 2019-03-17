package proteus.gwt.server.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Diagramgraphic;
import proteus.gwt.shared.datatransferobjects.DiagramgraphicDTO;

public class DiagramgraphicProxy {

	public static void save(Long componentId, DiagramgraphicDTO diagramDTO) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			 List<String> diagrams = diagramDTO.getGraphicList();
			for (String diagram : diagrams) {
				Diagramgraphic diagramGraphic = new Diagramgraphic();
				diagramGraphic.setValue(diagram.getBytes());
				diagramGraphic.setComponent(new Component(componentId));
				em.persist(diagramGraphic);
			}
			tx.commit();
			
		}
		em.close();
		emf.close();
	}

	public static void save(Long componentId, List<String> diagrams,
			EntityManager em) {
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			for (String diagram : diagrams) {
				Diagramgraphic diagramGraphic = new Diagramgraphic();
				diagramGraphic.setValue(diagram.getBytes());
				diagramGraphic.setComponent(new Component(componentId));
				em.persist(diagramGraphic);
			}
			tx.commit();
		}

	}

	public static boolean contains(Long componentId, EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em.createQuery("select diagram "
				+ "from Diagramgraphic diagram "
				+ "where diagram.component= :component ");
		query.setParameter("component", component);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}
}
