package br.com.eric.atendimentomobile.entidade.envio;

import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;
import br.com.eric.atendimentomobile.entidade.Pedido;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileEnvioPedido extends MobileEnvio {

    @XmlElement(name = "pedido")
    private Pedido pedido;

    public MobileEnvioPedido(AtendimentoMobile atendimentoMobile, Pedido pedido) {
        super(atendimentoMobile, EMobileRecursoCodigo.SALVAR_PEDIDO);
        this.pedido = pedido;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
}
