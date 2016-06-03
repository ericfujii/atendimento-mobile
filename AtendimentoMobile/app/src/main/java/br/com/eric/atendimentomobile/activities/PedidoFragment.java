package br.com.eric.atendimentomobile.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.entidade.Produto;
import br.com.eric.atendimentomobile.entidade.ProdutoTipo;
import br.com.eric.atendimentomobile.utils.EntityManager;


public class PedidoFragment extends Fragment {

    private RelativeLayout relativeLayout;
    private ListView listView;
    private PedidoActivity pedidoActivity;
    private EntityManager entityManager;
    private boolean camposCarregados = false;
    private ProdutoTipo produtoTipo;
    private List<ItemPedido> itensPedido;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        if (!camposCarregados) {
            entityManager = new EntityManager(pedidoActivity);
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
        List<Produto> produtos = entityManager.getByWhere(Produto.class, "_produto_tipo = " + produtoTipo.getId(), "ordem");
        itensPedido = new ArrayList<>();
        for (Produto produto : produtos) {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itensPedido.add(itemPedido);
        }
        ItemPedidoListAdapter adapter = new ItemPedidoListAdapter(pedidoActivity, itensPedido);
        listView = (ListView) relativeLayout.findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    private void criarCamposEntrada() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pedidoActivity = (PedidoActivity) getActivity();
    }

    public void setProdutoTipo(ProdutoTipo produtoTipo) {
        this.produtoTipo = produtoTipo;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }
}