package br.com.guaranamineiro.sankhya.model.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class QuerySet {

   public static void writeQueries(Class<?>... clazz) {
      try {
         for (Class<?> classRegister : clazz) {
            register(classRegister);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static void register(Class<?> clazz) throws Exception {
      Field[] fields = clazz.getDeclaredFields();
      Field noRead = clazz.getField("noRead");

      boolean read = noRead.getBoolean(null);

      if (fields.length > 0 && !read) {

         String content = "";

         for (Field field : fields) {

            QueryMap queryMap = field.getAnnotation(QueryMap.class);
            if (queryMap != null) {
               String fileName = String.format("/%s.sql", queryMap.path());
               try (InputStream is = QuerySet.class.getResourceAsStream(fileName)) {
                  if (is != null) {
                     try (BufferedReader reader = new BufferedReader(
                           new InputStreamReader(is, StandardCharsets.UTF_8))) {
                        content = reader.lines().collect(Collectors.joining("\n"));
                        if (content != null && !content.isEmpty()) {
                           field.setAccessible(false);
                           field.set(null, content);
                        }
                     }
                  }
               }
            }
         }

         noRead.setAccessible(false);
         noRead.set(null, true);
      }
   }
}
