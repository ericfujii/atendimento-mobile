package br.com.eric.atendimentomobile.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.SistemaConstantes;

public class FilaProdutoActivity extends ActionBarActivity {

    private ListView listView;
    private Integer idProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fila_produto);
        listView = (ListView) findViewById(R.id.listView);
        idProduto = getIntent().getIntExtra("idProduto", SistemaConstantes.ZERO);
    }
}
