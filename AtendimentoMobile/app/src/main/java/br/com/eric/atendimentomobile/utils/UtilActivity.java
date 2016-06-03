package br.com.eric.atendimentomobile.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.activities.MainActivity;
import br.com.eric.atendimentomobile.entidade.ItemComboAdapter;
import br.com.eric.atendimentomobile.entidade.SistemaConstantes;

/**
 * Created by samara on 20/02/15.
 */
public class UtilActivity {

    public static final String SELECIONE = "SELECIONE";
    public static final String MULTI_INTERNET = "MULTI INTERNET";
    public static final String COMBO_MULTI = "COMBO MULTI";

    public static void makeShortToast(String mensagem, Context context) {
        Toast.makeText(context, mensagem, Toast.LENGTH_SHORT).show();
    }

    public static void setAdapter(Activity activity, Spinner spinner, List<String> strings) {
        ItemComboAdapter adapter = new ItemComboAdapter(activity, R.layout.spinner, strings);
        adapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(adapter);
    }

    public static Calendar getCalendarValue(EditText editText) {
        if (editText != null
                && editText.getText() != null
                && !editText.getText().toString().isEmpty()
                && !"__/__/____".equals(editText.getText().toString())) {
            String textoData = editText.getText().toString();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date data;
            try {
                data = df.parse(textoData);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(data);
                return calendar;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Calendar ConverterStringParaCalendar(String strData) throws ParseException {
        if (strData.trim().equals("")) {
            return null;
        }
        DateFormat formatter;
        Date date;
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = (Date) formatter.parse(strData);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public static boolean isValidSpinnerValue(Spinner spinner) {
        if (spinner != null
                && spinner.getSelectedItemPosition() > SistemaConstantes.ZERO) {
            return true;
        }
        return false;
    }

    public static boolean isValidIntegerValue(EditText editText) {
        if (editText.getText() != null
                && !editText.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() * (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected())
            return true;
        else
            return false;
    }

    public static void irParaHome(Activity context) {
        Intent main = new Intent(context, MainActivity.class);
        context.startActivity(main);
        context.finish();
    }

    public static String formatarMoeda(Double valor) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        return "R$ " + decimalFormat.format(valor);
    }

    public static String serializarImagem(Bitmap bitmap) throws UnsupportedEncodingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static File convertBitmapToTempFile(Bitmap bitmap) throws IOException {
        File file = getTempFile();
        FileOutputStream fOut = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();

        return file;
    }

    public static File getTempFile() {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/MobsalesTempFiles";
        File dir = new File(filePath);
        if (!dir.exists())
            dir.mkdirs();
        return new File(dir, "tempImage.png");
    }

    public static File createNewImage(String fileName) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/MobsalesImages";
        File dir = new File(filePath);
        if (!dir.exists())
            dir.mkdirs();
        return new File(dir, fileName + ".jpeg");
    }

    public static File convertBitmapToFile(Bitmap bitmap, String fileName) throws IOException {
        File file = createNewImage(fileName);
        FileOutputStream fOut = new FileOutputStream(file);

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush();
        fOut.close();

        return file;
    }

    public static Bitmap getBitmapFromFile(String fileName) {
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/MobsalesImages";
        return BitmapFactory.decodeFile(filePath + "/" + fileName + ".png");

    }

    public static Bitmap deserializarImagem(String text) throws UnsupportedEncodingException {
        try {
            byte[] encodeByte = Base64.decode(text, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static Bitmap redimensionarImagem(Bitmap realImage, float maxImageSize,
                                             boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width,
                height, filter);
    }

    public static Bitmap corrigirOrientacaoImagem(String path, Bitmap imagemFinal) throws IOException {
        int orientation;

        if (path == null) {
            return null;
        }
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        BitmapFactory.Options o2 = new BitmapFactory.Options();

        ExifInterface exif = new ExifInterface(path);

        orientation = exif
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

        Matrix m = new Matrix();

        if ((orientation == ExifInterface.ORIENTATION_ROTATE_180)) {
            m.postRotate(180);
            return Bitmap.createBitmap(imagemFinal, 0, 0, imagemFinal.getWidth(),
                    imagemFinal.getHeight(), m, true);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
            m.postRotate(90);
            return Bitmap.createBitmap(imagemFinal, 0, 0, imagemFinal.getWidth(),
                    imagemFinal.getHeight(), m, true);
        } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
            m.postRotate(270);
            return Bitmap.createBitmap(imagemFinal, 0, 0, imagemFinal.getWidth(),
                    imagemFinal.getHeight(), m, true);
        }
        return imagemFinal;
    }

    public static void makeNotification(Context context, Integer notificacaoCodigo, int logo, String titulo, String texto, String aviso, Boolean permanente) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(logo)
                        .setContentTitle(titulo)
                        .setContentText(texto)
                        .setTicker(aviso)
                        .setAutoCancel(true);
        int mNotificationId = notificacaoCodigo;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        if (permanente) {
            notification.flags = Notification.FLAG_ONGOING_EVENT;
        }
        mNotifyMgr.notify(mNotificationId, notification);
    }

    public static void makeNotification(Context context, String tag, Integer notificacaoCodigo, int logo, String titulo, String texto, String aviso, Boolean permanente) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(logo)
                        .setContentTitle(titulo)
                        .setContentText(texto)
                        .setTicker(aviso)
                        .setAutoCancel(true);
        int mNotificationId = notificacaoCodigo;
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        if (permanente) {
            notification.flags = Notification.FLAG_ONGOING_EVENT;
        }
        mNotifyMgr.notify(tag, mNotificationId, notification);
    }

    public static void cancelNotification(Context context, Integer notificacaoCodigo) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) context.getSystemService(ns);
        nMgr.cancel(notificacaoCodigo);
    }

    public static void vibrate(Context context, long milis) {
        Vibrator vibrator;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(milis);
    }

    public static long calcularDiferencaHorariosEmSegundos(Calendar dataHorarioInicio, Calendar dataHorarioFim) {
        return ((dataHorarioFim.getTimeInMillis() - dataHorarioInicio.getTimeInMillis())
                / SistemaConstantes.MIL);
    }

    public static void alert(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }

    public static String formatarTelefone(String telefone) {

        String telefoneFormatado = " ";
        String numeroInicial = telefone.substring(0, 4);
        if (telefone.length() == 10) {
            telefoneFormatado = "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 6) + "-" + telefone.substring(6, 10);
        }
        if (telefone.length() == 11 && !numeroInicial.equals("0800")) {
            telefoneFormatado = "(" + telefone.substring(0, 2) + ") " + telefone.substring(2, 7) + "-" + telefone.substring(7, 11);
        }
        if (telefone.length() < 10 || telefone.length() > 11 || numeroInicial.equals("0800")) {
            telefoneFormatado = telefone;
        }
        return telefoneFormatado;

//        String telefoneFormatado=" ";
//        String numeroInicial = telefone.substring(0,4);
//        if(telefone.length() == 10) {
//            telefoneFormatado = "(" + telefone.substring(0,2) + ") " + telefone.substring(2,6) +"-"+ telefone.substring(6,10);
//        }
//        if(telefone.length() == 11 && !numeroInicial.equals("0800") && !numeroInicial.equals("4004")){
//            telefoneFormatado = "(" + telefone.substring(0,2) + ") " + telefone.substring(2,7) +"-"+ telefone.substring(7,11);
//        }
//        if( telefone.length() < 10 || telefone.length() >11 || numeroInicial.equals("0800") || numeroInicial.equals("4004")) {
//            telefoneFormatado = telefone;
//        }
//        return telefoneFormatado;
    }

    public static String formatarCPF(String cpf) {
        if (cpf.length() > 11 || cpf.length() < 11) {
            return cpf;
        } else {
            cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
            return cpf;
        }
    }

    public static String formatarCNPJ(String cnpj) {
        if (cnpj.length() > 14 || cnpj.length() < 14) {
            return cnpj;
        } else {
            cnpj = cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);
            return cnpj;
        }
    }

    public static String removerNaoDigitos(String value) {
        if (value == null || value.isEmpty()) {
            return "0";
        }
        return value.replaceAll("[^\\d]", "");
    }

    public static ProgressDialog mostrarOverlay(Context context, String mensagem) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(mensagem);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }

    public static void esconderOverlay(ProgressDialog progressDialog) {
        progressDialog.cancel();
    }
}
