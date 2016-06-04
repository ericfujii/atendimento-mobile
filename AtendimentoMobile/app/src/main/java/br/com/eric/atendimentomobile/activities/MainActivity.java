package br.com.eric.atendimentomobile.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.ED2DCodigoResponse;
import br.com.eric.atendimentomobile.entidade.Produto;
import br.com.eric.atendimentomobile.entidade.ProdutoTipo;
import br.com.eric.atendimentomobile.entidade.Usuario;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioPacote;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoLogin;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoPacote;
import br.com.eric.atendimentomobile.servico.MobileEnvioServico;
import br.com.eric.atendimentomobile.utils.DataBaseException;
import br.com.eric.atendimentomobile.utils.EntityManager;
import br.com.eric.atendimentomobile.utils.UtilActivity;

public class MainActivity extends Activity {

    private TextView txtClockMainData;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private RelativeLayout menuEsquerdoRL;
    private RelativeLayout aguardeRL;
    private RelativeLayout progressRL;
    private RelativeLayout comecoRL;
    private RelativeLayout botoesRL;
    private RelativeLayout botaoClientesRL;
    private TextView nomeUsuarioTV;
    private ImageView botaoSyncIV;

    private ProgressBar aguardePB;
    private TextView statusTV;

    private boolean mDisplayBlocked = false;

    List<Map<String, Object>> itens;

    private ArrayList<MenuLateralItem> navDrawerItems;

    private EntityManager entityManager;

    private AtendimentoMobile atendimentoMobile;

    private boolean primeiroLogin;

    private ListView listView;
    private TextView listaVazia;

    private MobileEnvioServico mobileEnvioServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();
        entityManager = new EntityManager(this);
        atendimentoMobile = (AtendimentoMobile) getApplication();
        mobileEnvioServico = new MobileEnvioServico(MobileRetornoPacote.class);
        instanciarComponentes();

        SimpleDateFormat getDiaSemana = new SimpleDateFormat("EEE", Locale.ENGLISH);
        String diaSemanaTemp = getDiaSemana.format(Calendar.getInstance().getTime()).toLowerCase();
        String diaSemana = "...";

        switch (diaSemanaTemp) {
            case "sun":
                diaSemana = "dom";
                break;
            case "mon":
                diaSemana = "seg";
                break;
            case "tue":
                diaSemana = "ter";
                break;
            case "wed":
                diaSemana = "qua";
                break;
            case "thu":
                diaSemana = "qui";
                break;
            case "fri":
                diaSemana = "sex";
                break;
            case "sat":
                diaSemana = "sáb";
                break;
        }

        SimpleDateFormat getDiaHoje = new SimpleDateFormat("dd", Locale.ENGLISH);
        String diaHoje = getDiaHoje.format(Calendar.getInstance().getTime()).toLowerCase();

        SimpleDateFormat getMesAtual = new SimpleDateFormat("M", Locale.ENGLISH);
        String mesAtualTemp = getMesAtual.format(Calendar.getInstance().getTime()).toLowerCase();
        String mesAtual = "...";

        switch (mesAtualTemp) {
            case "1":
                mesAtual = "jan";
                break;
            case "2":
                mesAtual = "fev";
                break;
            case "3":
                mesAtual = "mar";
                break;
            case "4":
                mesAtual = "abr";
                break;
            case "5":
                mesAtual = "mai";
                break;
            case "6":
                mesAtual = "jun";
                break;
            case "7":
                mesAtual = "jul";
                break;
            case "8":
                mesAtual = "ago";
                break;
            case "9":
                mesAtual = "set";
                break;
            case "10":
                mesAtual = "out";
                break;
            case "11":
                mesAtual = "nov";
                break;
            case "12":
                mesAtual = "dez";
                break;
        }

        txtClockMainData = (TextView) findViewById(R.id.txtClockMainData);
        txtClockMainData.setText(String.format("%s, %s de %s", diaSemana, diaHoje, mesAtual));

        // *************************

        String mensagem = getIntent().getStringExtra("mensagem");
        if (mensagem != null) {
            UtilActivity.makeShortToast(mensagem, this);
        }

        try {
            List<ProdutoTipo> produtos = entityManager.getAll(ProdutoTipo.class);
            primeiroLogin = (produtos == null || produtos.size() == 0);
            if (primeiroLogin) {
                mostrarOverlayCarga();
                aguardePB.setProgress(0);
                statusTV.setText("Instalando");

                try {
                    AsyncTask<Void, Void, MobileRetorno> taskPacotes = new AsyncTask<Void, Void, MobileRetorno>() {
                        @Override
                        protected MobileRetorno doInBackground(Void... params) {
                            MobileRetorno mobileRetorno = null;
                            try {
                                MobileEnvioPacote mobileEnvioPacote = new MobileEnvioPacote(atendimentoMobile);
                                mobileRetorno = mobileEnvioServico.send(mobileEnvioPacote);
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
                                    MobileRetornoPacote mobileRetornoPacote = (MobileRetornoPacote) resposta;
                                    for (ProdutoTipo produtoTipo : mobileRetornoPacote.getProdutoTipos()) {
                                        entityManager.save(produtoTipo);
                                    }
                                    for (Produto produto : mobileRetornoPacote.getProdutos()) {
                                        entityManager.save(produto);
                                    }
                                    for (Usuario usuario : mobileRetornoPacote.getUsuarios()) {
                                        entityManager.save(usuario);
                                    }

                                    MainActivity.this.getIntent().putExtra("primeiroLogin", false);
                                    statusTV.setText("Completo");
                                    aguardePB.setProgress(100);
                                    progressRL.animate().setDuration(1000);
                                    progressRL.animate().alpha(0).setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            comecoRL.animate().setDuration(1000);
                                            comecoRL.animate().alpha(1).setListener(new Animator.AnimatorListener() {
                                                @Override
                                                public void onAnimationStart(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    aguardeRL.animate().setDuration(2000);
                                                    aguardeRL.animate().alpha(0).setListener(new Animator.AnimatorListener() {
                                                        @Override
                                                        public void onAnimationStart(Animator animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationEnd(Animator animation) {
                                                            esconderOverlayCarga();
                                                        }

                                                        @Override
                                                        public void onAnimationCancel(Animator animation) {

                                                        }

                                                        @Override
                                                        public void onAnimationRepeat(Animator animation) {

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onAnimationCancel(Animator animation) {

                                                }

                                                @Override
                                                public void onAnimationRepeat(Animator animation) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                } catch (Exception e) {
                                    UtilActivity.makeShortToast("Erro ao carregar pacotes.", getApplicationContext());
                                }
                            } else {
                                UtilActivity.makeShortToast("Erro ao carregar pacotes.", getApplicationContext());
                            }
                        }
                    };
                    taskPacotes.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    UtilActivity.makeShortToast("Erro ao carregar pacotes.", getApplicationContext());
                }
            }
        } catch (DataBaseException e) {
            e.printStackTrace();
        }
    }

    private void instanciarComponentes() {
        botoesRL        = (RelativeLayout) findViewById(R.id.botoesRL);
        botaoClientesRL = (RelativeLayout) findViewById(R.id.botaoClientesRL);
        menuEsquerdoRL  = (RelativeLayout) findViewById(R.id.menuEsquerdoRL);
        progressRL      = (RelativeLayout) findViewById(R.id.progressRL);
        comecoRL        = (RelativeLayout) findViewById(R.id.comecoRL);
        statusTV        = (TextView) findViewById(R.id.statusTV);
        aguardePB       = (ProgressBar) findViewById(R.id.aguardePB);
        aguardeRL       = (RelativeLayout) findViewById(R.id.aguardeRL);
        mDrawerLayout   = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList     = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        botaoSyncIV         = (ImageView) findViewById(R.id.botaoSyncIV);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void acionarMenu(View view) {
        if (mDrawerLayout.isDrawerOpen(menuEsquerdoRL)) {
            mDrawerLayout.closeDrawer(menuEsquerdoRL);
        } else {
            mDrawerLayout.openDrawer(menuEsquerdoRL);
        }
    }

    private void mostrarOverlayCarga() {
        aguardeRL.setVisibility(View.VISIBLE);
        mDisplayBlocked = true;
    }

    private void esconderOverlayCarga() {
        aguardeRL.setVisibility(View.GONE);
        mDisplayBlocked = false;
    }

    private List<Map<String, Object>> listarAgendamentos() {
        itens = new ArrayList<>();

        Map<String, Object> item = new HashMap<>();
        item.put("nome", "Nome de Teste");
        item.put("razao", "Não está em casa");
        item.put("horario", "23:59");
        itens.add(item);

        return itens;
    }

    private List<Map<String, Object>> listarContatos() {
        itens = new ArrayList<>();

        Map<String, Object> item = new HashMap<>();
        item.put("nomeContato", "Nome do contato");
        itens.add(item);

        item = new HashMap<>();
        item.put("nomeContato", "Nome do contato");
        itens.add(item);

        return itens;
    }

    public void abrirPedido(View view) {
        Intent intent = new Intent("PEDIDO");
        startActivity(intent);
    }

    public void abrirFilas(View view) {
        Intent intent = new Intent("FILAS");
        startActivity(intent);
    }

    public void atualizarProdutos(View view) {
        try {
            UtilActivity.makeShortToast("Atualizando pacotes.", getApplicationContext());
            AsyncTask<Void, Void, MobileRetorno> taskPacotes = new AsyncTask<Void, Void, MobileRetorno>() {
                @Override
                protected MobileRetorno doInBackground(Void... params) {
                    MobileRetorno mobileRetorno = null;
                    try {
                        MobileEnvioPacote mobileEnvioPacote = new MobileEnvioPacote(atendimentoMobile);
                        mobileRetorno = mobileEnvioServico.send(mobileEnvioPacote);
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
                            entityManager.executeNativeQuery("DELETE FROM produto");
                            entityManager.executeNativeQuery("DELETE FROM produto_tipo");
                            entityManager.executeNativeQuery("DELETE FROM usuario");
                            MobileRetornoPacote mobileRetornoPacote = (MobileRetornoPacote) resposta;
                            for (ProdutoTipo produtoTipo : mobileRetornoPacote.getProdutoTipos()) {
                                entityManager.save(produtoTipo);
                            }
                            for (Produto produto : mobileRetornoPacote.getProdutos()) {
                                entityManager.save(produto);
                            }
                            for (Usuario usuario : mobileRetornoPacote.getUsuarios()) {
                                entityManager.save(usuario);
                            }
                            UtilActivity.makeShortToast("Pacotes atualizados.", getApplicationContext());
                        } catch (Exception e) {
                            UtilActivity.makeShortToast("Erro ao carregar pacotes.", getApplicationContext());
                        }
                    } else {
                        UtilActivity.makeShortToast("Erro ao carregar pacotes.", getApplicationContext());
                    }
                }
            };
            taskPacotes.execute();
        } catch (Exception e) {
            e.printStackTrace();
            UtilActivity.makeShortToast("Erro ao carregar pacotes.", getApplicationContext());
        }
    }


    private class SlideMenuClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        mDrawerLayout.closeDrawer(menuEsquerdoRL);
        startActivity(navDrawerItems.get(position).getIntent());
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(menuEsquerdoRL)) {
            mDrawerLayout.closeDrawer(menuEsquerdoRL);
        }
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent pEvent) {
        return mDisplayBlocked || super.dispatchTouchEvent(pEvent);
    }

    @Override
    protected void onResume() {
        mDrawerLayout.closeDrawer(menuEsquerdoRL);
        atendimentoMobile = (AtendimentoMobile) getApplication();

        navDrawerItems = new ArrayList<>();

        navDrawerItems.add(
                new MenuLateralItem(
                        "Novo Pedido",
                        R.drawable.ic_border_color_white_48dp,
                        "",
                        new Intent("PEDIDO")));

        navDrawerItems.add(
                new MenuLateralItem(
                        "Filas",
                        R.drawable.ic_clientes_branco,
                        "",
                        new Intent("FILAS")));

        navDrawerItems.add(
                new MenuLateralItem(
                        "Chat",
                        R.drawable.ic_textsms_white_48dp,
                        "",
                        new Intent("CHAT")));

        MenuLateralListAdapter menuLateralListAdapter = new MenuLateralListAdapter(getApplicationContext(), navDrawerItems);
        menuLateralListAdapter.notifyDataSetChanged();
        mDrawerList.setAdapter(menuLateralListAdapter);

        super.onResume();
    }
}
