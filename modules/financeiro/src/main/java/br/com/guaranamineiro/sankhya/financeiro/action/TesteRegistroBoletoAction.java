package br.com.guaranamineiro.sankhya.financeiro.action;

import java.util.ArrayList;
import java.util.Calendar;
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
import br.com.sankhya.jape.core.JapeSession;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.modelcore.util.EntityFacadeFactory;

public class TesteRegistroBoletoAction implements AcaoRotinaJava {
   
   private JdbcWrapper jdbc;
   
   private FinanceiroBoletoService financeiroBoletoService;
   private LogService logService;
   
   private JsonObject login;
   private List<Financeiro> lista;
   
   private ErroDto<Financeiro> erro;
   private List<ErroDto<Financeiro>> erroLista;

   @Override
   public void doAction(ContextoAcao ctx) throws Exception {

      boolean permitido = UtilEnv.isPermision();
      if (!permitido) {
         ctx.setMensagemRetorno(String.format("%s porém está no %s, não é permitido.", 
               UtilEnv.description(), UtilEnv.descriptionApp()));
         return;
      }
            
      ModuleDependencies.init();
      
      try {

         jdbc = EntityFacadeFactory.getDWFFacade().getJdbcWrapper();
         
         financeiroBoletoService = new FinanceiroBoletoService(jdbc);
         
         lista = financeiroBoletoService.buscarParaRegistrarBoleto();         
         login = financeiroBoletoService.autenticar(Banco.ITAU);
         
      } catch (Exception e) {
         System.out.println(UtilException.get(e));
         lista = null;
      }
      
      if (lista != null && lista.size() > 0) {
         
         erroLista = new ArrayList<>();
         
         JapeSession.SessionHandle hnd = null;
         
         for (final Financeiro financeiro : lista) {
            
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
            } finally {
               if (hnd != null) {
                  JapeSession.close(hnd);
              }
            }            
         }         
      }
      
      if (erroLista != null && erroLista.size() > 0) {
         
         JapeSession.SessionHandle hnd = null;
         
         for (final ErroDto<Financeiro> erro : erroLista) {
            
            try {
               
               hnd = JapeSession.open();
               hnd.setCanTimeout(false);
       
               hnd.execWithTX(new JapeSession.TXBlock() {
                  
                  @Override
                  public void doWithTx() throws Exception {
                 
                     financeiroBoletoService = new FinanceiroBoletoService(jdbc);
                     financeiroBoletoService.atualizarTentativaRegistroBoleto(erro.getDado().getId());
                        
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
  
   }

}
