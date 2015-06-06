package ericfujii.com.br.atendimentomobile.activities;

import android.animation.Animator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ericfujii.com.br.atendimentomobile.R;
import ericfujii.com.br.atendimentomobile.entities.Pedido;
import ericfujii.com.br.atendimentomobile.entities.Produto;
import ericfujii.com.br.atendimentomobile.entities.ProdutoTipo;
import ericfujii.com.br.atendimentomobile.entities.Sessao;
import ericfujii.com.br.atendimentomobile.entities.Usuario;
import ericfujii.com.br.atendimentomobile.rest.ECodigoFuncao;
import ericfujii.com.br.atendimentomobile.rest.ECodigoRequest;
import ericfujii.com.br.atendimentomobile.rest.ECodigoResponse;
import ericfujii.com.br.atendimentomobile.rest.RequestAtendimentoRest;
import ericfujii.com.br.atendimentomobile.rest.ResponseAtendimentoRest;
import ericfujii.com.br.atendimentomobile.utils.D2DRestClient;
import ericfujii.com.br.atendimentomobile.utils.EntityManager;
import ericfujii.com.br.atendimentomobile.utils.JsonParser;
import ericfujii.com.br.atendimentomobile.utils.UtilActivity;

public class MainActivity extends Activity {


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private RelativeLayout menuEsquerdoRL;
    private RelativeLayout aguardeRL;
    private TextView nomeUsuarioTV;

    private boolean mDisplayBlocked = false;

    List<Map<String, Object>> itens;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<MenuLateralItem> navDrawerItems;
    private static boolean alreadyOpen = false;

    private RequestAtendimentoRest requestAtendimentoRest = new RequestAtendimentoRest();
    private JsonParser jsonParser = new JsonParser();
    private D2DRestClient d2DRestClient = new D2DRestClient();
    private EntityManager entityManager;

    private Sessao sessao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        invalidateOptionsMenu();
        entityManager = new EntityManager(this);
        sessao = (Sessao)getApplication();

        menuEsquerdoRL = (RelativeLayout) findViewById(R.id.menuEsquerdoRL);
        nomeUsuarioTV = (TextView) findViewById(R.id.nomeUsuarioTV);
        nomeUsuarioTV.setText(sessao.getNomeUsuario());
        aguardeRL = (RelativeLayout) findViewById(R.id.aguardeRL);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

        }

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

    public void abrirConfiguracoes(View view) {
        /*Intent intentConf = new Intent(this, ConfiguracoesActivity.class);
        startActivity(intentConf);
        finish();*/
    }

    public void executarCarga(View view) {
        try {
            AsyncTask<Void, Void, ResponseAtendimentoRest> taskCarga = new AsyncTask<Void, Void, ResponseAtendimentoRest>() {
                @Override
                protected ResponseAtendimentoRest doInBackground(Void... params) {
                    JSONObject jsonEnvio;
                    JSONObject jsonRetorno;
                    ResponseAtendimentoRest responseAtendimentoRest = null;
                    try {
                        requestAtendimentoRest.setCodigoFuncao(ECodigoFuncao.CARGA_PACOTES);
                        jsonEnvio = jsonParser.parseObjectToJson(requestAtendimentoRest);
                        jsonRetorno = d2DRestClient.sendPost(jsonEnvio, ECodigoRequest.PROCESSAR);
                        responseAtendimentoRest = jsonParser.parseJsonToObject(jsonRetorno, ResponseAtendimentoRest.class);
                    } catch (Exception e) {
                        responseAtendimentoRest = new ResponseAtendimentoRest();
                        responseAtendimentoRest.setCodigoResponse(ECodigoResponse.ERROR);
                        responseAtendimentoRest.setMessage(e.getMessage());
                        return responseAtendimentoRest;
                    }
                    return responseAtendimentoRest;
                }

                @Override
                protected void onPostExecute(ResponseAtendimentoRest resposta) {
                    super.onPostExecute(resposta);
                    if (resposta.getCodigoResponse().equals(ECodigoResponse.OK)) {
                        try {
                            entityManager.dropBasicTables();
                            entityManager.createBasicTables();

                            for (ProdutoTipo produtoTipo : resposta.getProdutosTipos()) {
                                entityManager.save(produtoTipo);
                            }

                            for (Produto produto : resposta.getProdutos()) {
                                entityManager.save(produto);
                            }

                            for (Usuario usuario : resposta.getUsuarios()) {
                                entityManager.save(usuario);
                            }

                            UtilActivity.makeShortToast("Carga com sucesso", getApplicationContext());

                        } catch (Exception e) {
                            UtilActivity.makeShortToast("Falha na carga", getApplicationContext());
                        }
                    } else {
                        UtilActivity.makeShortToast(resposta.getMessage(), getApplicationContext());
                    }
                }
            };
            taskCarga.execute();
        } catch (Exception e) {
            UtilActivity.makeShortToast("Falha na carga", getApplicationContext());
        }
    }

    public void abrirPedido(View view) {
        Intent intent = new Intent(this, PedidoActivity.class);
        startActivity(intent);
    }


    private class SlideMenuClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        mDrawerLayout.closeDrawer(menuEsquerdoRL);
        switch (position)
        {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(menuEsquerdoRL)) {
            mDrawerLayout.closeDrawer(menuEsquerdoRL);
        } else {
            logoff(null);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent pEvent) {
        if (!mDisplayBlocked) {
            return super.dispatchTouchEvent(pEvent);
        }
        return mDisplayBlocked;
    }

    public void logoff (View view) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        try {
                            Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentLogin);
                            MainActivity.this.finish();
                        } catch (Exception e) {
                            UtilActivity.makeShortToast("Erro ao efetuar logoff", MainActivity.this);
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deseja Realmente Realizar Logoff?").setPositiveButton("Sim", dialogClickListener)
                .setNegativeButton("Não", dialogClickListener).show();
    }

    @Override
    protected void onResume() {
        mDrawerLayout.closeDrawer(menuEsquerdoRL);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        navDrawerItems = new ArrayList<>();

        // adding nav drawer items to array
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1), true, ""));
        navDrawerItems.add(new MenuLateralItem(navMenuTitles[9], navMenuIcons.getResourceId(9, -1), true, ""));

        // Recycle the typed array
        navMenuIcons.recycle();

        // setting the nav drawer list adapter
        MenuLateralListAdapter menuLateralListAdapter = new MenuLateralListAdapter(getApplicationContext(), navDrawerItems);
        menuLateralListAdapter.notifyDataSetChanged();
        mDrawerList.setAdapter(menuLateralListAdapter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
