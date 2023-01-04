package br.com.guaranamineiro.sankhya.model.service;

import java.util.Date;

import br.com.guaranamineiro.sankhya.model.entity.Log;
import br.com.guaranamineiro.sankhya.model.repository.LogRepository;
import br.com.guaranamineiro.sankhya.model.repository.RepositoryFactory;
import br.com.sankhya.jape.dao.JdbcWrapper;

public class LogService {
   
   private JdbcWrapper jdbc;
   
   private LogRepository logRepository;

   public LogService(JdbcWrapper jdbc) {
      this.jdbc = jdbc;
      
      logRepository = RepositoryFactory.getLogRepository(jdbc);
   }
   
   public void adicionar(Date data, String devDados, String devMensagem, String status, String mensagem, String categoria) throws Exception {
      
      Long id = logRepository.ultimoID();
      
      Log log = new Log();
      log.setId(id);
      log.setData(data);
      log.setDevDados(devDados);
      log.setDevMensagem(devMensagem);
      log.setStatus(status);
      log.setMensagem(mensagem);
      log.setCategoria(categoria);
      
      logRepository.adicionar(log);
   }
   
}
