package br.com.guaranamineiro.sankhya.model.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Financeiro {

   private Long id;
   private Long idNota;
   private Long idParceiro;
   private Date dataEmissao;
   private Date dataVencimento;
   private String desdobramento;
   private BigDecimal vlrDesdobramento;
   private Long boletoProximoNossoNumero;
   private String boletoNossoNumero;
   private String boletoLinhaDigitavel;
   private String boletoCodigoBarra;
   private String boletoPix;
   
   private Parceiro parceiro;

   public Financeiro() {

   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getIdNota() {
      return idNota;
   }

   public void setIdNota(Long idNota) {
      this.idNota = idNota;
   }

   public Long getIdParceiro() {
      return idParceiro;
   }

   public void setIdParceiro(Long idParceiro) {
      this.idParceiro = idParceiro;
   }

   public Date getDataEmissao() {
      return dataEmissao;
   }

   public void setDataEmissao(Date dataEmissao) {
      this.dataEmissao = dataEmissao;
   }

   public Date getDataVencimento() {
      return dataVencimento;
   }

   public void setDataVencimento(Date dataVencimento) {
      this.dataVencimento = dataVencimento;
   }

   public String getDesdobramento() {
      return desdobramento;
   }

   public void setDesdobramento(String desdobramento) {
      this.desdobramento = desdobramento;
   }

   public BigDecimal getVlrDesdobramento() {
      return vlrDesdobramento;
   }

   public void setVlrDesdobramento(BigDecimal vlrDesdobramento) {
      this.vlrDesdobramento = vlrDesdobramento;
   }

   public Long getBoletoProximoNossoNumero() {
      return boletoProximoNossoNumero;
   }

   public void setBoletoProximoNossoNumero(Long boletoProximoNossoNumero) {
      this.boletoProximoNossoNumero = boletoProximoNossoNumero;
   }

   public String getBoletoNossoNumero() {
      return boletoNossoNumero;
   }

   public void setBoletoNossoNumero(String boletoNossoNumero) {
      this.boletoNossoNumero = boletoNossoNumero;
   }

   public String getBoletoLinhaDigitavel() {
      return boletoLinhaDigitavel;
   }

   public void setBoletoLinhaDigitavel(String boletoLinhaDigitavel) {
      this.boletoLinhaDigitavel = boletoLinhaDigitavel;
   }

   public String getBoletoCodigoBarra() {
      return boletoCodigoBarra;
   }

   public void setBoletoCodigoBarra(String boletoCodigoBarra) {
      this.boletoCodigoBarra = boletoCodigoBarra;
   }

   public String getBoletoPix() {
      return boletoPix;
   }

   public void setBoletoPix(String boletoPix) {
      this.boletoPix = boletoPix;
   }

   public Parceiro getParceiro() {
      return parceiro;
   }

   public void setParceiro(Parceiro parceiro) {
      this.parceiro = parceiro;
   }
  
}
