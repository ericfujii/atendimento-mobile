package br.com.eric.atendimentomobile.entidade;

public enum ESituacaoPedido {
	NOVO("#FFC800"),
	AVISADO("#87CEEB"),
	EDITAR("#FF6347"),
	ENVIADO("#98FB98"),
	CANCELADO("#FF6A6A"),
	FINALIZADO("#98FB98");
	
	ESituacaoPedido(String corFundo) {
		this.corFundo = corFundo;
	}
	
	private String corFundo;

	public String getCorFundo() {
		return corFundo;
	}
}
