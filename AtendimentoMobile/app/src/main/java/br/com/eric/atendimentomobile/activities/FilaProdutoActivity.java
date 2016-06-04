package br.com.eric.atendimentomobile.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.EAcaoPedido;
import br.com.eric.atendimentomobile.entidade.ED2DCodigoResponse;
import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.entidade.Produto;
import br.com.eric.atendimentomobile.entidade.ProdutoTipo;
import br.com.eric.atendimentomobile.entidade.SistemaConstantes;
import br.com.eric.atendimentomobile.entidade.Usuario;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioAlterarCancelarPedido;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioFila;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioPacote;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoFila;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoLogin;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoPacote;
import br.com.eric.atendimentomobile.servico.MobileEnvioServico;
import br.com.eric.atendimentomobile.utils.EntityManager;
import br.com.eric.atendimentomobile.utils.UtilActivity;

public class FilaProdutoActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private AtendimentoMobile atendimentoMobile;
    private Integer idProduto;
    private MobileEnvioServico mobileEnvioServico;
    private EntityManager entityManager;
    private List<ItemPedido> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fila_produto);
        atendimentoMobile = (AtendimentoMobile)getApplication();
        mobileEnvioServico = new MobileEnvioServico(MobileRetornoFila.class);
        listView = (ListView) findViewById(R.id.listView);
        entityManager = new EntityManager(this);
        idProduto = getIntent().getIntExtra("idProduto", SistemaConstantes.ZERO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            Produto produto = entityManager.getById(Produto.class, idProduto);
            setTitle("Fila - " + produto.getNome());
            popularFila();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popularFila() throws Exception {

        AsyncTask<Void, Void, MobileRetorno> taskFila = new AsyncTask<Void, Void, MobileRetorno>() {
            @Override
            protected MobileRetorno doInBackground(Void... params) {
                MobileRetorno mobileRetorno = null;
                try {
                    MobileEnvioFila mobileEnvioFila = new MobileEnvioFila(atendimentoMobile, new Produto(idProduto));
                    mobileRetorno = mobileEnvioServico.send(mobileEnvioFila);
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
                        MobileRetornoFila mobileRetornoFila = (MobileRetornoFila) resposta;
                        pedidos = mobileRetornoFila.getItensPedidos();
                        FilaProdutoListAdapter adapter = new FilaProdutoListAdapter(FilaProdutoActivity.this, pedidos);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(FilaProdutoActivity.this);
                    } catch (Exception e) {
                        UtilActivity.makeShortToast("Erro ao carregar fila.", getApplicationContext());
                    }
                } else {
                    UtilActivity.makeShortToast("Erro ao carregar fila.", getApplicationContext());
                }
            }
        };
        taskFila.execute();
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
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FilaProdutoActivity.this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                AsyncTask<Void, Void, MobileRetorno> taskFila = new AsyncTask<Void, Void, MobileRetorno>() {
                    @Override
                    protected MobileRetorno doInBackground(Void... params) {
                        MobileRetorno mobileRetorno = null;
                        try {
                            MobileEnvioAlterarCancelarPedido mobileEnvioFila =
                                    new MobileEnvioAlterarCancelarPedido(atendimentoMobile, pedidos.get(position), EAcaoPedido.CANCELAR);
                            mobileRetorno = mobileEnvioServico.send(mobileEnvioFila);
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
                            UtilActivity.makeShortToast("Pedido cancelado.", getApplicationContext());
                            try {
                                popularFila();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            UtilActivity.makeShortToast("Erro ao cancelar.", getApplicationContext());
                        }
                    }
                };
                taskFila.execute();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setMessage("Deseja cancelar este pedido?");
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
