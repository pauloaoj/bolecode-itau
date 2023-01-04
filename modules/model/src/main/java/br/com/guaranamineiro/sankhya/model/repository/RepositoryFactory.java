package br.com.guaranamineiro.sankhya.model.repository;

import br.com.sankhya.jape.dao.JdbcWrapper;

public class RepositoryFactory {

   public static FinanceiroRepository getFinanceiroRepository(JdbcWrapper jdbc) {
      FinanceiroRepository financeiroRepository = new FinanceiroRepository(jdbc);
      return financeiroRepository;
   }

   public static ParceiroRepository getParceiroRepository(JdbcWrapper jdbc) {
      ParceiroRepository parceiroRepository = new ParceiroRepository(jdbc);
      return parceiroRepository;
   }
   
   public static LogRepository getLogRepository(JdbcWrapper jdbc) {
      LogRepository logRepository = new LogRepository(jdbc);
      return logRepository;
   }

}
