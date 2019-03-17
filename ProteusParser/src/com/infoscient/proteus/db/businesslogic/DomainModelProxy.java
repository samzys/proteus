package com.infoscient.proteus.db.businesslogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.infoscient.proteus.db.ModelJSONWriter;
import com.infoscient.proteus.db.datatransferobjects.ComponentDTO;
import com.infoscient.proteus.db.datatransferobjects.ConnectDTO;
import com.infoscient.proteus.db.datatransferobjects.ModelincludeDTO;
import com.infoscient.proteus.db.domain.Model;
import com.infoscient.proteus.db.domain.ModelIncludemodification;
import com.infoscient.proteus.db.domain.Modelconnect;
import com.infoscient.proteus.db.domain.Modelinclude;
import com.infoscient.proteus.db.exceptions.ComponentNotFoundException;
import com.infoscient.proteus.db.exceptions.ModelNotFoundException;
import com.infoscient.proteus.PersistenceManager;
public class DomainModelProxy {
	public static List<Long> getIdList() {
		List<Long> ret = new ArrayList<Long>();

		//		EntityManagerFactory emf = Persistence
		//				.createEntityManagerFactory("ProteusGWT");
		EntityManager em = PersistenceManager.get().getEntityManager();//emf.createEntityManager();

		Query query = em.createQuery("select mode " + "from Model mode ");
		List resultList = query.getResultList();
		List<Model> modelList = resultList;
		for (Model model : modelList) {
			ret.add(model.getId());
		}
		em.close();
		return ret;
	}

	public static ComponentDTO getModel(long id) throws ModelNotFoundException,
			ComponentNotFoundException {

		EntityManager em = PersistenceManager.get().getEntityManager();

		ComponentDTO componentDTO = getModel(em, id);

		em.close();
		//		emf.close();

		return componentDTO;
	}

	public static ComponentDTO getModel(EntityManager em, long id)
			throws ModelNotFoundException, ComponentNotFoundException {
		ComponentDTO componentDTO = new ComponentDTO();
		Model model;
		Query query = em.createQuery("select mode " + "from Model mode "
				+ "where mode.id= :id ");
		query.setParameter("id", id);

		try {
			model = (Model) query.getSingleResult();
		} catch (NoResultException e) {
			throw new ModelNotFoundException();
		}
		componentDTO.setName(model.getName());
		componentDTO.setWholeName(model.getWholename());
		componentDTO.setRestriction("model");
		componentDTO.setID(model.getId());
		// connect
		List<Modelconnect> modelConnectList = model.getModelConnects();
		for (Modelconnect modelConnect : modelConnectList) {
			// @sam the connection annotation could be null;
			String value = null;
			if (modelConnect.getValue() != null)
				value = new String(modelConnect.getValue());
			String startPin = modelConnect.getStartpin();
			String endPin = modelConnect.getEndpin();
			ConnectDTO modelConnectDTO = new ConnectDTO(value, startPin, endPin);
			componentDTO.getConnects().add(modelConnectDTO);
		}

		// include
		List<Modelinclude> modelIncludeList = model.getModelIncludes();
		for (Modelinclude modelInclude : modelIncludeList) {
			ModifacationData data = new ModifacationData();

			data.id = modelInclude.getTypeId();
			data.extent = modelInclude.getExtent();
			data.origin = modelInclude.getOrigin();
			data.rotation = modelInclude.getRotation();
			data.declarationName = modelInclude.getName();

			for (ModelIncludemodification modification : modelInclude
					.getModelincludemodifications()) {
				String name = modification.getName();
				String value = modification.getValue();
				data.arguments.put(name, value);
				//System.out.println("from database: " + name + " " + value);
			}
			ComponentDTO includeComponentDTO = DomainComponentProxy
					.getComponent(data, null, em);
			componentDTO.getIncludeComps().add(includeComponentDTO);
		}
		return componentDTO;
	}

	public static long update(long mid, String name,
			List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		long ret = mid;
		EntityManager em = PersistenceManager.get().getEntityManager();
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			Model modelx = em.find(Model.class, mid);
			em.remove(modelx);

			//add a new model with the same mid;
			Model model = new Model(mid);
			model = getModel(em, model, name, connectDTOList, includeDTOList);
			em.merge(model);
			em.getTransaction().commit();
			//write the json file
			writeJsonFile(mid, em);
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
		return ret;
	}

	public static void remove(long mid) {
		EntityManager em = PersistenceManager.get().getEntityManager();
		try {
			em.getTransaction().begin();
			Model modelx = em.find(Model.class, mid);
			em.remove(modelx);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}

	public static Model getModel(EntityManager em, Model model, String name,
			List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {

		model.setName(name);
		model.setWholename("proteus." + name);
		for (ConnectDTO connectDTO : connectDTOList) {
			Modelconnect connect = new Modelconnect();
			connect.setValue(connectDTO.getValue().getBytes());
			connect.setStartpin(connectDTO.getStartpin());
			connect.setEndpin(connectDTO.getEndpin());
			connect.setModel(model);
			model.getModelConnects().add(connect);
		}

		for (int i = 0; i < includeDTOList.size(); i++) {
			ModelincludeDTO includeDTO = includeDTOList.get(i);
			Long includeId = getModelincludeMaxId(em) + 1 + i;
			Modelinclude include = new Modelinclude();
			include.setId(includeId);

			Set<Entry<String, String>> includeModifications = includeDTO
					.getModification().entrySet();
			for (Entry<String, String> modification : includeModifications) {
				ModelIncludemodification includeModification = new ModelIncludemodification();
				includeModification.setName(modification.getKey());
				includeModification.setValue(modification.getValue());
				includeModification.setModelinclude(include);
				include.getModelincludemodifications().add(includeModification);
			}

			include.setName(includeDTO.getVarName());
			include.setOrigin(includeDTO.getOrigin());
			include.setExtent(includeDTO.getExtent());
			include.setRotation(includeDTO.getRotation());
			Long typeId = DomainComponentProxy.getId(includeDTO.getTypeName(),
					em);
			include.setTypeId(typeId);
			include.setModel(model);
			model.getModelIncludes().add(include);
		}
		return model;
	}

	private static void writeJsonFile(long mid, EntityManager em)
			throws ComponentNotFoundException {
		try {
			ModelJSONWriter.writeModel(mid, em);
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static long save(String name, List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {

		EntityManager em = PersistenceManager.get().getEntityManager();
		Long mid = getMaxId(em) + 1;
		long ret = 0;

		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			Model model = new Model(mid);
			model = getModel(em, model, name, connectDTOList, includeDTOList);
			em.merge(model);
			tx.commit();
			//try write the json file after updated. 
			writeJsonFile(mid, em);
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		} finally {
			em.close();
		}
		ret = mid;
		return ret;
	}

	public static Long getId(String wholename, EntityManager em)
			throws ComponentNotFoundException {

		Query query = em.createQuery("select comp " + "from Model comp "
				+ "where comp.wholename= :wholename ");
		query.setParameter("wholename", wholename);

		try {
			return ((Model) query.getSingleResult()).getId();
		} catch (NoResultException e) {
			throw new ComponentNotFoundException();
		}
	}

	private static Long getMaxId(EntityManager em) {
		Query query = em.createQuery("select max(model.id) "
				+ " from Model model ");
		Long maxId = (Long) query.getSingleResult();
		if (maxId == null) {
			maxId = 1L;
		}
		return maxId;
	}

	private static Long getModelincludeMaxId(EntityManager em) {
		Query query = em.createQuery("select max(modelinclude.id) "
				+ " from Modelinclude modelinclude ");
		Long maxId = (Long) query.getSingleResult();
		if (maxId == null) {
			maxId = 1L;
		}
		return maxId;
	}

	/**************************** start with sam's code ***************************************************/

}
