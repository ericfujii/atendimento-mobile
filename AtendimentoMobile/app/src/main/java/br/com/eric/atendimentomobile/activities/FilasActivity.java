package br.com.eric.atendimentomobile.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.Produto;
import br.com.eric.atendimentomobile.entidade.ProdutoTipo;
import br.com.eric.atendimentomobile.utils.EntityManager;
import br.com.eric.atendimentomobile.utils.SlidingTabLayout;

public class FilasActivity extends ActionBarActivity {

    private ViewPager pager;
    private TabAdapter adapter;
    private SlidingTabLayout tabs;
    private List<String> titles;
    private List<Fragment> fragments;
    private EntityManager entityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        entityManager = new EntityManager(this);
        atualizarAbas();
    }

    public void atualizarAbas() {
        try {
            fragments = new ArrayList<>();
            titles = new ArrayList<>();

            List<ProdutoTipo> produtoTipos = entityManager.getByWhere(ProdutoTipo.class, "situacao = 'ATIVO'", "nome DESC");
            for (ProdutoTipo produtoTipo : produtoTipos) {
                ProdutoFragment produtoFragment = new ProdutoFragment();
                produtoFragment.setProdutoTipo(produtoTipo);
                titles.add(produtoTipo.getNome());
                fragments.add(produtoFragment);
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
