package br.com.eric.atendimentomobile.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eric on 27/01/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{


    private static final int VERSAO = 1;
    private static final String BANCO = "multisales";

    public DatabaseHelper(Context context) {
        super(context, BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE versao_pacote (id INTEGER PRIMARY KEY, " +
                "versao NUMERIC, data_cadastro TEXT, data_atualizacao TEXT, descricao TEXT)");

        db.execSQL("CREATE TABLE contador_tabulacao (" +
                "tipo TEXT PRIMARY KEY, " +
                "validade_atualizacao TEXT, " +
                "quantidade_atual INTEGER, " +
                "quantidade_acumulado INTEGER)");

        db.execSQL("CREATE TABLE indicador_tabulacoes (" +
                "id INTEGER PRIMARY KEY, " +
                "quantidade_vendas INTEGER, " +
                "quantidade_nao_vendas INTEGER, " +
                "quantidade_agendamentos INTEGER)");

        db.execSQL("CREATE TABLE alerta (" +
                "id INTEGER PRIMARY KEY, " +
                " notificacao_codigo TEXT, " +
                " data_hora TEXT, " +
                " intent TEXT, " +
                " extra TEXT, " +
                " titulo TEXT, " +
                " texto TEXT, " +
                " alerta TEXT) ");

        db.execSQL("CREATE TABLE midia (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE motivo_tabulacao_tipo (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT, icone TEXT, retroalimentavel TEXT)");

        db.execSQL("CREATE TABLE motivo_tabulacao (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT, deve_baixar_hp TEXT, " +
                "_motivo_tabulacao_tipo INTEGER, concorrencia TEXT)");

        db.execSQL("CREATE TABLE motivo_migracao (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE concorrente (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, _produto_tipo INTEGER, situacao TEXT)");

        db.execSQL("CREATE TABLE estado (uf TEXT PRIMARY KEY, " +
                "nome TEXT)");

        db.execSQL("CREATE TABLE cidade (id INTEGER PRIMARY KEY, " +
                "nome TEXT, codigo TEXT, sigla TEXT, _estado TEXT)");

        db.execSQL("CREATE TABLE estado_civil (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE escolaridade (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE motivo_nao_venda (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT, _produto_tipo INTEGER)");

        db.execSQL("CREATE TABLE tabulacao (id INTEGER, " +
                "id_local INTEGER PRIMARY KEY, " +
                "completa TEXT, " +
                "_mailing_rua INTEGER, " +
                "_venda INTEGER, " +
                "_motivo_tabulacao INTEGER, " +
                "_usuario INTEGER, " +
                "data_cadastro TIMESTAMP, " +
                "_hp INTEGER, " +
                "_concorrente INTEGER, " +
                "_motivo_migracao INTEGER)");

        db.execSQL("CREATE TABLE hp (" +
                "id INTEGER, " +
                "id_local INTEGER PRIMARY KEY, " +
                "nome TEXT, " +
                "telefone1 TEXT, " +
                "telefone2 TEXT, " +
                "telefone3 TEXT, " +
                "telefone4 TEXT, " +
                "telefone5 TEXT, " +
                "telefone6 TEXT, " +
                "telefone7 TEXT, " +
                "telefone8 TEXT, " +
                "telefone9 TEXT, " +
                "telefone10 TEXT, " +
                "observacao TEXT," +
                "data_baixa TIMESTAMP," +
                "cep TEXT," +
                "bairro TEXT," +
                "complemento TEXT," +
                "email TEXT," +
                "tipo_logradouro TEXT," +
                "logradouro TEXT," +
                "numero TEXT," +
                "latitude REAL," +
                "longitude REAL," +
                "salt TEXT," +
                "_ultimo_motivo_tabulacao INTEGER," +
                "_cidade INTEGER," +
                "_condominio INTEGER) ");

        db.execSQL("CREATE TABLE nivel (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE foto_perfil_usuario (" +
                "id INTEGER PRIMARY KEY, " +
                "login TEXT," +
                "imagem TEXT)");

        db.execSQL("CREATE TABLE usuario (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT, " +
                "login TEXT, " +
                "imei TEXT, " +
                "_equipe_venda INTEGER, " +
                "_nivel INTEGER) ");

        db.execSQL("CREATE TABLE venda_imagem_anexo (" +
                "id INTEGER PRIMARY KEY, " +
                "_venda INTEGER, " +
                "imagem BLOB, " +
                "descricao TEXT," +
                "data_hora TEXT) ");

        db.execSQL("CREATE TABLE venda (" +
                "id INTEGER PRIMARY KEY, " +
                "data_cadastro TIMESTAMP, " +
                "data_instalacao TIMESTAMP, " +
                "fidelidade TEXT, " +
                "observacao TEXT, " +
                "oferta REAL, " +
                "perfil_combo TEXT, " +
                "tipo_cliente TEXT, " +
                "numero_parcelas_adesao INTEGER, " +
                "valor REAL, " +
                "valor_parcela_adesao REAL, " +
                "valor_total_parcela_adesao REAL," +
                "_cliente INTEGER, " +
                "_midia INTEGER, " +
                "_periodo_instalacao INTEGER, " +
                "_combinacao_produto_tipo INTEGER, " +
                "_tipo_contrato INTEGER, " +
                "_ultima_ocorrencia INTEGER, " +
                "data_ultima_alteracao TIMESTAMP, " +
                "_venda_internet INTEGER, " +
                "_venda_tv INTEGER, " +
                "_venda_fone INTEGER, " +
                "_venda_celular INTEGER, " +
                "_venda_forma_pagamento INTEGER, " +
                "numero_contrato INTEGER, " +
                "numero_proposta INTEGER, " +
                "_usuario_bloqueio INTEGER, " +
                "data_hora_bloqueio TIMESTAMP," +
                "adicionar_tv TEXT," +
                "adicionar_internet TEXT)");

        db.execSQL("CREATE TABLE cliente (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT, " +
                "cpf_cnpj TEXT, " +
                "data_emissao TIMESTAMP, " +
                "data_nascimento TIMESTAMP, " +
                "email TEXT, " +
                "emissor TEXT, " +
                "faixa_salarial REAL, " +
                "nacionalidade TEXT, " +
                "nome_mae TEXT, " +
                "nome_pai TEXT, " +
                "profissao TEXT, " +
                "rg_inscricao_estadual TEXT, " +
                "sexo TEXT, " +
                "telefone_celular TEXT, " +
                "telefone_comercial TEXT, " +
                "telefone_residencial TEXT, " +
                "responsavel_legal TEXT," +
                "procurador TEXT," +
                "_endereco INTEGER, " +
                "_endereco_cobranca INTEGER, " +
                "_escolaridade INTEGER, " +
                "_estado_civil INTEGER," +
                "cobranca TEXT)");

        db.execSQL("CREATE TABLE endereco (" +
                "id INTEGER PRIMARY KEY," +
                "apartamento TEXT, " +
                "bairro TEXT, " +
                "bloco TEXT, " +
                "cep TEXT, " +
                "complemento TEXT, " +
                "tipo_logradouro TEXT, " +
                "logradouro TEXT, " +
                "numero TEXT, " +
                "ponto_referencia TEXT, " +
                "_cidade INTEGER)");

        db.execSQL("CREATE TABLE produto_tipo (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE produto (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "_produto_pai INTEGER, " +
                "_produto_tipo INTEGER, " +
                "situacao TEXT, " +
                "sistema TEXT)");

        db.execSQL("CREATE TABLE tipo_contrato (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE periodo_instalacao (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE combinacao_produto_tipo (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE combinacao_produto_tipo_produto_tipo ( " +
                "_combinacao_produto_tipo INTEGER, " +
                "_produto_tipo INTEGER, " +
                "PRIMARY KEY(_combinacao_produto_tipo, _produto_tipo))");

        db.execSQL("CREATE TABLE tipo_contrato_combinacao_produto_tipo (" +
                "_combinacao_produto_tipo INTEGER, " +
                "_tipo_contrato INTEGER, " +
                "PRIMARY KEY (_combinacao_produto_tipo, _tipo_contrato))");

        db.execSQL("CREATE TABLE ocorrencia (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "codigo TEXT, " +
                "cor TEXT, " +
                "sequencia INTEGER, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE venda_internet (" +
                "id INTEGER PRIMARY KEY, " +
                "observacao TEXT, " +
                "taxa_instalacao REAL," +
                "valor_promocional REAL," +
                "valor_pos_promocao REAL," +
                "data_instalacao TIMESTAMP," +
                "vigencia_promocao TEXT," +
                "_concorrente_migracao INTEGER," +
                "_motivo_migracao INTEGER, " +
                "_produto INTEGER)");

        db.execSQL("CREATE TABLE venda_internet_produto_adicional (" +
                "_venda_internet INTEGER," +
                "_produto INTEGER, " +
                "PRIMARY KEY (_produto, _venda_internet))");

        db.execSQL("CREATE TABLE venda_tv (" +
                "id INTEGER PRIMARY KEY, " +
                "degustacao TEXT, " +
                "descricao_a_la_carte TEXT, " +
                "taxa_instalacao REAL," +
                "valor_promocional REAL," +
                "valor_pos_promocao REAL," +
                "data_instalacao TIMESTAMP," +
                "vigencia_promocao TEXT," +
                "_concorrente INTEGER, " +
                "_motivo_migracao INTEGER, " +
                "_produto INTEGER," +
                "adicionais TEXT)");

        db.execSQL("CREATE TABLE tipo_ponto_adicional (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE venda_tv_ponto_adicional (" +
                "id INTEGER PRIMARY KEY, " +
                "_venda_tv INTEGER, " +
                "_tipo_ponto_adicional INTEGER, " +
                "custo_ponto_adicional REAL)");

        db.execSQL("CREATE TABLE venda_tv_produto_adicional (" +
                "_produto INTEGER, " +
                "_venda_tv INTEGER," +
                "PRIMARY KEY (_produto, _venda_tv))");

        db.execSQL("CREATE TABLE venda_fone_linha (" +
                "id INTEGER PRIMARY KEY, " +
                "extensoes INTEGER, " +
                "mensalidade REAL," +
                "minuto_excedido_para_fixo REAL," +
                "habilitacao  REAL,"  +
                "_venda_fone INTEGER, " +
                "publicar_numero TEXT, " +
                "_produto INTEGER)");

        db.execSQL("CREATE TABLE operadora_telefonia (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE tecnologia_disponivel (" +
                "id INTEGER PRIMARY KEY, " +
                "situacao TEXT,"+
                "descricao TEXT,"+
                "_produto_tipo INTEGER)");

        db.execSQL("CREATE TABLE venda_fone_portabilidade (" +
                "id INTEGER PRIMARY KEY, " +
                "_operadora_portabilidade INTEGER, " +
                "telefone_portado TEXT, " +
                "_venda_fone INTEGER)");

        db.execSQL("CREATE TABLE venda_fone (" +
                "id INTEGER PRIMARY KEY, " +
                "linha_tronco TEXT, " +
                "taxa_habilitacao REAL,"+
                "valor_plano REAL,"+
                "_tecnologia_disponivel INTEGER," +
                "_concorrente INTEGER, " +
                "_motivo_migracao INTEGER, " +
                "observacao TEXT," +
                "portabilidades TEXT)");

        db.execSQL("CREATE TABLE tipo_compartilhamento (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE tamanho_chip (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT," +
                "modelo_aparelho TEXT)");

        db.execSQL("CREATE TABLE venda_celular_dependente (" +
                "id INTEGER PRIMARY KEY, " +
                "_venda_celular INTEGER, " +
                "_operadora_telefonia INTEGER, " +
                "telefone TEXT, " +
                "_tamanho_chip INTEGER," +
                "_tipo_compartilhamento INTEGER)");

        db.execSQL("CREATE TABLE venda_celular (" +
                "id INTEGER PRIMARY KEY, " +
                "_operadora_portabilidade INTEGER, " +
                "telefone TEXT, " +
                "_tamanho_chip INTEGER," +
                "_produto INTEGER," +
                "_concorrente INTEGER, " +
                "_motivo_migracao INTEGER, " +
                "dependentes TEXT)");

        db.execSQL("CREATE TABLE venda_forma_pagamento_banco (" +
                "id INTEGER PRIMARY KEY, " +
                "vencimento INTEGER, " +
                "_banco INTEGER, " +
                "agencia TEXT, " +
                "conta TEXT, " +
                "nome_titular TEXT)");

        db.execSQL("CREATE TABLE venda_forma_pagamento (" +
                "id INTEGER PRIMARY KEY, " +
                "cpf_na_nota TEXT, " +
                "fatura_somente_por_email TEXT, " +
                "informou_sobre_multa TEXT, " +
                "receber_campanha_publicitaria TEXT, " +
                "_vencimento_fatura INTEGER, " +
                "_venda_forma_pagamento_banco INTEGER)");

        db.execSQL("CREATE TABLE venda_interacao_detalhamento (" +
                "_venda_interacao INTEGER, " +
                "_detalhamento INTEGER, " +
                " PRIMARY KEY (_venda_interacao, _detalhamento))");

        db.execSQL("CREATE TABLE venda_interacao (" +
                "id INTEGER PRIMARY KEY, " +
                "_usuario INTEGER, " +
                "_venda INTEGER, " +
                "data_interacao TIMESTAMP)");

        db.execSQL("CREATE TABLE detalhamento (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "codigo TEXT, " +
                "_ocorrencia INTEGER," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE venda_motivo_nao_venda (" +
                "_venda INTEGER, " +
                "_motivo_nao_venda INTEGER, " +
                "_concorrente_migracao INTEGER," +
                "_motivo_migracao INTEGER," +
                " PRIMARY KEY (_venda, _motivo_nao_venda))");

        db.execSQL("CREATE TABLE bandeira_sistema (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "icone TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE bandeira_sistema_produto (" +
                "_bandeira_sistema INTEGER, " +
                "_produto INTEGER, " +
                " PRIMARY KEY (_bandeira_sistema, _produto))");

        db.execSQL("CREATE TABLE tabulacao_agendamento (" +
                "id INTEGER, " +
                "id_local INTEGER PRIMARY KEY, " +
                "_tabulacao INTEGER, " +
                "data_retorno TEXT, " +
                "tipo_agendamento TEXT, " +
                "trabalhado TEXT, " +
                "alertado TEXT) ");

        db.execSQL("CREATE TABLE log_acesso (" +
                "id INTEGER PRIMARY KEY, " +
                "acao_login TEXT, " +
                "login TEXT, " +
                "id_agente_autorizado INTEGER, " +
                "id_usuario INTEGER, " +
                "imei TEXT, " +
                "data_login TEXT, " +
                "hora_login TEXT) ");

        db.execSQL("CREATE TABLE log_diario (" +
                "id INTEGER PRIMARY KEY, " +
                "login TEXT, " +
                "data_log TEXT, " +
                "vendas INTEGER, " +
                "nao_vendas INTEGER, " +
                "agendamentos INTEGER) ");

        db.execSQL(" CREATE TABLE condominio (" +
                "id_local INTEGER PRIMARY KEY, " +
                "  id INTEGER, " +
                "  area_telefonica TEXT, " +
                "  capacidade_equipamento  INTEGER, " +
                "  chave_cep TEXT, " +
                "  chave_endereco TEXT, " +
                "  classe_social TEXT, " +
                "  cnl TEXT, " +
                "  cod_fttx TEXT, " +
                "  cod_log TEXT, " +
                "  fibra_livre  INTEGER, " +
                "  idade_edificio  INTEGER, " +
                "  latitude DOUBLE, " +
                "  layout TEXT, " +
                "  longitude DOUBLE, " +
                "  data_concl_infra DATE, "+
                "  metragem_apartamento DOUBLE, " +
                "  nome TEXT, " +
                "  observacao TEXT, " +
                "  ocupacao_equipamento  INTEGER, " +
                "  percentual_ocupacao DOUBLE, " +
                "  prioridade TEXT, " +
                "  quantidade_andares_bloco  INTEGER, " +
                "  quantidade_apartamento TEXT, " +
                "  quantidade_apartamento_andar  INTEGER, " +
                "  quantidade_bloco  INTEGER, " +
                "  quantidade_dormitorio_apartamento  INTEGER, " +
                "  segmentacao_mkt TEXT, " +
                "  segmento TEXT, " +
                "  situacao TEXT, " +
                "  telefone_portaria TEXT, " +
                "  tipo_condominio TEXT, " +
                "  _condominio_operacao_comercial  INTEGER, " +
                "  _condominio_status_infra  INTEGER, " +
                "  _endereco integer NOT NULL)");

        db.execSQL("CREATE TABLE condominio_contato (" +
                "id INTEGER, " +
                "  data_nascimento DATE, " +
                "  nome TEXT, " +
                "  numero_apartamento TEXT, " +
                "  telefone_celular TEXT, " +
                "  telefone_comercial TEXT, " +
                "  telefone_residencial TEXT, " +
                "  telefone_portaria TEXT, " +
                "  _condominio INTEGER, " +
                "  _condominio_contato_cargo INTEGER," +
                "  cortesia TEXT," +
                "  data_limite_mandato DATE," +
                "  clube TEXT)");

        db.execSQL("CREATE TABLE condominio_contato_cargo (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE condominio_operacao_comercial (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE condominio_plantao_situacao (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE condominio_status_infra (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE condominio_plantao (" +
                "id_local INTEGER PRIMARY KEY, " +
                "  id INTEGER, " +
                "data_inicio DATE, " +
                "_usuario INTEGER, " +
                "data_final DATE, " +
                "_condominio integer, " +
                "_condominio_plantao_situacao integer) ");

        db.execSQL("CREATE TABLE status_sincronizacao (" +
                "id INTEGER PRIMARY KEY, " +
                "motivo_tabulacao_tipo TEXT, " +
                "em_sincronizacao TEXT) ");

        db.execSQL("CREATE TABLE banco (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE vencimento_fatura (" +
                "vencimento INTEGER PRIMARY KEY, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE imagem_usuario (" +
                "id INTEGER PRIMARY KEY, " +
                "login TEXT, " +
                "imagem TEXT)");

        db.execSQL("CREATE TABLE atualizacao (" +
                "id INTEGER PRIMARY KEY, " +
                "dataHora TEXT, " +
                "login TEXT)");

        db.execSQL("CREATE TABLE indicador_ocorrencia (" +
                "id_ocorrencia INTEGER PRIMARY KEY, " +
                "descricao_ocorrencia TEXT, " +
                "quantidade_vendas INTEGER, " +
                "cor_ocorrencia TEXT, " +
                "sequencia_ocorrencia INTEGER)");

        db.execSQL("CREATE TABLE indicador_venda (" +
                "id INTEGER PRIMARY KEY, " +
                "_indicador_ocorrencia INTEGER, " +
                "nome_cliente TEXT, " +
                "data_cadastro INTEGER, " +
                "descricao_combinacao_produto_tipo TEXT)");

        db.execSQL("CREATE TABLE configuracao_sistema (" +
                "descricao TEXT PRIMARY KEY, " +
                "valor TEXT)");

        db.execSQL("CREATE TABLE mailing (" +
                "id INTEGER PRIMARY KEY)");

        db.execSQL("CREATE TABLE hp_mailing (" +
                "id_local INTEGER PRIMARY KEY, " +
                "nome_cidade TEXT, " +
                "campaign_cd TEXT, " +
                "resptracking_cd TEXT, " +
                "id_domicilio TEXT, " +
                "cd_operadora TEXT, " +
                "cd_net TEXT, " +
                "nr_dbm TEXT, " +
                "nr_dbm_hh TEXT, " +
                "cpf_cnpj TEXT, " +
                "node TEXT, " +
                "tipo_logradouro TEXT, " +
                "produto TEXT, " +
                "classe_social TEXT, " +
                "propensao TEXT, " +
                "segmento TEXT, " +
                "coringa_1 TEXT, " +
                "coringa_2 TEXT, " +
                "coringa_3 TEXT, " +
                "coringa_4 TEXT, " +
                "coringa_5 TEXT, " +
                "coringa_6 TEXT, " +
                "coringa_7 TEXT, " +
                "coringa_8 TEXT, " +
                "coringa_9 TEXT, " +
                "coringa_10 TEXT, " +
                "_mailing INTEGER, " +
                "_hp INTEGER )");

        db.execSQL("CREATE TABLE previsao_tempo (" +
                "id INTEGER PRIMARY KEY, " +
                " cidade TEXT, " +
                " estado TEXT, " +
                " temperatura DOUBLE, " +
                " minima DOUBLE, " +
                " maxima DOUBLE, " +
                " idIcone TEXT, " +
                " nome_icone TEXT, " +
                " data DATE) ");

        db.execSQL("CREATE TABLE mailing_rua (" +
                "id INTEGER PRIMARY KEY, " +
                " _mailing INTEGER, " +
                " _cidade INTEGER, " +
                " tipo_logradouro TEXT, " +
                " logradouro TEXT, " +
                " bairro TEXT, " +
                " cep TEXT, " +
                " numero_inicio INTEGER, " +
                " numero_fim INTEGER, " +
                " area_fibra TEXT, " +
                " potencial TEXT, " +
                " status TEXT) ");

        db.execSQL("CREATE TABLE equipe_venda (" +
                "id INTEGER PRIMARY KEY, " +
                " descricao TEXT, " +
                " situacao TEXT) ");

        db.execSQL("CREATE TABLE usuario_configuracao_localizacao (" +
                "id INTEGER PRIMARY KEY, " +
                " localizacao_precisao INTEGER, " +
                " localizacao_intervalo INTEGER, " +
                " envio_intervalo INTEGER, " +
                " rastreamento TEXT) ");

        db.execSQL("CREATE TABLE posicionamento (" +
                "imei TEXT , " +
                " data_cadastro_mobile TEXT , " +
                " _usuario INTEGER, " +
                " latitude DOUBLE, " +
                " longitude DOUBLE, " +
                " data_cadastro_servidor TEXT) ");

        db.execSQL("CREATE TABLE mobile_permissao (" +
                "id INTEGER PRIMARY KEY, " +
                " descricao TEXT , " +
                " icone TEXT , " +
                " label TEXT, " +
                " segue TEXT , " +
                " activity TEXT, " +
                " sequencia INTEGER, " +
                " situacao TEXT, " +
                " lado_menu TEXT) ");

        db.execSQL("CREATE TABLE agente_autorizado (" +
                "id INTEGER PRIMARY KEY, " +
                " razao_social TEXT , " +
                " nome_fantasia TEXT , " +
                " cnpj TEXT, " +
                " data_ultima_situacao_historico DATE , " +
                " data_ultima_tabulacao DATE, " +
                " _ultima_situacao_detalhamento INTEGER, " +
                " tabulacao_simplificada TEXT, " +
                " telefone TEXT, " +
                " telefone2 TEXT, " +
                " telefone3 TEXT) ");

        db.execSQL("CREATE TABLE agente_autorizado_situacao (" +
                "id INTEGER PRIMARY KEY, " +
                " descricao TEXT, " +
                " ativo TEXT) " );

        db.execSQL("CREATE TABLE agente_autorizado_situacao_detalhamento (" +
                "id INTEGER PRIMARY KEY, " +
                " descricao TEXT, " +
                " _agente_autorizado_situacao INTEGER) " );

        db.execSQL("CREATE TABLE tabulacao_contador (" +
                "id INTEGER PRIMARY KEY, " +
                " motivo_tabulacao_tipo TEXT, " +
                " quantidade_atual DOUBLE, " +
                " quantidade_acumulada DOUBLE, " +
                " _agente_autorizado INTEGER, " +
                " icone TEXT ) " );

        db.execSQL("CREATE TABLE chamado (" +
                "id INTEGER PRIMARY KEY, " +
                " protocolo INTEGER, " +
                " _agente_autorizado INTEGER, " +
                " _chamado_situacao INTEGER, " +
                " _responsavel INTEGER, " +
                " _solicitante INTEGER, " +
                " _nivel_responsavel INTEGER, " +
                " data_cadastro DATE, " +
                " ultima_alteracao DATE, " +
                " _chamado_interacao_chamado INTEGER, " +
                " _chamado_interacao_data_interacao INTEGER, " +
                " nota_atendimento INTEGER) " );

        db.execSQL("CREATE TABLE chamado_situacao (" +
                "id INTEGER PRIMARY KEY, " +
                " descricao TEXT, " +
                " icone TEXT, " +
                " situacao TEXT, " +
                " aberto TEXT, " +
                " cor TEXT) " );

        db.execSQL("CREATE TABLE chamado_interacao (" +
                " _chamado INTEGER, " +
                " data_interacao DATE, " +
                " _responsavel INTEGER, " +
                " observacao TEXT, " +
                " fechado TEXT) " );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
