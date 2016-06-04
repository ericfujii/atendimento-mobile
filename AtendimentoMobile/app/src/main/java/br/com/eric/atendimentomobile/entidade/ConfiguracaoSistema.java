package br.com.eric.atendimentomobile.entidade;

import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.Table;

/**
 * Created by eric on 21-05-2015.
 */
@Table(name = "configuracao_sistema")
public class ConfiguracaoSistema {
    @Id
    private String descricao;
    private String valor;

    public ConfiguracaoSistema(){}

    public ConfiguracaoSistema(String descricao, String valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }


}
