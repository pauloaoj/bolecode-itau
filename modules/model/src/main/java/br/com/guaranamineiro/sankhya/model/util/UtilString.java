package br.com.guaranamineiro.sankhya.model.util;

import java.text.Normalizer;

public class UtilString {
   
   public static String removerAcentos(String str) {
      
      if (str == null) {
         return null;
      }
      
      str = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
      str = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[\\p{Punct}]", "");
      return str;
  }

}
