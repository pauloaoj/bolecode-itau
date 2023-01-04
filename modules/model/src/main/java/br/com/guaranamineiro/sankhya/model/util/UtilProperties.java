package br.com.guaranamineiro.sankhya.model.util;

import java.io.InputStream;
import java.util.Properties;

public class UtilProperties {

   private static Properties properties;

   public static Properties get() throws Exception {
      if (properties == null) {
         try (InputStream is = UtilProperties.class.getResourceAsStream("/config.properties")) {
            properties = new Properties();
            properties.load(is);
         }
      }
      return properties;
   }

}
