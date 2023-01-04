package br.com.guaranamineiro.sankhya.model.repository;

import java.sql.ResultSet;

import br.com.guaranamineiro.sankhya.model.entity.Parceiro;
import br.com.guaranamineiro.sankhya.model.query.GeralQueryMap;
import br.com.sankhya.jape.dao.JdbcWrapper;
import br.com.sankhya.jape.sql.NativeSql;

public class ParceiroRepository {

   private JdbcWrapper jdbc;

   public ParceiroRepository(JdbcWrapper jdbc) {
      this.jdbc = jdbc;
   }

   public Parceiro buscarPorId(Long id) throws Exception {
      Parceiro parceiro = null;

      NativeSql nativeSql = new NativeSql(jdbc);
      nativeSql.setNamedParameter("CODPARC", id);

      try (ResultSet rs = nativeSql.executeQuery(GeralQueryMap.PARCEIRO_QUERY)) {
         while (rs.next()) {
            parceiro = get(rs);
         }
      }

      return parceiro;
   }

   private Parceiro get(ResultSet rs) throws Exception {
      Parceiro parceiro = new Parceiro();
      parceiro.setId(rs.getLong("CODPARC"));
      parceiro.setNome(rs.getString("NOMEPARC"));
      parceiro.setNomeRazaoSocial(rs.getString("RAZAOSOCIAL"));
      parceiro.setTipo(rs.getString("TIPPESSOA"));
      parceiro.setCpfCnpj(rs.getString("CGC_CPF"));
      parceiro.setUf(rs.getString("UF"));
      parceiro.setNomeUF(rs.getString("NOME_UF"));
      parceiro.setNomeCidade(rs.getString("NOMECID"));
      parceiro.setNomeBairro(rs.getString("NOMEBAI"));
      parceiro.setCep(rs.getString("CEP"));
      parceiro.setComplemento(rs.getString("COMPLEMENTO"));
      parceiro.setNumero(rs.getString("NUMEND"));
      parceiro.setNomeEndereco(rs.getString("NOMEEND"));
      return parceiro;
   }

}
