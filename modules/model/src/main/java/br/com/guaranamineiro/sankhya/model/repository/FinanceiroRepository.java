package br.com.guaranamineiro.sankhya.model.repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import br.com.guaranamineiro.sankhya.model.entity.Financeiro;
import br.com.guaranamineiro.sankhya.model.entity.NossoNumero;
import br.com.guaranamineiro.sankhya.model.query.FinanceiroQueryMap;
import br.com.guaranamineiro.sankhya.model.util.UtilDate;
import br.com.guaranamineiro.sankhya.model.util.UtilProperties;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;

public class FinanceiroRepository {

   private JdbcWrapper jdbc;

   public FinanceiroRepository(JdbcWrapper jdbc) {
      this.jdbc = jdbc;
   }

   public List<Financeiro> buscarParaRegistrarBoleto() throws Exception {
      Properties properties = UtilProperties.get();

      List<Financeiro> financeiroLista = new ArrayList<>();

      String sql = FinanceiroQueryMap.BOLETO_PARA_REGISTRO_QUERY;
      sql = sql.replace("{{{tamanho}}}", properties.getProperty("TAMANHO_MAXIMO_REGISTRO_BOLETO"));

      NativeSql nativeSql = new NativeSql(jdbc);
      try (ResultSet rs = nativeSql.executeQuery(sql)) {

         Financeiro financeiro = null;

         while (rs.next()) {
            financeiro = get(rs);
            financeiroLista.add(financeiro);
         }
      }
      
      NativeSql.releaseResources(nativeSql);

      return financeiroLista;
   }
   
   public Financeiro buscarParaRegistrarBoleto(Long id) throws Exception {
      Financeiro financeiro = null;

      String sql = FinanceiroQueryMap.BOLETO_PARA_REGISTRO_FILTRO_QUERY;
      sql = sql.replace("{{{filtro}}}", String.format("AND TGFFIN.NUFIN = %d", id));

      NativeSql nativeSql = new NativeSql(jdbc);
      try (ResultSet rs = nativeSql.executeQuery(sql)) {

         while (rs.next()) {
            financeiro = get(rs);
         }
      }
      
      NativeSql.releaseResources(nativeSql);

      return financeiro;
   }
   
   public void atualizarTentativaRegistroBoleto(Long id) throws Exception {
      NativeSql nativeSql = new NativeSql(jdbc);
      nativeSql.setNamedParameter("NUFIN", id);
  
      nativeSql.executeUpdate(FinanceiroQueryMap.BOLETO_TENTATIVA_ATUALIZAR_QUERY);
      
      NativeSql.releaseResources(nativeSql);
   }
   
   public NossoNumero proximoNossoNumero(Long idConta) throws Exception {
      NossoNumero nossoNumero = null;

      NativeSql nativeSql = new NativeSql(jdbc);
      nativeSql.setNamedParameter("CONTA", idConta);
      
      try (ResultSet rs = nativeSql.executeQuery(FinanceiroQueryMap.BOLETO_NOSSONUMERO_QUERY)) {

         nossoNumero = new NossoNumero();
         
         while (rs.next()) {
            nossoNumero.setId(rs.getLong("CODCTABCOINT"));
            nossoNumero.setValor(rs.getString("VALOR"));
            nossoNumero.setProximo(rs.getLong("PROXIMO"));
         }
      }
      
      NativeSql.releaseResources(nativeSql);

      return nossoNumero;
   }
   
   public void atualizarNossoNumero(NossoNumero nossoNumero) throws Exception {
      
      NativeSql nativeSql = new NativeSql(jdbc);
      nativeSql.setNamedParameter("CODCTABCOINT", nossoNumero.getId());
      nativeSql.setNamedParameter("REMBCO", nossoNumero.getProximo());
      
      nativeSql.executeUpdate(FinanceiroQueryMap.BOLETO_NOSSONUMERO_ATUALIZAR_QUERY);
      
      NativeSql.releaseResources(nativeSql);
   }
   
   public void atualizarRegistroBoleto(Financeiro financeiro) throws Exception {
      
      NativeSql nativeSql = new NativeSql(jdbc);
      nativeSql.setNamedParameter("NUFIN", financeiro.getId());
      nativeSql.setNamedParameter("NOSSONUM", financeiro.getBoletoNossoNumero());
      nativeSql.setNamedParameter("LINHADIGITAVEL", financeiro.getBoletoLinhaDigitavel());
      nativeSql.setNamedParameter("CODIGOBARRA", financeiro.getBoletoCodigoBarra());
      nativeSql.setNamedParameter("EMVPIX", financeiro.getBoletoPix());
      
      nativeSql.executeUpdate(FinanceiroQueryMap.BOLETO_REGISTRO_ATUALIZAR_QUERY);
      
      NativeSql.releaseResources(nativeSql);
   }

   private Financeiro get(ResultSet rs) throws Exception {
      Financeiro financeiro = new Financeiro();
      financeiro.setId(rs.getLong("NUFIN"));
      financeiro.setIdNota(rs.getString("NUNOTA") != null ? rs.getLong("NUNOTA") : null);
      financeiro.setIdParceiro(rs.getLong("CODPARC"));
      financeiro.setDataEmissao(rs.getString("DTNEG") != null ? UtilDate.stringToDate(rs.getString("DTNEG")) : null);
      financeiro.setDataVencimento(rs.getString("DTVENC") != null ? UtilDate.stringToDate(rs.getString("DTVENC")) : null);
      financeiro.setDesdobramento(rs.getString("DESDOBRAMENTO"));
      financeiro.setVlrDesdobramento(rs.getBigDecimal("VLRDESDOB"));
      financeiro.setBoletoNossoNumero(rs.getString("NOSSONUM"));
      financeiro.setBoletoLinhaDigitavel(rs.getString("LINHADIGITAVEL"));
      financeiro.setBoletoCodigoBarra(rs.getString("CODIGOBARRA"));
      financeiro.setBoletoPix(rs.getString("EMVPIX"));
      return financeiro;
   }
   
   
}
