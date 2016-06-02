package br.com.eric.atendimentomobile.entidade.envio;

import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

/**
 * Created by desenvolvimento on 17/03/16.
 */
public class MobileEnvioLogin extends MobileEnvio {

    @XmlElement(name = "login")
    private String login;
    @XmlElement(name = "senha")
    private String senha;

    public  MobileEnvioLogin(AtendimentoMobile atendimentoMobile, String login, String senha) {
        super(atendimentoMobile, EMobileRecursoCodigo.LOGAR);
        this.setLogin(login);
        this.setSenha(senha);
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
