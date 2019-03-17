package proteus.gwt.server.security;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * To create Singleton for specific class
 * Factory Pattern
 * @author Thomas
 *
 */
public class SingletonRegistry {
   public static SingletonRegistry REGISTRY = new SingletonRegistry();
   private static HashMap map = new HashMap();
   private static Logger logger = Logger.getAnonymousLogger();
   protected SingletonRegistry() {
      // Exists to defeat instantiation
   }
   public synchronized Object getInstanceOf(String classname) {
      Object singleton = map.get(classname);
      if(singleton != null) {
         return singleton;
      }
      try {
         singleton = Class.forName(classname).newInstance();
         logger.info("created singleton: " + singleton);
      }
      catch(ClassNotFoundException cnf) {
         logger.warning("Couldn't find class " + classname);    
      }
      catch(InstantiationException ie) {
         logger.warning("Couldn't instantiate an object of type " + 
                       classname);    
      }
      catch(IllegalAccessException ia) {
         logger.warning("Couldn't access class " + classname);    
      }
      map.put(classname, singleton);
      return singleton;
   }
}
