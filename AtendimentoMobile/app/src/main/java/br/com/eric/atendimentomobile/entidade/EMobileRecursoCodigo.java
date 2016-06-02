package br.com.eric.atendimentomobile.entidade;

/**
 * Created by eric on 10/02/15.
 */
public enum EMobileRecursoCodigo {

    SALVAR_TABULACAO("salvarTabulacao"),
    SALVAR_LOCALIZACAO("salvarLocalizacao"),
    PESQUISAR_MAILING("pesquisarRuas"),
    VINCULAR_MAILING("vincularRuas"),
    RECUPERAR_MAILING("recuperarRuasSelecionadas"),
    RECUPERAR_AGENDAMENTO("recuperarAgendamento"),
    VERIFICAR_PACOTE("verificarPacote"),
    INICIAR_CONTADOR("iniciarContador"),
    RECUPERAR_RELATORIO("recuperarRelatorio"),
    LOGAR("logar"),
    RECUPERAR_RELATORIO_LOCALIZACAO("recuperarRelatorioLocalizacao"),
    RECUPERAR_CONFIGURACAO_LOCALIZACAO("recuperarConfiguracaoLocalizacao"),
    SALVAR_RUA("salvarRua"),
    PESQUISAR_MAILING_CONSOLIDADO("pesquisarMailingConsolidado"),
    BAIXAR_MAILING_CONSOLIDADO("baixarMailingConsolidado"),
    RECUPERAR_CONTADORES("recuperarContadores"),
    PESQUISAR_PROXY("proxy"),
    RECUPERAR_AGENTE_AUTORIZADO("recuperarAgenteAutorizado");

    private String codigo;

    private EMobileRecursoCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}
