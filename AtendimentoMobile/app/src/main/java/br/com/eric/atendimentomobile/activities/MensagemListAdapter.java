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
import br.com.eric.atendimentomobile.entidade.ItemPedido;
import br.com.eric.atendimentomobile.entidade.Mensagem;
import br.com.eric.atendimentomobile.entidade.Usuario;

public class MensagemListAdapter extends BaseAdapter {
    private Context context;
    private Map<Usuario, List<Mensagem>> mensagens;
    private List<Usuario> usuarios;
    private Integer idUsuario;

    public MensagemListAdapter(Context context, List<Usuario> usuarios, Map<Usuario, List<Mensagem>> mensagens, Integer idUsuario){
        this.context = context;
        this.mensagens = mensagens;
        this.usuarios = usuarios;
        this.idUsuario = idUsuario;
    }

    @Override
    public int getCount() {
        if (usuarios != null) {
            return usuarios.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mensagens.get(usuarios.get(position));
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
            convertView = mInflater.inflate(R.layout.item_mensagens, null);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        TextView remetenteTV = (TextView) convertView.findViewById(R.id.remetenteTV);
        TextView ultimaMensagemTV = (TextView) convertView.findViewById(R.id.ultimaMensagemTV);
        TextView dataUltimaMensagemTV = (TextView) convertView.findViewById(R.id.dataUltimaMensagemTV);
        TextView quantidadeMensagensTV = (TextView) convertView.findViewById(R.id.quantidadeMensagensTV);

        remetenteTV.setText(usuarios.get(position).getNome());
        if (mensagens.get(usuarios.get(position)) != null && mensagens.get(usuarios.get(position)).size() > 0) {
            String quemDisse = "";
            if (idUsuario.equals(mensagens.get(usuarios.get(position)).get(mensagens.get(usuarios.get(position)).size()-1).getRemetente().getId())) {
                quemDisse = "Você disse: ";
            } else {
                quemDisse = mensagens.get(usuarios.get(position)).get(mensagens.get(usuarios.get(position)).size()-1).getRemetente().getNome() + " disse: ";
            }
            ultimaMensagemTV.setText(quemDisse + mensagens.get(usuarios.get(position)).get(mensagens.get(usuarios.get(position)).size()-1).getMensagem());
            dataUltimaMensagemTV.setText(dateFormat.format(mensagens.get(usuarios.get(position)).get(mensagens.get(usuarios.get(position)).size()-1).getDataMensagem().getTime()));
            Integer quantidadeNovas = 0;
            for (Mensagem mensagem : mensagens.get(usuarios.get(position))) {
                if (mensagem.getVisualizada() != EBoolean.TRUE) {
                    quantidadeNovas++;
                }
            }
            if (quantidadeNovas > 0) {
                quantidadeMensagensTV.setText(quantidadeNovas.toString());
            } else {
                quantidadeMensagensTV.setVisibility(View.GONE);
            }
        } else {
            ultimaMensagemTV.setText("");
            dataUltimaMensagemTV.setText("");
            quantidadeMensagensTV.setText("");
        }
        return convertView;
    }
}
