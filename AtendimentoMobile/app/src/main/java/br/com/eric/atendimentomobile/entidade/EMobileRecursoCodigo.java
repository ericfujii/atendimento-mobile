package br.com.eric.atendimentomobile.entidade;

/**
 * Created by eric on 10/02/15.
 */
public enum EMobileRecursoCodigo {

    VERIFICAR_PACOTE("verificarPacote"),
    LOGAR("logar"),
    SALVAR_PEDIDO("salvarPedido"),
    RECUPERAR_FILA("recuperarFila");

    private String codigo;

    private EMobileRecursoCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
