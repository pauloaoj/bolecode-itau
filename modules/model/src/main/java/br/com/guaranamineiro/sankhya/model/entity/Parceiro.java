package br.com.guaranamineiro.sankhya.model.entity;

public class Parceiro {

   private Long id;
   private String nome;
   private String nomeRazaoSocial;
   private String tipo;
   private String cpfCnpj;
   private String uf;
   private String nomeUF;
   private String nomeCidade;
   private String nomeBairro;
   private String nomeEndereco;
   private String numero;
   private String cep;
   private String complemento;

   public Parceiro() {
      // TODO Auto-generated constructor stub
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNome() {
      return nome;
   }

   public void setNome(String nome) {
      this.nome = nome;
   }

   public String getNomeRazaoSocial() {
      return nomeRazaoSocial;
   }

   public void setNomeRazaoSocial(String nomeRazaoSocial) {
      this.nomeRazaoSocial = nomeRazaoSocial;
   }

   public String getTipo() {
      return tipo;
   }

   public void setTipo(String tipo) {
      this.tipo = tipo;
   }

   public String getCpfCnpj() {
      return cpfCnpj;
   }

   public void setCpfCnpj(String cpfCnpj) {
      this.cpfCnpj = cpfCnpj;
   }

   public String getUf() {
      return uf;
   }

   public void setUf(String uf) {
      this.uf = uf;
   }

   public String getNomeUF() {
      return nomeUF;
   }

   public void setNomeUF(String nomeUF) {
      this.nomeUF = nomeUF;
   }

   public String getNomeCidade() {
      return nomeCidade;
   }

   public void setNomeCidade(String nomeCidade) {
      this.nomeCidade = nomeCidade;
   }

   public String getNomeBairro() {
      return nomeBairro;
   }

   public void setNomeBairro(String nomeBairro) {
      this.nomeBairro = nomeBairro;
   }

   public String getNomeEndereco() {
      return nomeEndereco;
   }

   public void setNomeEndereco(String nomeEndereco) {
      this.nomeEndereco = nomeEndereco;
   }

   public String getNumero() {
      return numero;
   }

   public void setNumero(String numero) {
      this.numero = numero;
   }

   public String getCep() {
      return cep;
   }

   public void setCep(String cep) {
      this.cep = cep;
   }

   public String getComplemento() {
      return complemento;
   }

   public void setComplemento(String complemento) {
      this.complemento = complemento;
   }

}
