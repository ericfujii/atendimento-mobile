package br.com.eric.atendimentomobile.entidade.envio;

import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;
import br.com.eric.atendimentomobile.entidade.Mensagem;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileEnvioMensagem extends MobileEnvio {

	@XmlElement(name = "mensagem")
	private Mensagem mensagem;

	public MobileEnvioMensagem(AtendimentoMobile atendimentoMobile, Mensagem mensagem) {
		super(atendimentoMobile, EMobileRecursoCodigo.ENVIAR_MENSAGEM);
		this.mensagem = mensagem;
	}

	public Mensagem getMensagem() {
		return mensagem;
	}

	public void setMensagem(Mensagem mensagem) {
		this.mensagem = mensagem;
	}
	
	
}
