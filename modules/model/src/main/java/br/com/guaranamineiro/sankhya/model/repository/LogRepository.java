package br.com.guaranamineiro.sankhya.model.repository;

import java.sql.ResultSet;

import br.com.guaranamineiro.sankhya.model.entity.Log;
import br.com.guaranamineiro.sankhya.model.query.GeralQueryMap;
import br.com.guaranamineiro.sankhya.model.util.UtilDate;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;

public class LogRepository {
   
   private JdbcWrapper jdbc;

   public LogRepository(JdbcWrapper jdbc) {
      this.jdbc = jdbc;
   }
   
   public Long ultimoID() throws Exception {
      Long id = null;

      NativeSql nativeSql = new NativeSql(jdbc);

      try (ResultSet rs = nativeSql.executeQuery("SELECT (NVL(MAX(ID), 0) + 1) AS ID FROM AD_LOG")) {
          while (rs.next()) {
              id = rs.getLong("ID");
          }
      }

      NativeSql.releaseResources(nativeSql);

      return id;
   }
   
   public void adicionar(Log log) throws Exception {
      
      NativeSql nativeSql = new NativeSql(jdbc);
      nativeSql.setNamedParameter("ID", log.getId());
      nativeSql.setNamedParameter("DATA", UtilDate.convertToTimestamp(log.getData()));
      nativeSql.setNamedParameter("DEV_DADOS", log.getDevDados());
      nativeSql.setNamedParameter("DEV_MENSAGEM", log.getDevMensagem());
      nativeSql.setNamedParameter("STATUS", log.getStatus());
      nativeSql.setNamedParameter("MENSAGEM", log.getMensagem());
      nativeSql.setNamedParameter("CATEGORIA", log.getCategoria());
      nativeSql.executeUpdate(GeralQueryMap.LOG_ADICIONAR_QUERY);
      
      NativeSql.releaseResources(nativeSql);
      
   }

}
