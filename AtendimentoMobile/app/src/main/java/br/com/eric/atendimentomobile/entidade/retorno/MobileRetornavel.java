package br.com.eric.atendimentomobile.entidade.retorno;

import java.util.Map;

/**
 * Created by marceloeugenio on 3/16/16.
 */
public interface MobileRetornavel {

    String getMensagem();

    String getCodigoRetorno();

    Map<String, Object> getExtras();
}
