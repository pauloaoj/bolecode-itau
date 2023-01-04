package br.com.guaranamineiro.sankhya.model.util;

import org.apache.commons.lang3.exception.ExceptionUtils;

import br.com.guaranamineiro.sankhya.model.constant.MessageType;

public class UtilException extends Exception {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   public UtilException(MessageType type, String message) {
      super(String.format("%s: %s", type.name(), message));
   }

   public static String get(Exception exception) {
      String error = ExceptionUtils.getStackTrace(exception);
      return error;
   }

}
