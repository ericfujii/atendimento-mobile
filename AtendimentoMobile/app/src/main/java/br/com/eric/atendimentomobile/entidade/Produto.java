package br.com.eric.atendimentomobile.entidade;


import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.JoinColumn;
import br.com.eric.atendimentomobile.utils.annotations.Table;
import br.com.eric.atendimentomobile.utils.annotations.Transient;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;
import br.com.eric.atendimentomobile.utils.annotations.XmlTransient;

@Table(name="produto")
public class Produto {

	@Id
	@XmlElement(name = "id")
	private Integer id;
	
	@XmlElement(name = "nome")
	private String nome;
	
	@JoinColumn(name = "_produto_tipo")
	@XmlElement(name = "produto_tipo")
	private ProdutoTipo produtoTipo;
	
	@XmlElement(name = "ordem")
	private Integer ordem = 0;
	
	@XmlElement(name = "situacao")
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
	public ProdutoTipo getProdutoTipo() {
		return produtoTipo;
	}
	public void setProdutoTipo(ProdutoTipo produtoTipo) {
		this.produtoTipo = produtoTipo;
	}
	public Integer getOrdem() {
		return ordem;
	}
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
}
