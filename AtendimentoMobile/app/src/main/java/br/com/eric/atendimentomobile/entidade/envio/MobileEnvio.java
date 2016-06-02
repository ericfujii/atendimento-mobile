package br.com.eric.atendimentomobile.entidade.envio;

import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;
import br.com.eric.atendimentomobile.utils.annotations.XmlTransient;

/**
 * Created by marceloeugenio on 3/16/16.
 */
public abstract class MobileEnvio implements MobileEnviavel {

    @XmlElement(name = "id_usuario")
    private Integer idUsuario;
    @XmlTransient
    private EMobileRecursoCodigo mobileRecursoCodigo;

    protected MobileEnvio(AtendimentoMobile atendimentoMobile, EMobileRecursoCodigo mobileRecursoCodigo) {
        this.idUsuario = atendimentoMobile.getIdUsuario();
        this.mobileRecursoCodigo = mobileRecursoCodigo;
    }

    @Override
    public Integer getIdUsuario() {
        return idUsuario;
    }

    @Override
    public EMobileRecursoCodigo getMobileRecursoCodigo() {
        return mobileRecursoCodigo;
    }
}