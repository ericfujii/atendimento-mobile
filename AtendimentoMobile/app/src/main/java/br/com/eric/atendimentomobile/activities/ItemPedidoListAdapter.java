package br.com.eric.atendimentomobile.activities;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.ItemPedido;


/**
 * Created by eric on 26-02-2015.
 */
public class ItemPedidoListAdapter extends BaseAdapter {
    private Context context;
    private List<ItemPedido> itensPedido;

    public ItemPedidoListAdapter(Context context, List<ItemPedido> itensPedido){
        this.context = context;
        this.itensPedido = itensPedido;
    }

    @Override
    public int getCount() {
        return itensPedido.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_pedido, null);
        }

        TextView nomeTV = (TextView) convertView.findViewById(R.id.nomeTV);
        nomeTV.setText(itensPedido.get(position).getProduto().getNome());
        NumberPicker quantidadeNP = (NumberPicker) convertView.findViewById(R.id.quantidadeNP);
        quantidadeNP.setMinValue(0);
        quantidadeNP.setMaxValue(20);
        quantidadeNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                itensPedido.get(position).setQuantidadeMesa(newVal);
            }
        });

        NumberPicker quantidadeViagemNP = (NumberPicker) convertView.findViewById(R.id.quantidadeViagemNP);
        quantidadeViagemNP.setMinValue(0);
        quantidadeViagemNP.setMaxValue(20);
        quantidadeViagemNP.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                itensPedido.get(position).setQuantidadeViagem(newVal);
            }
        });

        EditText obsET = (EditText) convertView.findViewById(R.id.obsET);
        obsET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                itensPedido.get(position).setObservacao(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return convertView;
    }
}
