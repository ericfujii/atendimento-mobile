package br.com.eric.atendimentomobile.entidade.envio;


import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EAcaoPedido;
import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;
import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

public class MobileEnvioAlterarCancelarPedido extends MobileEnvio {

	@XmlElement(name = "item_pedido")
	private ItemPedido itemPedido;
	
	@XmlElement(name = "acao_pedido")
	private EAcaoPedido acaoPedido;

	public MobileEnvioAlterarCancelarPedido(AtendimentoMobile atendimentoMobile, ItemPedido itemPedido, EAcaoPedido acaoPedido) {
		super(atendimentoMobile, EMobileRecursoCodigo.ALTERAR_CANCELAR_PEDIDO);
		this.itemPedido = itemPedido;
		this.acaoPedido = acaoPedido;
	}

	public ItemPedido getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(ItemPedido itemPedido) {
		this.itemPedido = itemPedido;
	}
	
	public EAcaoPedido getAcaoPedido() {
		return acaoPedido;
	}
	
	public void setAcaoPedido(EAcaoPedido acaoPedido) {
		this.acaoPedido = acaoPedido;
	}
}
