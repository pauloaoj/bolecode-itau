package br.com.guaranamineiro.sankhya.model.entity;

import java.util.Date;

public class Log {
   
   private Long id;
   private Date data;
   private String devDados;
   private String devMensagem;
   private String status;
   private String mensagem;
   private String categoria;
   
   public Log() {
      // TODO Auto-generated constructor stub
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Date getData() {
      return data;
   }

   public void setData(Date data) {
      this.data = data;
   }

   public String getDevDados() {
      return devDados;
   }

   public void setDevDados(String devDados) {
      this.devDados = devDados;
   }

   public String getDevMensagem() {
      return devMensagem;
   }

   public void setDevMensagem(String devMensagem) {
      this.devMensagem = devMensagem;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getMensagem() {
      return mensagem;
   }

   public void setMensagem(String mensagem) {
      this.mensagem = mensagem;
   }

   public String getCategoria() {
      return categoria;
   }

   public void setCategoria(String categoria) {
      this.categoria = categoria;
   }   
}
