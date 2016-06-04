package br.com.eric.atendimentomobile.entidade.envio;

import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;

public class MobileEnvioVerificarMensagens extends MobileEnvio {

    public MobileEnvioVerificarMensagens(AtendimentoMobile atendimentoMobile) {
        super(atendimentoMobile, EMobileRecursoCodigo.VERIFICAR_MENSAGENS);
    }
}
