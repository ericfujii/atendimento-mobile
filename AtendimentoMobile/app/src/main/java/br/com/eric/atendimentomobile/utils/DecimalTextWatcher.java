package br.com.eric.atendimentomobile.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by samara on 03/03/15.
 */
public class DecimalTextWatcher implements TextWatcher {

    private DecimalFormat df;
    private EditText et;
    private Integer maxLenght;

    public DecimalTextWatcher(EditText et, Integer maxLenght) {
        Locale myLocale = new Locale("pt", "BR");
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(myLocale);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#,###.##", otherSymbols);
        df.setDecimalSeparatorAlwaysShown(true);
        this.et = et;
        this.maxLenght = maxLenght;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        et.removeTextChangedListener(this);

        try {
            int inilen, endlen;
            inilen = et.getText().length();

            String v = s.toString().replace(String.valueOf(df.getDecimalFormatSymbols().getGroupingSeparator()), "").replace("R", "").replace("$", "").replace(" ", "");
            int cp = et.getSelectionStart();
            boolean lastZero = false;
            boolean lastDoubleZero = false;
            v = v.replace(",","");
            if (v.length() > maxLenght) {
                v = v.substring(0, maxLenght);
            }
            String part1 = v.substring(0, v.length()-2);
            String part2 = v.substring(v.length()-2, v.length());
            v = part1 + df.getDecimalFormatSymbols().getDecimalSeparator() + part2;
            String last = v.substring(v.length()-1, v.length());
            if (last.equals("0")) {
                lastZero = true;
            }
            if (part2.equals("00")) {
                lastZero = false;
                lastDoubleZero = true;
            }
            Number n = df.parse(v);
            String text = df.format(n);
            if (lastZero) {
                text += "0";
            }
            if (lastDoubleZero) {
                text += "00";
            }
            et.setText("R$ " + text);
            endlen = et.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= et.getText().length()) {
                et.setSelection(sel);
            } else {
                et.setSelection(et.getText().length() - 1);
            }
        } catch (Exception e) {
            et.setText("R$ 0,00");
            et.setSelection(et.getText().length());
        }
        et.addTextChangedListener(this);
    }
}
