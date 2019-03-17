package proteus.gwt.server.businesslogic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import proteus.gwt.server.db.ModelJSONWriter;
import proteus.gwt.server.domain.Model;
import proteus.gwt.server.domain.ModelIncludemodification;
import proteus.gwt.server.domain.Modelconnect;
import proteus.gwt.server.domain.Modelinclude;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.exceptions.ModelNotFoundException;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ConnectDTO;
import proteus.gwt.shared.datatransferobjects.ModelincludeDTO;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;
import proteus.gwt.shared.datatransferobjects.ModificationData;
import proteus.gwt.shared.util.Utils;

import com.infoscient.proteus.PersistenceManager;

/**
 * @author sam
 * 
 */
public class DomainModelProxy2 {

	// This is to keep 5 edit history for every model in the database
	private static final int HISTORY_LIMIT = 5;


	public static List<Long> getIdList() {
		List<Long> ret = new ArrayList<Long>();

		// EntityManagerFactory emf = Persistence
		// .createEntityManagerFactory("ProteusGWT");
		EntityManager em = PersistenceManager.get().getEntityManager();// emf.createEntityManager();

		Query query = em.createQuery("SELECT DISTINCT mode.mid FROM Model mode ");
		List resultList = query.getResultList();
		List<Long> idList = resultList;
		for (Long model : idList) {
			ret.add(model);
		}
		em.close();
		return ret;
	}

	public static long update(long mid, String name,
			List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		Long indexId = getMaxId(em) + 1;
		long ret = 0;
		save(em, indexId, mid, name, connectDTOList, includeDTOList);
		if (em.isOpen())
			em.close();
		PersistenceManagerGWT.get().closeEntityManagerFactory();

		writeJsonFile(mid);
		ret = mid;
		try {
			removeOldest(mid);
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static long save(String name, List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {

		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		Long mid = getMaxMId(em) + 1;
		Long indexId = getMaxId(em) + 1;
		long ret = 0;
		save(em, indexId, mid, name, connectDTOList, includeDTOList);
		if (em.isOpen())
			em.close();
		writeJsonFile(mid);
		ret = mid;

		return ret;
	}

	private static void save(EntityManager em, Long indexId, Long mid,
			String name, List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			Model model = getModel(em, indexId, mid, name, connectDTOList,
					includeDTOList);
			em.merge(model);
			tx.commit();
			// try write the json file after updated.
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		}
	}

	public static Model getModel(EntityManager em, Long indexId, Long modelID,
			String name, List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		Model model = new Model(indexId);
		model.setName(name);
		model.setWholename("proteus." + name);
		model.setMid(modelID);
		Long connId = getModelconnMaxId(em) + 1;

		for (ConnectDTO connectDTO : connectDTOList) {
			connId++;
			Modelconnect connect = new Modelconnect();
			connect.setId(connId);
			connect.setValue(connectDTO.getValue().getBytes());
			connect.setStartpin(connectDTO.getStartpin());
			connect.setEndpin(connectDTO.getEndpin());
			connect.setModel(model);
			model.getModelConnects().add(connect);
		}

		Long includeId = getModelincludeMaxId(em) + 1;
		Long modificationId = getIncludeModificationMaxId(em) + 1;
		for (int i = 0; i < includeDTOList.size(); i++) {
			ModelincludeDTO includeDTO = includeDTOList.get(i);
			Modelinclude include = new Modelinclude();
			include.setId(includeId++);

			List<ModificationArgument> includeModifications = includeDTO
					.getModification();
			for (ModificationArgument argument : includeModifications) {
				ModelIncludemodification includeModification = new ModelIncludemodification();

				includeModification.setId(modificationId++);

				includeModification.setName(argument.getName());
				includeModification.setValue(argument.getValue());
				includeModification.setStart(argument.getStart());
				includeModification.setFixed(argument.getFixed());
				includeModification.setUnit(argument.getUnit());
				includeModification.setDisplayUnit(argument.getDisplayUnit());
				includeModification.setQuantity(argument.getQuantity());
				includeModification.setMin(argument.getMin());
				includeModification.setMax(argument.getMax());
				includeModification.setNominal(argument.getNominal());
				includeModification.setStateSelect(argument.getStateSelect());

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
			
			include.setProtected_(includeDTO.getProtected_());
			include.setFinal_(includeDTO.getFinal_());
			include.setTyping(includeDTO.getTyping());
			include.setCausality(includeDTO.getCausality());
			include.setVariability(includeDTO.getVariability());
			
			include.setArrayForm(includeDTO.getArrayForm());
			include.setIfFlagName(includeDTO.getIfFlagName());
			
			model.getModelIncludes().add(include);
		}
		return model;
	}

	private static Long getIncludeModificationMaxId(EntityManager em) {
		Query query = em.createQuery("select max(modification.id) "
				+ " from ModelIncludemodification modification ");
		Long maxId = (Long) query.getSingleResult();

		if (maxId == null) {
			maxId = 1L;
		}
		return maxId;
	}

	public static void main(String[] args) throws ComponentNotFoundException {
		List<Long> idList = getIdList();
		for(Long id: idList) {
			writeJsonFile(id);
		}
	}
	private static void writeJsonFile(long mid)
			throws ComponentNotFoundException {
		try {
			EntityManager em = PersistenceManagerGWT.get().getEntityManager();
			writeJsonFile(mid, em);
			em.close();
			PersistenceManagerGWT.get().closeEntityManagerFactory();
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
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

	private static Long getMaxMId(EntityManager em) {
		Query query = em.createQuery("select max(model.mid) "
				+ " from Model model ");
		Long maxId = (Long) query.getSingleResult();
		if (maxId == null) {
			maxId = 1L;
		}
		return maxId;
	}

	private static Long getModelconnMaxId(EntityManager em) {
		Query query = em.createQuery("select max(modelconnect.id) "
				+ " from Modelconnect modelconnect");
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

	public static void remove(long mid) {
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		try {
			Model modelx = em.find(Model.class, mid);
			if (modelx != null) {
				em.getTransaction().begin();
				em.remove(modelx);
				em.getTransaction().commit();
			}
		} finally {
			em.close();
		}
	}

	public static void delete(long mid) {
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		Query query = em.createQuery("SELECT mode FROM Model mode "
				+ "WHERE mode.mid= :mid ORDER BY mode.id DESC");
		query.setParameter("mid", mid);

		try {
			List<Model> models = (List<Model>) query.getResultList();
			for (Model m : models) {
				em.getTransaction().begin();
				em.remove(m);
				em.getTransaction().commit();
			}
		} catch (NoResultException e) {
		} catch (RuntimeException e) {
			EntityTransaction tx = em.getTransaction();
			if (tx != null && tx.isActive())
				tx.rollback();
			throw e;
		}
		em.close();
		removeJsonFile(mid);
	}

	public static void removeOldest(long id) throws ModelNotFoundException {
		List<Model> models;
		Model model;
		// check # of record.
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		Query query = em.createQuery("SELECT count(mode) FROM Model mode "
				+ "WHERE mode.mid= :mid ORDER BY mode.id DESC");
		query.setParameter("mid", id);
		Number result = null;
		try {
			result = (Number) query.getSingleResult();
		} catch (NoResultException e) {
			throw new ModelNotFoundException();
		}
		// perform remove
		if (result != null && result.intValue() > HISTORY_LIMIT) {
			query = em.createQuery("SELECT mode FROM Model mode "
					+ "WHERE mode.mid= :mid ORDER BY mode.id DESC");
			query.setParameter("mid", id);
			query.setFirstResult(HISTORY_LIMIT);

			try {
				models = (List<Model>) query.getResultList();
				for (Model m : models) {
					if (Utils.DEBUG)
						System.out.println(m.getId());
					em.getTransaction().begin();
					em.remove(m);
					em.getTransaction().commit();
				}
			} catch (NoResultException e) {
				throw new ModelNotFoundException();
			} catch (RuntimeException e) {
				EntityTransaction tx = em.getTransaction();
				if (tx != null && tx.isActive())
					tx.rollback();
				throw e;
			}
		}
		em.close();
		PersistenceManagerGWT.get().closeEntityManagerFactory();
	}

	public static ComponentDTO getComponentModel(String wholename)
			throws ModelNotFoundException, ComponentNotFoundException {
		Model model;
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();

		Query query = em.createQuery("select mode " + "from Model mode "
				+ "where mode.wholename= :wholename ");

		query.setParameter("wholename", wholename);

		try {
			model = (Model) query.getSingleResult();
		} catch (NoResultException e) {
			throw new ModelNotFoundException();
		}

		Long modelId = model.getId();

		return getComponentModel(em, modelId);
	}

	public static ComponentDTO getComponentModel(long id)
			throws ModelNotFoundException, ComponentNotFoundException {

		EntityManager em = PersistenceManagerGWT.get().getEntityManager();

		ComponentDTO componentDTO = getComponentModel(em, id);

		em.close();

		return componentDTO;
	}

	public static ComponentDTO getComponentModel(EntityManager em, long id)
			throws ModelNotFoundException, ComponentNotFoundException {
		ComponentDTO componentDTO = new ComponentDTO();
		List<Model> models;
		Model model;
		Query query = em.createQuery("SELECT mode FROM Model mode "
				+ "WHERE mode.mid= :mid ORDER BY mode.id DESC");
		query.setParameter("mid", id);

		try {
			models = (List<Model>) query.getResultList();
			model = models.get(0);
		} catch (NoResultException e) {
			throw new ModelNotFoundException();
		}
		componentDTO.setID(id);
		componentDTO.setName(model.getName());
		componentDTO.setWholeName(model.getWholename());
		componentDTO.setRestriction("model");
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
			ModificationData data = new ModificationData();

			data.id = modelInclude.getTypeId();
			data.extent = modelInclude.getExtent();
			data.origin = modelInclude.getOrigin();
			data.rotation = modelInclude.getRotation();
			data.declarationName = modelInclude.getName();

			for (ModelIncludemodification modification : modelInclude
					.getModelincludemodifications()) {
				String name = modification.getName();
				String value = modification.getValue();
				String start = modification.getStart();
				String fixed = modification.getFixed();
				String min = modification.getMin();
				String max = modification.getMax();
				String unit = modification.getUnit();
				String displayUnit = modification.getDisplayUnit();
				String quantity = modification.getQuantity();
				String nominal = modification.getNominal();
				String stateSelect = modification.getStateSelect();

				data.argsList.add(new ModificationArgument(name, value, start,
						fixed, min, max, unit, displayUnit, quantity, nominal,
						stateSelect));
				// System.out.println("from database: " + name + " " + value);
			}

			ComponentDTO includeComponentDTO = DomainComponentProxy
					.getComponent(data, null, em);
			includeComponentDTO.setSuperIncludeArgList(data.argsList);
			
			includeComponentDTO.setProtected_(modelInclude.getProtected_());
			includeComponentDTO.setFinal_(modelInclude.getFinal_());
			includeComponentDTO.setTyping(modelInclude.getTyping());
			includeComponentDTO.setVariability(modelInclude.getVariability());
			includeComponentDTO.setCausality(modelInclude.getCausality());
			
			String arrayFromStr = modelInclude.getArrayForm();
//			//TODO only solve array form right now. no matrix support
			if (arrayFromStr != null
					&& !arrayFromStr.equals("")) {
				String list[] = arrayFromStr.split(",");
				for (String str : list) {
//					ModificationArgument arg = containsParameter(parentParameter,
//							name);
					includeComponentDTO.getArrayFormList().add(str);
				}
			}

			componentDTO.getIncludeComps().add(includeComponentDTO);
		}
		return componentDTO;
	}

	
	private static void writeJsonFile(long mid, EntityManager em)
			throws ComponentNotFoundException {
		try {
			ModelJSONWriter.writeModel(mid, em);
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void removeJsonFile(long mid) {
		try {
			ModelJSONWriter.removeModel(mid);
		} catch (Exception e) {
		}
	}
}
