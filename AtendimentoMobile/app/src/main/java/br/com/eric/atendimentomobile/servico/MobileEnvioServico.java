package br.com.eric.atendimentomobile.servico;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import br.com.eric.atendimentomobile.entidade.EMobileRecursoCodigo;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvio;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornavel;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.utils.JsonParser;

/**
 * Created by marceloeugenio on 3/16/16.
 */
public class MobileEnvioServico {
    public static String URL = "http://192.168.1.200:8080/AtendimentoWeb/rest/atendimentoRest"; // Eric

    private Class<? extends MobileRetornavel> classeRetorno;

    public MobileEnvioServico(Class<? extends MobileRetornavel> classeRetorno) {
        this.classeRetorno = classeRetorno;
    }

    public MobileRetorno send(MobileEnvio mobileEnvio) throws Exception{
        try {
            JsonParser jsonParser = new JsonParser();
            JSONObject jsonEnvio   = jsonParser.parseObjectToJson(mobileEnvio);
            JSONObject jsonRetorno = sendPost(jsonEnvio, mobileEnvio.getMobileRecursoCodigo());
            return (MobileRetorno) jsonParser.parseJsonToObject(jsonRetorno, classeRetorno);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private JSONObject sendPost(JSONObject jsonObject, EMobileRecursoCodigo recurso) throws Exception {
        try {
            JSONObject jsonReturn;
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
            defaultHttpClient.getParams().setIntParameter("http.socket.timeout", 30000);
            defaultHttpClient.getParams().setIntParameter("http.connection.timeout", 30000);
            HttpPost httpPost = new HttpPost(URL + "/" + recurso.getCodigo());
            StringEntity stringEntity = new StringEntity(jsonObject.toString(), "UTF-8");

            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                String stringReturn = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                jsonReturn = new JSONObject(stringReturn);
                Log.d("JSON", "JSON RETORNO: " + stringReturn);
            } else {
                throw new Exception("Falha na requisição: " + httpResponse.getStatusLine().getStatusCode());
            }

            return jsonReturn;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
