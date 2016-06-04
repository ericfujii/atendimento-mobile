package br.com.eric.atendimentomobile.entidade.retorno;

import java.util.Calendar;

import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileRetornoMensagem extends MobileRetorno {

    @XmlElement(name = "id_mensagem")
    private Integer idMensagem;

    @XmlElement(name = "hora_mensagem")
    private Calendar horaMensagem;

    public Integer getIdMensagem() {
        return idMensagem;
    }

    public void setIdMensagem(Integer idMensagem) {
        this.idMensagem = idMensagem;
    }

    public Calendar getHoraMensagem() {
        return horaMensagem;
    }

    public void setHoraMensagem(Calendar horaMensagem) {
        this.horaMensagem = horaMensagem;
    }
}
