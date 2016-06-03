package br.com.eric.atendimentomobile.entidade;


import java.util.Date;
import java.util.List;

import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.JoinColumn;
import br.com.eric.atendimentomobile.utils.annotations.Table;

@Table(name="mensagem")
public class Mensagem{


	@Id
	private Integer id;

	private String mensagem;

	private Date dataMensagem;

	@JoinColumn(name = "_remetente")
	private Usuario remetente;

	@JoinColumn(name="_destinatario")
	private Usuario destinatario;

	/* *************************************************************** */
	public Mensagem(){

	}

	public Mensagem(Integer id){
		this.id = id;
	}
	
	
	/* *************************************************************** */
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Date getDataMensagem() {
		return dataMensagem;
	}

	public void setDataMensagem(Date dataMensagem) {
		this.dataMensagem = dataMensagem;
	}

	public Usuario getRemetente() {
		return remetente;
	}

	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}

	public Usuario getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(Usuario destinatario) {
		this.destinatario = destinatario;
	}
}
