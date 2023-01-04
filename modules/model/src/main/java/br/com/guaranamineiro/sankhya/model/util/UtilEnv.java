package br.com.guaranamineiro.sankhya.model.util;

import java.util.Properties;

public class UtilEnv {

   public static boolean isStaging() throws Exception {      
      Properties properties = UtilProperties.get();
      
      String env = properties.getProperty("ENV");
      return env.equals("staging");
   }
   
   public static boolean isPermision() throws Exception {
      Properties properties = UtilProperties.get();

      String env = properties.getProperty("ENV");
      String path = System.getProperty("jboss.home.dir");

      boolean permision = false;
      if (env.equals("staging")
            && (path.equals("/home/mgeweb/wildfly_teste") || path.equals("/home/mgeweb/wildfly_treinamento"))) {
         permision = true;
      } else if (env.equals("production") && path.equals("/home/mgeweb/wildfly_producao")) {
         permision = true;
      }

      return permision;
   }

   public static String description() throws Exception {
      Properties properties = UtilProperties.get();

      String description = properties.getProperty("ENV_DESCRIPTION");
      return description;
   }

   public static String descriptionApp() throws Exception {
      String path = System.getProperty("jboss.home.dir");

      String description = "";
      if (path.equals("/home/mgeweb/wildfly_teste") || path.equals("/home/mgeweb/wildfly_treinamento")) {
         description = "ambiente sankhya em teste";
      } else if (path.equals("/home/mgeweb/wildfly_producao")) {
         description = "ambiente sankhya em produção";
      }

      return description;
   }

}
