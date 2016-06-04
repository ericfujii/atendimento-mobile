package br.com.eric.atendimentomobile.entidade;


import java.util.Calendar;

import br.com.eric.atendimentomobile.utils.annotations.Column;
import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.JoinColumn;
import br.com.eric.atendimentomobile.utils.annotations.Table;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;
import br.com.eric.atendimentomobile.utils.annotations.XmlTransient;


@Table(name="mensagem")
public class Mensagem{
	@Id
	@XmlElement(name = "id")
	private Integer id;

	@XmlElement(name = "mensagem")
	private String mensagem;

	@XmlElement(name = "data_mensagem")
	@Column(name = "data_mensagem")
	private Calendar dataMensagem;

	@JoinColumn(name = "_remetente")
	@XmlElement(name = "remetente")
	private Usuario remetente;

	@JoinColumn(name="_destinatario")
	@XmlElement(name = "destinatario")
	private Usuario destinatario;

	@XmlTransient
	private EBoolean visualizada = EBoolean.FALSE;

	public Mensagem(){
	}

	public Mensagem(Integer id){
		this.id = id;
	}

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

	public Calendar getDataMensagem() {
		return dataMensagem;
	}

	public void setDataMensagem(Calendar dataMensagem) {
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

	public EBoolean getVisualizada() {
		return visualizada;
	}

	public void setVisualizada(EBoolean visualizada) {
		this.visualizada = visualizada;
	}
}
