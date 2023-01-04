package br.com.guaranamineiro.sankhya.model.service;

import java.util.List;

import com.google.gson.JsonObject;

import br.com.guaranamineiro.sankhya.model.BoletoService;
import br.com.guaranamineiro.sankhya.model.constant.Banco;
import br.com.guaranamineiro.sankhya.model.constant.MessageType;
import br.com.guaranamineiro.sankhya.model.entity.Financeiro;
import br.com.guaranamineiro.sankhya.model.entity.NossoNumero;
import br.com.guaranamineiro.sankhya.model.entity.Parceiro;
import br.com.guaranamineiro.sankhya.model.repository.FinanceiroRepository;
import br.com.guaranamineiro.sankhya.model.repository.ParceiroRepository;
import br.com.guaranamineiro.sankhya.model.repository.RepositoryFactory;
import br.com.guaranamineiro.sankhya.model.util.UtilCodigoBarra;
import br.com.guaranamineiro.sankhya.model.util.UtilException;
import br.com.sankhya.jape.dao.JdbcWrapper;

public class FinanceiroBoletoService {
   
   private JdbcWrapper jdbc;
   
   private FinanceiroRepository financeiroRepository;
   private ParceiroRepository parceiroRepository;

   public FinanceiroBoletoService(JdbcWrapper jdbc) {
      this.jdbc = jdbc;
      
      financeiroRepository = RepositoryFactory.getFinanceiroRepository(jdbc);
      parceiroRepository = RepositoryFactory.getParceiroRepository(jdbc);
   }
   
   public List<Financeiro> buscarParaRegistrarBoleto() throws Exception {
      List<Financeiro> financeiroLista = financeiroRepository.buscarParaRegistrarBoleto();
      return financeiroLista;
   }
   
   public Financeiro buscarParaRegistrarBoleto(Long id) throws Exception {
      Financeiro financeiro = financeiroRepository.buscarParaRegistrarBoleto(id);
      return financeiro;
   }
   
   public void atualizarTentativaRegistroBoleto(Long id) throws Exception {
      financeiroRepository.atualizarTentativaRegistroBoleto(id);
   }
   
   public JsonObject autenticar(Banco banco) throws Exception {
      
      BoletoService boletoService = Banco.getBoletoService(banco, jdbc);
      if (boletoService == null) {
         throw new UtilException(MessageType.FINANCEIRO_REGISTRO_BOLETO, 
               "Não foi possível localizar o serviço de registro de boleto.");
      }
      
      JsonObject auth = boletoService.autenticar();
      return auth;
   }
   
   public void registrar(Banco banco, JsonObject login, Financeiro financeiro) throws Exception {
      BoletoService boletoService = Banco.getBoletoService(banco, jdbc);
      if (boletoService == null) {
         throw new UtilException(MessageType.FINANCEIRO_REGISTRO_BOLETO, 
               "Não foi possível localizar o serviço de registro de boleto.");
      }
      
      Long idConta = Banco.getConta(banco);
      if (idConta == null) {
         throw new UtilException(MessageType.FINANCEIRO_REGISTRO_BOLETO, 
               "Não foi possível localizar a conta do banco para o registro de boleto.");
      }
      
      Parceiro parceiro = parceiroRepository.buscarPorId(financeiro.getIdParceiro());
      NossoNumero nossoNumero = financeiroRepository.proximoNossoNumero(idConta);
      
      financeiro.setBoletoProximoNossoNumero(nossoNumero.getProximo());
      
      JsonObject parametro = boletoService.corpo(financeiro, parceiro);
      JsonObject resultado = boletoService.registrar(login, parametro);

      JsonObject dadosBoleto = resultado.get("data").getAsJsonObject()
            .get("data").getAsJsonObject()
            .get("dado_boleto").getAsJsonObject()
            .get("dados_individuais_boleto").getAsJsonArray()
            .get(0).getAsJsonObject();
      JsonObject dadosPix = resultado.get("data").getAsJsonObject()
            .get("data").getAsJsonObject()
            .get("dados_qrcode").getAsJsonObject();
 
      String linhaDigitavel = dadosBoleto.get("codigo_barras").getAsString();
      linhaDigitavel = UtilCodigoBarra.toTypefulLine(linhaDigitavel);
      
      financeiro.setBoletoNossoNumero(nossoNumero.getValor());
      financeiro.setBoletoCodigoBarra(dadosBoleto.get("codigo_barras").getAsString());
      financeiro.setBoletoPix(dadosPix.get("emv").getAsString());
      financeiro.setBoletoLinhaDigitavel(linhaDigitavel);
   
      financeiroRepository.atualizarNossoNumero(nossoNumero);
      financeiroRepository.atualizarRegistroBoleto(financeiro);      
   }
   
}
