package br.com.guaranamineiro.sankhya.financeiro.action;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonObject;

import br.com.guaranamineiro.sankhya.financeiro.ModuleDependencies;
import br.com.guaranamineiro.sankhya.model.constant.Banco;
import br.com.guaranamineiro.sankhya.model.constant.MessageType;
import br.com.guaranamineiro.sankhya.model.dto.ErroDto;
import br.com.guaranamineiro.sankhya.model.entity.Financeiro;
import br.com.guaranamineiro.sankhya.model.service.FinanceiroBoletoService;
import br.com.guaranamineiro.sankhya.model.service.LogService;
import br.com.guaranamineiro.sankhya.model.util.UtilEnv;
import br.com.guaranamineiro.sankhya.model.util.UtilException;
import br.com.guaranamineiro.sankhya.model.util.UtilJson;
import br.com.sankhya.extensions.actionbutton.AcaoRotinaJava;
import br.com.sankhya.extensions.actionbutton.ContextoAcao;
import br.com.sankhya.extensions.actionbutton.Registro;
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class RegistroBoletoAction implements AcaoRotinaJava {
   
   private JdbcWrapper jdbc;
   
   private FinanceiroBoletoService financeiroBoletoService;
   private LogService logService;
   
   private JsonObject login;
   private Financeiro financeiro;
   
   private ErroDto<Financeiro> erro;
   private List<ErroDto<Financeiro>> erroLista;

   @Override
   public void doAction(ContextoAcao ctx) throws Exception {
      
      boolean permitido = UtilEnv.isPermision();
      if (!permitido) {
         ctx.setMensagemRetorno(String.format("%s porém está no %s, não é permitido!", 
               UtilEnv.description(), UtilEnv.descriptionApp()));
         return;
      }
      
      if (ctx.getLinhas().length == 0) {
         ctx.setMensagemRetorno("Selecione um financeiro para registrar o boleto!");
         return;
      }
      
      if (ctx.getLinhas().length > 1) {
         ctx.setMensagemRetorno("Selecione apenas um financeiro para registrar o boleto!");
         return;
      }
            
      Registro registro = ctx.getLinhas()[0];
      
      String registrado = (String) registro.getCampo("AD_BR_REGISTRADO");
      if (registrado != null && registrado.equals("S")) {
         ctx.setMensagemRetorno("Boleto já registrado!");
         return;
      }
      
      Date dhbaixa = (Date) registro.getCampo("DHBAIXA");
      if (dhbaixa != null) {
         ctx.setMensagemRetorno("Financeiro já baixado, não é possível registrar o boleto!");
         return;
      }
      
      ModuleDependencies.init();
      
      BigDecimal nufin = (BigDecimal) registro.getCampo("NUFIN");
            
      try {

         jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
         
         financeiroBoletoService = new FinanceiroBoletoService(jdbc);
         
         financeiro = financeiroBoletoService.buscarParaRegistrarBoleto(nufin.longValue());         
         login = financeiroBoletoService.autenticar(Banco.ITAU);
         
      } catch (Exception e) {
         System.out.println(UtilException.get(e));
         financeiro = null;
      }
      
      if (financeiro != null) {
         
         JapeSession.SessionHandle hnd = null;
         
         try {
            
            hnd = JapeSession.open();
            hnd.setCanTimeout(false);
    
            hnd.execWithTX(new JapeSession.TXBlock() {
               
               @Override
               public void doWithTx() throws Exception {
              
                  try {
                     
                     financeiroBoletoService = new FinanceiroBoletoService(jdbc);
                     financeiroBoletoService.registrar(Banco.ITAU, login, financeiro);
                     
                     logService = new LogService(jdbc);
                     logService.adicionar(Calendar.getInstance().getTime(), 
                           null, 
                           null, 
                           MessageType.SUCESSO.name(), 
                           String.format("NUFIN %d boleto registrado com sucesso.", financeiro.getId()), 
                           MessageType.FINANCEIRO_REGISTRO_BOLETO.name());
                     
                  } catch (Exception e) {
                     erro = new ErroDto<Financeiro>();
                     erro.setDataMensagem(Calendar.getInstance().getTime());
                     erro.setErroMensagem(e.getMessage());
                     erro.setServidorMensagem(UtilException.get(e));
                     erro.setDado(financeiro);
                     
                     erroLista.add(erro);                        
                     throw e;
                  }
                     
               }
               
            });
            
         } catch (Exception e) {
            System.out.println(UtilException.get(e));
            ctx.setMensagemRetorno("Não foi possível registrar o boleto, verifique o log para verificar a causa!");
         } finally {
            if (hnd != null) {
               JapeSession.close(hnd);
            }
         }
         
         if (erroLista != null && erroLista.size() > 0) {
            
            for (final ErroDto<Financeiro> erro : erroLista) {
               
               try {
                  
                  hnd = JapeSession.open();
                  hnd.setCanTimeout(false);
          
                  hnd.execWithTX(new JapeSession.TXBlock() {
                     
                     @Override
                     public void doWithTx() throws Exception {
                    
                        logService = new LogService(jdbc);
                        logService.adicionar(erro.getDataMensagem(), 
                              UtilJson.convertToJson(erro.getDado()), 
                              erro.getServidorMensagem(), 
                              MessageType.ERRO.name(), 
                              erro.getErroMensagem(), 
                              MessageType.FINANCEIRO_REGISTRO_BOLETO.name());
                           
                     }
                     
                  });
                  
               } catch (Exception e) {
                  System.out.println(UtilException.get(e));
               } finally {
                  if (hnd != null) {
                     JapeSession.close(hnd);
                  }
               }            
            }
            
         }
         
      } else {
         ctx.setMensagemRetorno("Financeiro não pode ser registrado, verifique as informações!");
      }
            
   }

}
