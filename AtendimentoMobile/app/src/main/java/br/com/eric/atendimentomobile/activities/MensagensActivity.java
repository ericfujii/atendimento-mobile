package br.com.eric.atendimentomobile.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EBoolean;
import br.com.eric.atendimentomobile.entidade.Mensagem;
import br.com.eric.atendimentomobile.entidade.Usuario;
import br.com.eric.atendimentomobile.utils.DataBaseException;
import br.com.eric.atendimentomobile.utils.EntityManager;

public class MensagensActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private BroadcastReceiver mReceiver;
    private ListView listView;
    private EntityManager entityManager;
    private List<Usuario> usuarios;
    private Map<Usuario, List<Mensagem>> mensagens;
    private AtendimentoMobile atendimentoMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagens);
        atendimentoMobile = (AtendimentoMobile)getApplication();
        entityManager = new EntityManager(this);

        atualizarMensagens();
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

    public void atualizarMensagens() {
        try {
            List<Mensagem> mensagensNaoLidas = entityManager.getByWhere(Mensagem.class, "visualizada == 'FALSE'", "_remetente");
            usuarios = entityManager.getByWhere(Usuario.class, "id != " + atendimentoMobile.getIdUsuario(), "nome ASC");
            mensagens = new HashMap<>();
            for (Usuario usuario : usuarios) {
                List<Mensagem> mensagensUsuario = new ArrayList<>();
                for (Mensagem mensagem : mensagensNaoLidas) {
                    if (mensagem.getRemetente().getId().equals(usuario.getId())) {
                        entityManager.initialize(mensagem.getRemetente());
                        entityManager.initialize(mensagem.getDestinatario());
                        mensagensUsuario.add(mensagem);
                    }
                }
                if (mensagensUsuario.size() == 0) {
                    List<Mensagem> mensagensBase = entityManager.getByWhere(Mensagem.class, "_remetente = " + usuario.getId() + " OR (_remetente = " + atendimentoMobile.getIdUsuario() +" AND _destinatario = " + usuario.getId() + ")", "data_mensagem DESC");
                    if (mensagensBase.size() > 0) {
                        entityManager.initialize(mensagensBase.get(0).getRemetente());
                        entityManager.initialize(mensagensBase.get(0).getDestinatario());
                        mensagensUsuario.add(mensagensBase.get(0));
                    }
                }
                mensagens.put(usuario, mensagensUsuario);
            }

            MensagemListAdapter adapter = new MensagemListAdapter(this, usuarios, mensagens, atendimentoMobile.getIdUsuario());
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
        } catch (DataBaseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, MensagemUsuarioActivity.class);
        intent.putExtra("idUsuario", usuarios.get(position).getId());
        for (Mensagem mensagem : mensagens.get(usuarios.get(position))) {
            mensagem.setVisualizada(EBoolean.TRUE);
            try {
                entityManager.atualizar(mensagem);
            } catch (DataBaseException e) {
                e.printStackTrace();
            }
        }
        startActivity(intent);
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

    @Override
    protected void onResume() {
        atualizarMensagens();
        super.onResume();
    }
}
