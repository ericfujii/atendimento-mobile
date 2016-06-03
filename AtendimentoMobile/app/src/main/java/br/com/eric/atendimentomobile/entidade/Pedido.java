package br.com.eric.atendimentomobile.entidade;

import java.util.Calendar;
import java.util.List;

import br.com.eric.atendimentomobile.utils.annotations.Column;
import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.JoinColumn;
import br.com.eric.atendimentomobile.utils.annotations.Table;
import br.com.eric.atendimentomobile.utils.annotations.Transient;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

@Table(name = "pedido")
public class Pedido {
	
	@Id
	@XmlElement(name = "id")
	private Integer id;
	
	@XmlElement(name = "cliente")
	private String cliente;
	
	@Column(name = "tipo_pedido")
	@XmlElement(name = "tipo_pedido")
	private ETipoPedido tipoPedido;
	
	@JoinColumn(name = "_usuario")
	@XmlElement(name = "usuario")
	private Usuario usuario;
	
	@Column(name = "data_hora_cadastro")
	@XmlElement(name = "data_hora_cadastro")
	private Calendar dataHoraCadatro;

	@Transient
	private List<ItemPedido> pedidos;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCliente() {
		return cliente;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Calendar getDataHoraCadatro() {
		return dataHoraCadatro;
	}
	public void setDataHoraCadatro(Calendar dataHoraCadatro) {
		this.dataHoraCadatro = dataHoraCadatro;
	}

	public List<ItemPedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<ItemPedido> pedidos) {
		this.pedidos = pedidos;
	}
}
