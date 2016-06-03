package br.com.eric.atendimentomobile.entidade.retorno;

import java.util.List;

import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileRetornoFila extends MobileRetorno {

    @XmlElement(name = "itens_pedidos")
    private List<ItemPedido> itensPedidos;

    public List<ItemPedido> getItensPedidos() {
        return itensPedidos;
    }

    public void setItensPedidos(List<ItemPedido> itensPedidos) {
        this.itensPedidos = itensPedidos;
    }
}
