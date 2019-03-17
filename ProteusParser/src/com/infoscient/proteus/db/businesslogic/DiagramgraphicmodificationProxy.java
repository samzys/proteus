package com.infoscient.proteus.db.businesslogic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.infoscient.proteus.db.datatransferobjects.DiagramgraphicDTO;
import com.infoscient.proteus.db.domain.Component;
import com.infoscient.proteus.db.domain.Diagramgraphicsmodification;
public class DiagramgraphicmodificationProxy {

	public static void save(Long componentId, DiagramgraphicDTO diagramDTO) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = emf.createEntityManager();
		if (!contains(componentId, em)) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			String extent = diagramDTO.getExtent();
			String preserveAspectRatio = diagramDTO.getPreserveAspectRatio();
			String grid = diagramDTO.getGrid();
			if (extent != null || preserveAspectRatio != null || grid != null) {
				Diagramgraphicsmodification diagramModification = new Diagramgraphicsmodification();
				diagramModification.setExtent(extent);
				diagramModification.setPreserveAspectRatio(preserveAspectRatio);
				diagramModification.setGrid(grid);
				diagramModification.setComponent(new Component(componentId));

				em.persist(diagramModification);
			}
			tx.commit();

		}
		em.close();
		emf.close();
	}

	public static boolean contains(Long componentId, EntityManager em) {
		boolean ret = false;
		Component component = new Component(componentId);
		Query query = em.createQuery("select diagrammodification "
				+ "from Diagramgraphicsmodification diagrammodification "
				+ "where diagrammodification.component= :component ");
		query.setParameter("component", component);
		List list = query.getResultList();
		if (list != null && list.size() > 0) {
			ret = true;
		}
		return ret;
	}
}
