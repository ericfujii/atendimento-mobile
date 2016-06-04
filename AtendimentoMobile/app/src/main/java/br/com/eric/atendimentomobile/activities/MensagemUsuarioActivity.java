package br.com.eric.atendimentomobile.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;
import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.ConfiguracaoSistema;
import br.com.eric.atendimentomobile.entidade.EBoolean;
import br.com.eric.atendimentomobile.entidade.ED2DCodigoResponse;
import br.com.eric.atendimentomobile.entidade.Mensagem;
import br.com.eric.atendimentomobile.entidade.Usuario;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioMensagem;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioVerificarMensagens;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoLogin;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoMensagem;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoVerificarMensagens;
import br.com.eric.atendimentomobile.servico.MobileEnvioServico;
import br.com.eric.atendimentomobile.utils.DataBaseException;
import br.com.eric.atendimentomobile.utils.EntityManager;
import br.com.eric.atendimentomobile.utils.UtilActivity;

public class MensagemUsuarioActivity extends ActionBarActivity {

    private BroadcastReceiver mReceiver;
    private ListView listView;
    private EntityManager entityManager;
    private EditText mensagemET;
    private List<Mensagem> mensagens;
    private Usuario destinatario;
    private Usuario remetente;
    private AtendimentoMobile atendimentoMobile;
    private Mensagem mensagem;
    private MobileEnvioServico mobileEnvioServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem_usuario);
        entityManager = new EntityManager(this);
        atendimentoMobile = (AtendimentoMobile) getApplication();
        mobileEnvioServico = new MobileEnvioServico(MobileRetornoMensagem.class);
        listView = (ListView) findViewById(R.id.listView);
        mensagemET = (EditText) findViewById(R.id.mensagemET);
        Integer idUsuario = getIntent().getExtras().getInt("idUsuario", 0);
        try {
            destinatario = entityManager.getById(Usuario.class, idUsuario);
            remetente = entityManager.getById(Usuario.class, atendimentoMobile.getIdUsuario());
            setTitle(destinatario.getNome());
            atualizarMensagens();
        } catch (DataBaseException e) {
            e.printStackTrace();
        }
        IntentFilter intentFilter = new IntentFilter("NOVA_MENSAGEM");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                atualizarMensagens();
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void atualizarMensagens() {
        try {
            mensagens = entityManager.getByWhere(Mensagem.class, "_remetente = " + destinatario.getId() + " OR _destinatario = " + destinatario.getId(), "data_mensagem ASC");
            for (Mensagem mensagem : mensagens) {
                entityManager.initialize(mensagem.getRemetente());
                entityManager.initialize(mensagem.getDestinatario());
                if (mensagem.getVisualizada().equals(EBoolean.FALSE)) {
                    mensagem.setVisualizada(EBoolean.TRUE);
                    entityManager.atualizar(mensagem);
                }
            }
            MensagemUsuarioListAdapter adapter = new MensagemUsuarioListAdapter(this, mensagens);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
            scrollMyListViewToBottom();
        } catch (DataBaseException e) {
            e.printStackTrace();
        }
    }

    private void scrollMyListViewToBottom() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                listView.setSelection(mensagens.size() - 1);
            }
        });
    }

    public void enviarMensagem(View view) {
        if (!mensagemET.getText().toString().trim().equals("")) {
            final String mensagemTexto = mensagemET.getText().toString();
            AsyncTask<Void, Void, MobileRetorno> taskMensagens = new AsyncTask<Void, Void, MobileRetorno>() {
                @Override
                protected MobileRetorno doInBackground(Void... params) {
                    MobileRetorno mobileRetorno = null;
                    try {
                        mensagem = new Mensagem();
                        mensagem.setDestinatario(destinatario);
                        mensagem.setMensagem(mensagemTexto);
                        mensagem.setRemetente(remetente);
                        MobileEnvioMensagem mobileEnvioMensagem = new MobileEnvioMensagem(atendimentoMobile, mensagem);

                        mobileRetorno = mobileEnvioServico.send(mobileEnvioMensagem);
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
                            MobileRetornoMensagem mobileRetornoMensagem = (MobileRetornoMensagem) resposta;
                            mensagem.setId(mobileRetornoMensagem.getIdMensagem());
                            mensagem.setDataMensagem(mobileRetornoMensagem.getHoraMensagem());
                            mensagem.setVisualizada(EBoolean.TRUE);
                            entityManager.save(mensagem);
                            atualizarMensagens();
                            mensagemET.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            taskMensagens.execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            this.finish();
        }
        return true;
    }
}
