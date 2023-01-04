package br.com.guaranamineiro.sankhya.model.dto;

import java.util.Date;
import java.util.List;

public class ErroDto<T> {

	private String chave;
	private Date dataMensagem;
	private String erroMensagem;
	private String servidorMensagem;
	private T dado;
	private List<T> dados;
	
	public ErroDto() {
		// TODO Auto-generated constructor stub
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public Date getDataMensagem() {
		return dataMensagem;
	}

	public void setDataMensagem(Date dataMensagem) {
		this.dataMensagem = dataMensagem;
	}

	public String getErroMensagem() {
		return erroMensagem;
	}

	public void setErroMensagem(String erroMensagem) {
		this.erroMensagem = erroMensagem;
	}

	public String getServidorMensagem() {
		return servidorMensagem;
	}

	public void setServidorMensagem(String servidorMensagem) {
		this.servidorMensagem = servidorMensagem;
	}

	public T getDado() {
		return dado;
	}

	public void setDado(T dado) {
		this.dado = dado;
	}

	public List<T> getDados() {
		return dados;
	}

	public void setDados(List<T> dados) {
		this.dados = dados;
	}
}
