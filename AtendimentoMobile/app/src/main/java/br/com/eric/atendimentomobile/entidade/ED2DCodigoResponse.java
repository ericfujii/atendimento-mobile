package br.com.eric.atendimentomobile.entidade;

public enum ED2DCodigoResponse {

	OK("1"),
	ERROR("2");
	
	private final String codigo;
	
	private ED2DCodigoResponse(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
}
