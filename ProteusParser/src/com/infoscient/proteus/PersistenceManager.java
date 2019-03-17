package com.infoscient.proteus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManager {
	 
  public static final boolean DEBUG = true;
  private static final PersistenceManager singleton = new PersistenceManager();
  
  protected EntityManagerFactory emf;
  protected EntityManager em = null;
  
  public static PersistenceManager get() {
    return singleton;
  }
  
  private PersistenceManager() {
  }
 
  public EntityManager getEntityManager() {
  	if (em == null || !em.isOpen()) {
  		em = getEntityManagerFactory().createEntityManager();
  	}
  	return em;
  }
  public EntityManagerFactory getEntityManagerFactory() {
    
    if (emf == null)
      createEntityManagerFactory();
    return emf;
  }
  
  public void closeEntityManagerFactory() {
    
    if (emf != null) {
      emf.close();
      emf = null;
      if (DEBUG)
        System.out.println("n*** Persistence finished at " + new java.util.Date());
    }
  }
  
  protected void createEntityManagerFactory() {
    this.emf = Persistence.createEntityManagerFactory("ProteusGWT");
    if (DEBUG)
      System.out.println("n*** Persistence started at " + new java.util.Date());
  }
}