package br.com.guaranamineiro.sankhya.model.query;

import br.com.guaranamineiro.sankhya.model.util.QueryMap;

public class FinanceiroQueryMap {

   public static boolean noRead = false;

   @QueryMap(path = "query/financeiro/boleto-para-registro-query")
   public static String BOLETO_PARA_REGISTRO_QUERY;
   
   @QueryMap(path = "query/financeiro/boleto-para-registro-filtro-query")
   public static String BOLETO_PARA_REGISTRO_FILTRO_QUERY;

   @QueryMap(path = "query/financeiro/boleto-nossonumero-query")
   public static String BOLETO_NOSSONUMERO_QUERY;
   
   @QueryMap(path = "query/financeiro/boleto-nossonumero-atualizar-query")
   public static String BOLETO_NOSSONUMERO_ATUALIZAR_QUERY;
   
   @QueryMap(path = "query/financeiro/boleto-registro-atualizar-query")
   public static String BOLETO_REGISTRO_ATUALIZAR_QUERY;
   
   @QueryMap(path = "query/financeiro/boleto-tentativa-atualizar-query")
   public static String BOLETO_TENTATIVA_ATUALIZAR_QUERY;
   
}
