package br.com.guaranamineiro.sankhya.model.entity;

public class NossoNumero {
   
   private Long id;
   private String valor;
   private Long proximo;
   
   public NossoNumero() {
      // TODO Auto-generated constructor stub
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getValor() {
      return valor;
   }

   public void setValor(String valor) {
      this.valor = valor;
   }

   public Long getProximo() {
      return proximo;
   }

   public void setProximo(Long proximo) {
      this.proximo = proximo;
   }   
   
}
