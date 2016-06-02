package br.com.eric.atendimentomobile.entidade.retorno;

import java.util.Map;

import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

/**
 * Created by marceloeugenio on 3/16/16.
 */
public abstract class MobileRetorno implements MobileRetornavel {

    @XmlElement(name = "mensagem")
    private String mensagem;
    @XmlElement(name = "codigo_retorno")
    private String codigoRetorno;
    @XmlElement(name = "extras")
    private Map<String, Object> extras;

    @Override
    public String getMensagem() {
        return mensagem;
    }

    @Override
    public String getCodigoRetorno() {
        return codigoRetorno;
    }

    @Override
    public Map<String, Object> getExtras() {
        return extras;
    }
}
