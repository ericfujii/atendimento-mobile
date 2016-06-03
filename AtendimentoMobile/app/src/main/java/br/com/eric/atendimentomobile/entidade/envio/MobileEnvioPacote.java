package br.com.eric.atendimentomobile.entidade.envio;

import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;

public class MobileEnvioPacote extends MobileEnvio {

    public MobileEnvioPacote(AtendimentoMobile atendimentoMobile) {
        super(atendimentoMobile, EMobileRecursoCodigo.VERIFICAR_PACOTE);
    }

}
