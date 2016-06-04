package br.com.eric.atendimentomobile.activities;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.EBoolean;
import br.com.eric.atendimentomobile.entidade.Mensagem;
import br.com.eric.atendimentomobile.entidade.Usuario;

public class MensagemUsuarioListAdapter extends BaseAdapter {
    private Context context;
    private List<Mensagem> mensagens;

    public MensagemUsuarioListAdapter(Context context, List<Mensagem> mensagens){
        this.context = context;
        this.mensagens = mensagens;
    }

    @Override
    public int getCount() {
        if (mensagens != null) {
            return mensagens.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mensagens.get(position);
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
            convertView = mInflater.inflate(R.layout.item_mensagens_usuario, null);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        TextView remetenteTV = (TextView) convertView.findViewById(R.id.remetenteTV);
        TextView mensagemTV = (TextView) convertView.findViewById(R.id.mensagemTV);
        TextView dataMensagemTV = (TextView) convertView.findViewById(R.id.dataMensagemTV);

        remetenteTV.setText(mensagens.get(position).getRemetente().getNome());
        mensagemTV.setText(mensagens.get(position).getMensagem());
        dataMensagemTV.setText(dateFormat.format(mensagens.get(position).getDataMensagem().getTime()));
        return convertView;
    }
}
