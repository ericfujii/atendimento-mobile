package br.com.eric.atendimentomobile.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eric on 27/01/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{


    private static final int VERSAO = 1;
    private static final String BANCO = "atendimento";

    public DatabaseHelper(Context context) {
        super(context, BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE produto (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT," +
                "_produto_tipo INTEGER," +
                "situacao TEXT," +
                "ordem INTEGER)");

        db.execSQL("CREATE TABLE produto_tipo (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT," +
                "bebida TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE usuario (" +
                "id INTEGER PRIMARY KEY, " +
                "nome TEXT," +
                "login TEXT," +
                "situacao TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
