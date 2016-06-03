package br.com.eric.atendimentomobile.entidade;

import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.Table;
import br.com.eric.atendimentomobile.utils.annotations.XmlElement;

@Table(name = "usuario")
public class Usuario {

	@Id
	@XmlElement(name = "id")
	private Integer id;
	
	@XmlElement(name = "nome")
	private String nome;
	
	@XmlElement(name = "login")
	private String login;
	
	@XmlElement(name = "situacao")
	private ESituacao situacao = ESituacao.ATIVO;
	
	public Usuario(Integer id){
		this.id = id;
	}
	
	public Usuario(){}
	
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
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public ESituacao getSituacao() {
		return situacao;
	}
	public void setSituacao(ESituacao situacao) {
		this.situacao = situacao;
	}
}
