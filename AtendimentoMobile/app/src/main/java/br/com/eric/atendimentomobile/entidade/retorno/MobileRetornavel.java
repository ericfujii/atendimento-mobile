package br.com.eric.atendimentomobile.entidade.retorno;

import java.util.Map;

public interface MobileRetornavel {

    String getMensagem();

    String getCodigoRetorno();

    Map<String, Object> getExtras();
}
