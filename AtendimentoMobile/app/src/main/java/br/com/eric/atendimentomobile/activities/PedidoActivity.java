package br.com.eric.atendimentomobile.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.ED2DCodigoResponse;
import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.entidade.Pedido;
import br.com.eric.atendimentomobile.entidade.Produto;
import br.com.eric.atendimentomobile.entidade.ProdutoTipo;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioPedido;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoPedido;
import br.com.eric.atendimentomobile.servico.MobileEnvioServico;
import br.com.eric.atendimentomobile.utils.EntityManager;
import br.com.eric.atendimentomobile.utils.SlidingTabLayout;
import br.com.eric.atendimentomobile.utils.UtilActivity;

public class PedidoActivity extends ActionBarActivity {

    private ViewPager pager;
    private TabAdapter adapter;
    private SlidingTabLayout tabs;
    private List<String> titles;
    private List<Fragment> fragments;
    private EntityManager entityManager;
    private EditText mesaET;
    private ProgressDialog progressDialog;
    private AtendimentoMobile atendimentoMobile;
    private List<ItemPedido> pedidosEnviar;
    private String mesa;

    private MobileEnvioServico mobileEnvioServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        entityManager = new EntityManager(this);
        atendimentoMobile = (AtendimentoMobile) getApplication();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mesaET = (EditText) findViewById(R.id.mesaET) ;
        mobileEnvioServico = new MobileEnvioServico(MobileRetornoPedido.class);

        atualizarAbas();
    }

    public void atualizarAbas() {
        try {
            fragments = new ArrayList<>();
            titles = new ArrayList<>();

            List<ProdutoTipo> produtoTipos = entityManager.getByWhere(ProdutoTipo.class, "situacao = 'ATIVO'", "ordem ASC");
            for (ProdutoTipo produtoTipo : produtoTipos) {
                PedidoFragment pedidoFragment = new PedidoFragment();
                pedidoFragment.setProdutoTipo(produtoTipo);
                titles.add(produtoTipo.getNome());
                fragments.add(pedidoFragment);
            }

            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter = new TabAdapter(getSupportFragmentManager(), titles, fragments);

            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.pager);
            pager.setOffscreenPageLimit(titles.size());
            pager.setAdapter(adapter);

            // Assiging the Sliding Tab Layout View
            tabs = (SlidingTabLayout) findViewById(R.id.tabs);
            tabs.setDistributeEvenly(false); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

            // Setting Custom Color for the Scroll bar indicator of the Tab View
            tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.cor_destaque);
                }
            });

            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pedido, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            this.finish();
        }

        if (id == R.id.action_salvar_pedido) {

            if (mesaET.getText().toString().trim().equals("")) {
                UtilActivity.makeShortToast("Informe a mesa", this);
                return false;
            } else {
                mesa = mesaET.getText().toString();
            }

            pedidosEnviar = new ArrayList<>();

            for (Fragment fragment : fragments) {
                PedidoFragment pedidoFragment = (PedidoFragment) fragment;
                for (ItemPedido itemPedido : pedidoFragment.getItensPedido()) {
                    if (itemPedido.getQuantidadeMesa() > 0 || itemPedido.getQuantidadeViagem() > 0) {
                        pedidosEnviar.add(itemPedido);
                    }
                }
            }

            if (pedidosEnviar.size() == 0) {
                UtilActivity.makeShortToast("Nenhum produto selecionado", this);
                return false;
            }

            mostrarOverlay();

            try {
                AsyncTask<Void, Void, MobileRetorno> taskNaoVenda = new AsyncTask<Void, Void, MobileRetorno>() {
                    @Override
                    protected MobileRetorno doInBackground(Void... params) {
                        MobileRetorno mobileRetorno;
                        try {
                            Pedido pedido = new Pedido();
                            pedido.setCliente(mesa);
                            pedido.setPedidos(pedidosEnviar);
                            MobileEnvioPedido mobileEnvioTabulacao = new MobileEnvioPedido(atendimentoMobile, pedido);
                            mobileRetorno = mobileEnvioServico.send(mobileEnvioTabulacao);
                        } catch (Exception e) {
                            mobileRetorno = new MobileRetornoPedido();
                            e.printStackTrace();
                            return mobileRetorno;
                        }
                        return mobileRetorno;
                    }

                    @Override
                    protected void onPostExecute(MobileRetorno resposta) {
                        super.onPostExecute(resposta);
                        if(resposta.getCodigoRetorno() != null && resposta.getCodigoRetorno().equals(ED2DCodigoResponse.OK.toString())) {
                            UtilActivity.makeShortToast("Pedido enviado com sucesso!", PedidoActivity.this);
                            mesaET.setText("");
                            atualizarAbas();
                        } else {
                            exibirMensagemErro();
                        }
                        esconderOverlay();
                    }
                };
                taskNaoVenda.execute();

            } catch (Exception e) {
                exibirMensagemErro();
                esconderOverlay();
                e.printStackTrace();
                return false;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void exibirMensagemErro() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PedidoActivity.this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setMessage("Erro ao enviar pedido, favor verificar no balcão antes de enviar novamente");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarOverlay() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando pedido");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void esconderOverlay() {
        progressDialog.cancel();
    }
}
