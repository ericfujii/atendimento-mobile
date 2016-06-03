package br.com.eric.atendimentomobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.entidade.Produto;
import br.com.eric.atendimentomobile.entidade.ProdutoTipo;
import br.com.eric.atendimentomobile.utils.EntityManager;


public class ProdutoFragment extends Fragment implements AdapterView.OnItemClickListener {

    private RelativeLayout relativeLayout;
    private ListView listView;
    private FilasActivity filasActivity;
    private EntityManager entityManager;
    private boolean camposCarregados = false;
    private ProdutoTipo produtoTipo;
    private List<Produto> produtos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        if (!camposCarregados) {
            entityManager = new EntityManager(filasActivity);
            relativeLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_pedido, container, false);
            criarCamposEntrada();
            camposCarregados = true;
        } else {
            ((ViewGroup) relativeLayout.getParent()).removeView(relativeLayout);
        }

        try {
            popularListaProdutos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return relativeLayout;
    }

    private void popularListaProdutos() throws Exception {
        produtos = entityManager.getByWhere(Produto.class, "_produto_tipo = " + produtoTipo.getId(), "nome ASC");
        ProdutoListAdapter adapter = new ProdutoListAdapter(filasActivity, produtos);
        listView = (ListView) relativeLayout.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void criarCamposEntrada() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filasActivity = (FilasActivity) getActivity();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(filasActivity, FilaProdutoActivity.class);
        intent.putExtra("idProduto", produtos.get(position).getId());
        startActivity(intent);
    }

    public void setProdutoTipo(ProdutoTipo produtoTipo) {
        this.produtoTipo = produtoTipo;
    }
}