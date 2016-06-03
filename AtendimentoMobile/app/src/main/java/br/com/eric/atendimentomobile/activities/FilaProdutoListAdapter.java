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
import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.entidade.Produto;

public class FilaProdutoListAdapter extends BaseAdapter {
    private Context context;
    private List<ItemPedido> itensPedido;

    public FilaProdutoListAdapter(Context context, List<ItemPedido> itensPedido){
        this.context = context;
        this.itensPedido = itensPedido;
    }

    @Override
    public int getCount() {
        if (itensPedido != null) {
            return itensPedido.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return itensPedido.get(position);
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
            convertView = mInflater.inflate(R.layout.item_produto_fila, null);
        }

        TextView mesaClienteTV = (TextView) convertView.findViewById(R.id.mesaClienteTV);
        TextView situacaoTV = (TextView) convertView.findViewById(R.id.situacaoTV);
        TextView quantidadeMesaTV = (TextView) convertView.findViewById(R.id.quantidadeMesaTV);
        TextView quantidadeViagemTV = (TextView) convertView.findViewById(R.id.quantidadeViagemTV);
        TextView quantidadeTotalTV = (TextView) convertView.findViewById(R.id.quantidadeTotalTV);

        mesaClienteTV.setText("Mesa/Cliente: " + itensPedido.get(position).getPedido().getCliente());
        situacaoTV.setText(itensPedido.get(position).getSituacaoPedido().toString());
        quantidadeMesaTV.setText("Mesa: " + itensPedido.get(position).getQuantidadeMesa());
        quantidadeViagemTV.setText("Viagem: " + itensPedido.get(position).getQuantidadeViagem());
        quantidadeTotalTV.setText("Total: " + (itensPedido.get(position).getQuantidadeMesa() + itensPedido.get(position).getQuantidadeViagem()));

        return convertView;
    }
}
