package br.com.eric.atendimentomobile.activities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.Produto;

public class ProdutoListAdapter extends BaseAdapter {
    private Context context;
    private List<Produto> produtos;

    public ProdutoListAdapter(Context context, List<Produto> produtos){
        this.context = context;
        this.produtos = produtos;
    }

    @Override
    public int getCount() {
        if (produtos != null) {
            return produtos.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return produtos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_produto, null);
        }

        TextView nomeTV = (TextView) convertView.findViewById(R.id.nomeTV);
        nomeTV.setText(produtos.get(position).getNome());
        return convertView;
    }
}
