package br.com.eric.atendimentomobile.entidade.retorno;

import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileRetornoLogin extends MobileRetorno {

    @XmlElement(name = "id_usuario")
    private Integer idUsuario;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
