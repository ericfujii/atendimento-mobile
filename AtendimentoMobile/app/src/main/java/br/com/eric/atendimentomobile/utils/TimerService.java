package br.com.eric.atendimentomobile.utils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;

import java.text.SimpleDateFormat;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.activities.MainActivity;
import br.com.eric.atendimentomobile.activities.MensagensActivity;
import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.ConfiguracaoSistema;
import br.com.eric.atendimentomobile.entidade.ED2DCodigoResponse;
import br.com.eric.atendimentomobile.entidade.Mensagem;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioVerificarMensagens;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoLogin;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoVerificarMensagens;
import br.com.eric.atendimentomobile.servico.MobileEnvioServico;

/**
 * Created by eric on 25-05-2015.
 */
public class TimerService extends Service {

    private Handler mHandler;
    private EntityManager entityManager;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatoDataHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private MobileEnvioServico mobileEnvioServico;

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            verificarMensagens();
            mHandler.postDelayed(mStatusChecker, 10000);
        }
    };

    private void verificarMensagens() {
        AsyncTask<Void, Void, MobileRetorno> taskMensagens = new AsyncTask<Void, Void, MobileRetorno>() {
            @Override
            protected MobileRetorno doInBackground(Void... params) {
                MobileRetorno mobileRetorno = null;
                try {
                    AtendimentoMobile atendimentoMobile = new AtendimentoMobile();
                    atendimentoMobile.setIdUsuario(Integer.parseInt(entityManager.getById(ConfiguracaoSistema.class, "usuario").getValor()));
                    MobileEnvioVerificarMensagens mobileEnvioVerificarMensagens = new MobileEnvioVerificarMensagens(atendimentoMobile);

                    mobileRetorno = mobileEnvioServico.send(mobileEnvioVerificarMensagens);
                } catch (Exception e) {
                    e.printStackTrace();
                    mobileRetorno = new MobileRetornoLogin();
                    return mobileRetorno;
                }
                return mobileRetorno;
            }

            @Override
            protected void onPostExecute(MobileRetorno resposta) {
                super.onPostExecute(resposta);
                if (resposta.getCodigoRetorno() != null && resposta.getCodigoRetorno().equals(ED2DCodigoResponse.OK.toString())) {
                    try {
                        MobileRetornoVerificarMensagens mobileRetornoVerificarMensagens = (MobileRetornoVerificarMensagens) resposta;
                        if (mobileRetornoVerificarMensagens.getMensagens() != null && mobileRetornoVerificarMensagens.getMensagens().size() > 0) {
                            UtilActivity.makeNotification(TimerService.this,
                                    1,
                                    R.drawable.onigiri,
                                    "Atendimento",
                                    "Nova mensagem",
                                    "Nova mensagem",
                                    new Intent(TimerService.this, MensagensActivity.class),
                                    false);
                            UtilActivity.vibrate(TimerService.this, 500);
                            Intent intent = new Intent("NOVA_MENSAGEM");
                            sendBroadcast(intent);
                        }
                        for (Mensagem mensagem : mobileRetornoVerificarMensagens.getMensagens()) {
                            try {
                                entityManager.getById(Mensagem.class, mensagem.getId());
                            } catch (Exception e) {
                                entityManager.save(mensagem);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        taskMensagens.execute();
    }

    public TimerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        entityManager = new EntityManager(this);
        mobileEnvioServico = new MobileEnvioServico(MobileRetornoVerificarMensagens.class);

        mHandler = new Handler();
        mStatusChecker.run();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        startService(new Intent(this, TimerService.class));
        super.onDestroy();
    }
}
