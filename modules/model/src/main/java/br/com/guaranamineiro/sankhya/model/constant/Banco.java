package br.com.guaranamineiro.sankhya.model.constant;

import java.util.Properties;

import br.com.guaranamineiro.sankhya.model.BoletoService;
import br.com.guaranamineiro.sankhya.model.service.BoletoITAUService;
import br.com.guaranamineiro.sankhya.model.util.UtilProperties;
import br.com.sankhya.jape.dao.JdbcWrapper;

public enum Banco {

   ITAU, BB, CEF;

   public static Banco get(String data) {
      return Banco.ITAU;
   }
   
   public static Long getConta(Banco banco) throws Exception {
      Properties properties = UtilProperties.get();
      
      Long idConta = null;
      switch (banco) {
      case ITAU:
         idConta = Long.valueOf(properties.getProperty("ITAU_DADOS_ID_CONTA"));
         break;
      default:
         break;
      }
      
      return idConta;
   }
   
   public static BoletoService getBoletoService(Banco banco, JdbcWrapper jdbc) throws Exception {
      BoletoService boletoService = null;
      
      switch (banco) {
      case ITAU:
         boletoService = new BoletoITAUService(jdbc);
         break;
      default:
         break;
      }
      
      return boletoService;
   }
}
