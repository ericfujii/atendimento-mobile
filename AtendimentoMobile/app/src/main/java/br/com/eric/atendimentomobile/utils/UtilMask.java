package br.com.eric.atendimentomobile.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import br.com.eric.atendimentomobile.entidade.SistemaConstantes;

/**
 * Created by eric on 13-03-2015.
 */
public class UtilMask {
    public static String unmask(String s) {
        if (s != null) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "")
                    .replaceAll("[/]", "").replaceAll("[(]", "")
                    .replaceAll("[)]", "").replaceAll("[ ]", "");
        }

        return s;
    }

    public static String unmaskMoeda(String s) {
        if (s != null) {
            return s.replace("R", "").replace("$", "")
                    .replace(" ", "").replaceAll("[.]","")
                    .replace(",", ".");
        }

        return s;
    }

    public static TextWatcher insert(final String mask, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String str = UtilMask.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#') {
                        if (i != str.length()) {
                            mascara += m;
                        }
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    public static TextWatcher insertDynamic(final String maskBefore, final String maskAfter, final EditText ediTxt) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String mask;
                if (s.length() > maskBefore.length()) {
                   mask = maskAfter;
                } else {
                   mask = maskBefore;
                }

                String str = UtilMask.unmask(s.toString());
                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if (m != '#') {
                        if (i != str.length()) {
                            mascara += m;
                        }
                        continue;
                    }
                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                ediTxt.setText(mascara);
                ediTxt.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        };
    }

    public static void setMascaraTelefone(EditText editText) {
        editText.addTextChangedListener(UtilMask.insertDynamic("(##) ####-####", "(##) #####-####", editText));
    }

    public static void setMascaraCpfCnpj(EditText editText) {
        editText.addTextChangedListener(UtilMask.insertDynamic("###.###.###-##", "##.###.###/####-##", editText));
    }

    public static void setMascaraCep(EditText editText) {
        editText.addTextChangedListener(UtilMask.insert("#####-###", editText));
    }

    public static void setMascaraMoeda(EditText editText, Integer maxLength ) {
        editText.setText("R$ 0,00");
        DecimalTextWatcher decimalTextWatcher = new DecimalTextWatcher(editText, maxLength);
        editText.addTextChangedListener(decimalTextWatcher);
    }

    public static boolean validarTamanhoTelefone(String telefone) {
        boolean retorno = false;
        telefone = unmask(telefone);
        if ((telefone == null
            || telefone.isEmpty())
            || ((telefone != null)
            && (telefone.length() > SistemaConstantes.NOVE)
            && (telefone.length() < SistemaConstantes.DOZE))) {
            retorno = true;
        }
        return retorno;
    }

    public static boolean validarTamanhoCep(String cep) {
        boolean retorno = false;
        cep = unmask(cep);
        if ((cep == null
                || cep.isEmpty())
                || ((cep != null)
                && (cep.length() == SistemaConstantes.OITO))) {
            retorno = true;
        }
        return retorno;
    }
}
