package br.com.eric.atendimentomobile.entidade;

import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.Table;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

@Table(name="produto_tipo")
public class ProdutoTipo {

	@Id
	@XmlElement(name="id")
	private Integer id;
	
	@XmlElement(name="nome")
	private String nome;
	
	@XmlElement(name="bebida")
	private Boolean bebida;

	@XmlElement(name="ordem")
	private Integer ordem;
	
	@XmlElement(name="situacao")
	private ESituacao situacao = ESituacao.ATIVO;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public ESituacao getSituacao() {
		return situacao;
	}
	public void setSituacao(ESituacao situacao) {
		this.situacao = situacao;
	}
	public Boolean getBebida() {
		return bebida;
	}
	public void setBebida(Boolean bebida) {
		this.bebida = bebida;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
