package br.com.guaranamineiro.sankhya.model.query;

import br.com.guaranamineiro.sankhya.model.util.QueryMap;

public class GeralQueryMap {

   public static boolean noRead = false;

   @QueryMap(path = "query/parceiro-query")
   public static String PARCEIRO_QUERY;
   
   @QueryMap(path = "query/log-adicionar-query")
   public static String LOG_ADICIONAR_QUERY;

}
