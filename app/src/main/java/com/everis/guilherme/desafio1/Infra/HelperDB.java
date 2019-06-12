package com.everis.guilherme.desafio1.Infra;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.everis.guilherme.desafio1.DAO.EventoDAO;
import com.everis.guilherme.desafio1.DAO.ParticipanteDAO;
import com.everis.guilherme.desafio1.DAO.RegistroDAO;
import com.everis.guilherme.desafio1.DAO.UsuarioDAO;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eventos.db";
    private static final int DATABASE_VERSION = 1;

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventoDAO.DATABASE_CREATE);
        db.execSQL(ParticipanteDAO.DATABASE_CREATE);
        db.execSQL(UsuarioDAO.DATABASE_CREATE);
        db.execSQL(RegistroDAO.DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(HelperDB.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + EventoDAO.TABLE_EVENTO);
        db.execSQL("DROP TABLE IF EXISTS " + ParticipanteDAO.TABLE_PARTICIPANTE);
        db.execSQL("DROP TABLE IF EXISTS " + UsuarioDAO.TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + RegistroDAO.TABLE_REGISTRO);
        onCreate(db);
    }
}
