package br.com.eric.atendimentomobile.entidade;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.eric.atendimentomobile.R;

/**
 * Created by eric on 18-03-2015.
 */
public class ItemComboAdapter extends ArrayAdapter<String> {

    private Activity context;
    private List<String> values;
    private int txtViewResourceId;
    private int layoutItem;

    public ItemComboAdapter(Activity ctx, int txtViewResourceId, List<String> objects) {
        super(ctx, txtViewResourceId, objects);
        this.context = ctx;
        this.values = objects;
        this.txtViewResourceId = txtViewResourceId;
    }

    public ItemComboAdapter(Activity ctx, int txtViewResourceId, List<String> objects, int layoutItem) {
        super(ctx, txtViewResourceId, objects);
        this.context = ctx;
        this.values = objects;
        this.txtViewResourceId = txtViewResourceId;
        this.layoutItem = layoutItem;
    }

    @Override
    public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
        return getCustomView(position, cnvtView, prnt);
    }

    @Override
    public View getView(int pos, View cnvtView, ViewGroup prnt) {
        return getCustomView(pos, cnvtView, prnt);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View mySpinner;
        if (layoutItem != 0) {
            mySpinner = inflater.inflate(layoutItem, parent, false);
        } else {
            mySpinner = inflater.inflate(R.layout.item_spinner, parent, false);
        }
        TextView main_text = (TextView) mySpinner .findViewById(R.id.text1);
        main_text.setText(values.get(position));
        return mySpinner;
    }

}
