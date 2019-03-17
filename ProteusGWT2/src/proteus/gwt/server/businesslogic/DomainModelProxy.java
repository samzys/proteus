package proteus.gwt.server.businesslogic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import proteus.gwt.server.db.ModelJSONWriter;
import proteus.gwt.server.domain.Component;
import proteus.gwt.server.domain.Model;
import proteus.gwt.server.domain.ModelIncludemodification;
import proteus.gwt.server.domain.Modelconnect;
import proteus.gwt.server.domain.Modelextend;
import proteus.gwt.server.domain.Modelinclude;
import proteus.gwt.server.exceptions.ComponentNotFoundException;
import proteus.gwt.server.exceptions.ModelNotFoundException;
import proteus.gwt.shared.datatransferobjects.ComponentDTO;
import proteus.gwt.shared.datatransferobjects.ConnectDTO;
import proteus.gwt.shared.datatransferobjects.ModelDTO;
import proteus.gwt.shared.datatransferobjects.ModelincludeDTO;
import proteus.gwt.shared.datatransferobjects.ModificationArgument;
import proteus.gwt.shared.datatransferobjects.ModificationData;

/**
 * @author Gao Lei Created Apr 12, 2011 remove the field mid to use this file
 */
@Deprecated
public class DomainModelProxy {

	@Deprecated
	public static long update(long mid, String name,
			List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		long ret = mid;
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();

		Model modelx = em.find(Model.class, mid);
		if (modelx != null) {
			List<Modelconnect> connList = modelx.getModelConnects();
			for (Modelconnect modelconn : connList) {
				System.out.println("model conn " + modelconn.getId() + ", "
						+ modelconn.getStartpin() + ", "
						+ modelconn.getEndpin());
				modelconn.setModel(null);
				// connList.remove(modelconn);
				em.remove(modelconn);
			}

			List<Modelinclude> includeList = modelx.getModelIncludes();
			for (Modelinclude modelInclude : includeList) {
				System.out.println("model include " + modelInclude.getName()
						+ ", " + modelInclude.getId().toString());
				modelInclude.setModel(null);
				// includeList.remove(modelInclude);
				em.remove(modelInclude);
			}
			connList.clear();
			includeList.clear();
			em.merge(modelx);
		}
		if (modelx != null) {
			// em.getTransaction().begin();
			// modelx.getModelConnects().clear();
			// modelx.getModelIncludes().clear();

			// for (Modelconnect modelconn : modelx.getModelConnects()) {
			// modelconn.setModel(null);
			// em.remove(modelconn);
			// }
			//			
			// for (Modelinclude modelInclude : modelx.getModelIncludes()) {
			// modelInclude.setModel(null);
			// em.remove(modelInclude);
			// }

			// modelx.setName(name);
			// modelx.setWholename("proteus." + name);
			//
			// Long connId = getModelconnMaxId(em) + 1;
			// for (ConnectDTO connectDTO : connectDTOList) {
			// connId++;
			// Modelconnect connect = new Modelconnect();
			// connect.setId(connId);
			// connect.setValue(connectDTO.getValue().getBytes());
			// connect.setStartpin(connectDTO.getStartpin());
			// connect.setEndpin(connectDTO.getEndpin());
			// connect.setModel(modelx);
			// modelx.getModelConnects().add(connect);
			// }
			//
			// Long includeId = getModelincludeMaxId(em) + 1;
			// for (int i = 0; i < includeDTOList.size(); i++) {
			// includeId += i;
			// ModelincludeDTO includeDTO = includeDTOList.get(i);
			// Modelinclude include = new Modelinclude();
			// include.setId(includeId);
			//
			// Set<Entry<String, String>> includeModifications = includeDTO
			// .getModification().entrySet();
			// for (Entry<String, String> modification : includeModifications) {
			// ModelIncludemodification includeModification = new
			// ModelIncludemodification();
			// includeModification.setName(modification.getKey());
			// includeModification.setValue(modification.getValue());
			// includeModification.setModelinclude(include);
			// include.getModelincludemodifications().add(
			// includeModification);
			// }
			//
			// include.setName(includeDTO.getVarName());
			// include.setOrigin(includeDTO.getOrigin());
			// include.setExtent(includeDTO.getExtent());
			// include.setRotation(includeDTO.getRotation());
			// Long typeId = DomainComponentProxy.getId(includeDTO
			// .getTypeName(), em);
			// include.setTypeId(typeId);
			// include.setModel(modelx);
			// modelx.getModelIncludes().add(include);
			// }
			// em.merge(modelx);
			// em.getTransaction().commit();
		} else {
			save(em, mid, name, connectDTOList, includeDTOList);
		}
		if (em.isOpen())
			em.close();
		writeJsonFile(mid);
		return ret;
	}

	/**
	 * @this is old orgianl update method
	 * @param mid
	 * @param name
	 * @param connectDTOList
	 * @param includeDTOList
	 * @return
	 * @throws ComponentNotFoundException
	 */
	@Deprecated
	public static long update3(long mid, String name,
			List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		long ret = mid;
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		Model modelx = em.find(Model.class, mid);
		if (modelx != null) {
			em.getTransaction().begin();
			em.remove(modelx);
			em.getTransaction().commit();
		}
		em.close();

		em = PersistenceManagerGWT.get().getEntityManager();
		save(em, mid, name, connectDTOList, includeDTOList);

		em.close();
		writeJsonFile(mid);
		return ret;
	}

	@Deprecated
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

	/**
	 * @param wholename
	 * @param em
	 * @return
	 * @throws ModelNotFoundException
	 * @throws ComponentNotFoundException
	 * @deprecated use getModel return ComponentDTO instead
	 */
	public static ModelDTO getModel(String wholename, EntityManager em)
			throws ModelNotFoundException, ComponentNotFoundException {
		ModelDTO modelDTO = new ModelDTO();
		Model model;
		Query query = em.createQuery("select mode " + "from Model mode "
				+ "where mode.wholename= :wholename ");

		query.setParameter("wholename", wholename);

		try {
			model = (Model) query.getSingleResult();
		} catch (NoResultException e) {
			throw new ModelNotFoundException();
		}

		modelDTO.setName(model.getName());
		modelDTO.setWholeName(model.getWholename());

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
			modelDTO.getModelConnects().add(modelConnectDTO);
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
				data.argsList.add(new ModificationArgument(name, value, start));
				System.out.println("from database: " + name + " " + value);
			}

			ComponentDTO componentDTO = DomainComponentProxy.getComponent(data,
					null, em);
			modelDTO.getModelIncludes().add(componentDTO);
		}

		// extend
		List<Modelextend> modelExtendList = model.getModelExtends();
		for (Modelextend modelExtend : modelExtendList) {

			ModificationData data = new ModificationData();

			data.id = modelExtend.getTypeId();
			data.extent = modelExtend.getExtent();
			data.origin = modelExtend.getOrigin();
			data.rotation = modelExtend.getRotation();

			ComponentDTO componentDTO = DomainComponentProxy.getComponent(data,
					null, em);
			modelDTO.getModelExtends().add(componentDTO);
		}

		return modelDTO;
	}

	/**
	 * @param wholename
	 * @return
	 * @throws ModelNotFoundException
	 * @throws ComponentNotFoundException
	 * @deprecated
	 */
	public static ModelDTO getModel(String wholename)
			throws ModelNotFoundException, ComponentNotFoundException {
		ModelDTO modelDTO;

		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		modelDTO = getModel(wholename, em);
		em.close();

		return modelDTO;
	}

	@Deprecated
	public static ComponentDTO getModel(long id) throws ModelNotFoundException,
			ComponentNotFoundException {

		EntityManager em = PersistenceManagerGWT.get().getEntityManager();

		ComponentDTO componentDTO = getModel(em, id);

		em.close();

		return componentDTO;
	}

	@Deprecated
	public static long getId(String wholename)
			throws ComponentNotFoundException {
		EntityManager em = PersistenceManagerGWT.get().getEntityManager();

		Query query = em.createQuery("select mode " + "from Model mode "
				+ "where mode.wholename= :wholename ");
		query.setParameter("wholename", wholename);

		try {
			return ((Component) query.getSingleResult()).getId();
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
@Deprecated
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

@Deprecated
	public static long save(String name, List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {

		EntityManager em = PersistenceManagerGWT.get().getEntityManager();
		Long mid = getMaxId(em) + 1;
		long ret = 0;
		save(em, mid, name, connectDTOList, includeDTOList);
		if (em.isOpen())
			em.close();
		writeJsonFile(mid);
		ret = mid;
		return ret;
	}

	private static void save(EntityManager em, Long mid, String name,
			List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();
			Model model = getModel(em, mid, name, connectDTOList,
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
@Deprecated
	public static Model getModel(EntityManager em, Long modelID, String name,
			List<ConnectDTO> connectDTOList,
			List<ModelincludeDTO> includeDTOList)
			throws ComponentNotFoundException {
		Model model = new Model(modelID);
		model.setName(name);
		model.setWholename("proteus." + name);
		model.setId(model.getId());
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
		for (int i = 0; i < includeDTOList.size(); i++) {
			includeId += i;
			ModelincludeDTO includeDTO = includeDTOList.get(i);
			Modelinclude include = new Modelinclude();
			include.setId(includeId);

			 List<ModificationArgument> includeModifications = includeDTO
					.getModification();
			for (ModificationArgument modification : includeModifications) {
				ModelIncludemodification includeModification = new ModelIncludemodification();
				includeModification.setName(modification.getName());
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

	private static void writeJsonFile(long mid)
			throws ComponentNotFoundException {
		try {
			EntityManager em = PersistenceManagerGWT.get().getEntityManager();
			writeJsonFile(mid, em);
			em.close();
		} catch (ComponentNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void writeJsonFile(long mid, EntityManager em)
			throws ComponentNotFoundException {
		try {
			ModelJSONWriter.writeModel(mid, em);
		} catch (ModelNotFoundException e) {
			e.printStackTrace();
		}
	}
@Deprecated
	public static List<Long> getIdList() {
		List<Long> ret = new ArrayList<Long>();

		EntityManager em = PersistenceManagerGWT.get().getEntityManager();// emf.createEntityManager();

		Query query = em.createQuery("select mode " + "from Model mode ");
		List resultList = query.getResultList();
		List<Model> modelList = resultList;
		for (Model model : modelList) {
			ret.add(model.getId());
		}
		em.close();
		return ret;
	}
@Deprecated
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
				data.argsList.add(new ModificationArgument(name, value, start));
				System.out.println("from database: " + name + " " + value);
			}
			ComponentDTO includeComponentDTO = DomainComponentProxy
					.getComponent(data, null, em);
			componentDTO.getIncludeComps().add(includeComponentDTO);
		}

		return componentDTO;
	}

}
