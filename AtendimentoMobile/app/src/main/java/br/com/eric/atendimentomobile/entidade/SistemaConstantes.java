package br.com.eric.atendimentomobile.entidade;

public final class SistemaConstantes {

	public static final int ZERO = 0;
	public static final int UM = 1;
	public static final int DOIS = 2;
	public static final int TRES = 3;
	public static final int QUATRO = 4;
	public static final int CINCO = 5;
	public static final int SEIS = 6;
	public static final int SETE = 7;
	public static final int OITO = 8;
	public static final int NOVE = 9;
	public static final int DEZ = 10;
	public static final int ONZE = 11;
	public static final int DOZE = 12;
	public static final int TREZE = 13;
	public static final int QUATORZE = 14;
	public static final int QUINZE = 15;
	public static final int DEZESSEIS = 16;
	public static final int DEZESSETE = 17;
	public static final int DEZOITO = 18;
	public static final int DEZENOVE = 19;
	public static final int VINTE = 20;
	public static final int VINTE_UM = 21;
	public static final int VINTE_DOIS = 22;
	public static final int VINTE_TRES = 23;
	public static final int VINTE_QUATRO = 24;
	public static final int VINTE_CINCO = 25;
	public static final int VINTE_SEIS = 26;
	public static final int VINTE_SETE = 27;
	public static final int VINTE_OITO = 28;
	public static final int VINTE_NOVE = 29;
	public static final int TRINTA = 30;
	public static final int TRINTA_UM = 31;
	public static final int TRINTA_DOIS = 32;
	public static final int TRINTA_TRES = 33;
	public static final int TRINTA_QUATRO = 34;
	public static final int TRINTA_CINCO = 35;
	public static final int TRINTA_SEIS = 36;
	public static final int TRINTA_SETE = 37;
	public static final int TRINTA_OITO = 38;
	public static final int TRINTA_NOVE = 39;
	public static final int QUARENTA = 40;
	public static final int QUARENTA_UM = 41;
	public static final int CINQUENTA = 50;
	public static final int CINQUENTA_NOVE = 59;
	public static final int SETENTA = 70;
	public static final int OITENTA = 80;
	public static final int OITENTA_OITO = 88;
	public static final int OITENTA_NOVE = 89;
	public static final int NOVENTA_UM = 91;
	public static final int CEM = 100;
	
	public static final int CENTO_CINQUENTA = 150;
	
	public static final long TEMPO_MAXIMO_DURACAO_LIGACAO = 180;

	public static final int TELEFONE = 11;
	public static final int CPF_CNPJ = 14;
	public static final int CEP = 8;
	public static final int DESCRICAO = 100;
	public static final int EMAIL = 100;
	
	public static final int TREZENTOSOITENTASETE = 387;
	public static final int MIL = 1000;
	public static final int MILHAO = 10000;

	public static final int CINCO_SEGUNDOS = 5;
	public static final int SEIS_SEGUNDOS = 6;
	public static final int DEZ_SEGUNDOS = 10;
	public static final int VINTE_SEGUNDOS = 20;
	public static final int TRINTA_SEGUNDOS = 30;
	public static final int VINTE_CINCO_SEGUNDOS = 25;
	public static final int SESSENTA_SEGUNDOS = 60;
	public static final int SESSENTA_UM = 61;
	public static final int SESSENTA_QUATRO = 64;
	public static final int DUZENTOS_CINQUENTA = 250;
	public static final int TREZENTOS_CINQUENTA_UM = 351;
	public static final int HORAMILISSEGUNDOS = 3600000;
	public static final int MINUTOMILISSEGUNDOS = 60000;
	
	public static final String E_BOOLEAN_TRUE = "varchar(10) default 'TRUE'";
	public static final String E_BOOLEAN_FALSE = "varchar(10) default 'FALSE'";
	
	public static final String E_SITUACAO_ATIVO = "varchar(10) default 'ATIVO'";
	public static final String E_SITUACAO_INATIVO = "varchar(10) default 'INATIVO'";
	
	public static final String E_SISTEMA_AMBOS = "varchar(20) default 'AMBOS'";
	
	public static final String E_TIPO_PERGUNTA_SUBJETIVA = "varchar(20) default 'SUBJETIVA'";
	
	public static final String E_INFORMACAO_TIPO_LINK = "varchar(20) default 'CONVENCIONAL'";
	
	public static final double MARGEM_PONTO_FLUTUANTE = 0.00001;
	
	/**Tamanho do campo data (dd/MM/yyyy)
	 * considerando a máscara
	 */
	public static final Integer DATA_COM_MASCARA = 10;
	
	/**Tamanho do campo moeda (precision 17, scale 2)
	 * considerando os pontos e vírgula da máscara
	 */
	public static final Integer MOEDA_COM_MASCARA = 17;
	
	/**Tamanho do campo cep (#####-###)
	 * considerando o separador da máscara
	 */
	public static final Integer CEP_COM_MASCARA = 9;
	public static final Integer NOVENTA = 90;
	public static final Integer CENTO_DEZ = 110;
	public static final Integer CENTO_VINTE = 120;
	public static final Integer CENTO_TRINTA = 130;
	
	public static final String DATA_HORA_PADRAO_BRASIL_SEM_SEGUNDOS = "dd/MM/yyyy HH:mm";

	public static final Integer EARTH_RADIUS_KM = 6371;
	
	private SistemaConstantes() {

	}
}