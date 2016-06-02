package br.com.eric.atendimentomobile.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by eric on 04-08-2015.
 */
public class DataUtil {

    public static Calendar obterFinalDia(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND, 59);

        return calendar;
    }

    public static Calendar obterInicioDia(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    public static String formatarDataQuery(Calendar calendar) {
        SimpleDateFormat iso8601Format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        return iso8601Format.format(calendar.getTime());
    }

}
