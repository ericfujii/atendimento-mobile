package br.com.eric.atendimentomobile.entidade.retorno;


import java.util.List;

import br.com.eric.atendimentomobile.entidade.Mensagem;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileRetornoVerificarMensagens extends MobileRetorno {
	@XmlElement(name = "mensagens")
	private List<Mensagem> mensagens;

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}
}
