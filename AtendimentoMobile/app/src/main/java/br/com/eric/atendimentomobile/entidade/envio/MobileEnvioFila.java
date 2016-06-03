package br.com.eric.atendimentomobile.entidade.envio;

import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;
import br.com.eric.atendimentomobile.entidade.Produto;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileEnvioFila extends MobileEnvio {

    @XmlElement(name = "produto")
    private Produto produto;

    public MobileEnvioFila(AtendimentoMobile atendimentoMobile, Produto produto) {
        super(atendimentoMobile, EMobileRecursoCodigo.RECUPERAR_FILA);
        this.produto = produto;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
