package br.com.eric.atendimentomobile.entidade;

import android.app.Application;

/**
 * Created by Eric on 01/06/2016.
 */
public class AtendimentoMobile extends Application {
    private Integer idUsuario;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
