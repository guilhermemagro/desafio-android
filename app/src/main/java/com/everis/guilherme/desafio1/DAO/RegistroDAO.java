package com.everis.guilherme.desafio1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.everis.guilherme.desafio1.Infra.HelperDB;
import com.everis.guilherme.desafio1.Domain.Participante;
import com.everis.guilherme.desafio1.Domain.Registro;

import java.util.ArrayList;

public class RegistroDAO {

    private static final String ID = "_id";
    private static final String ID_EVENTO = "id_evento";
    private static final String ID_PARTICIPANTE = "id_participante";
    private static final String PARTIC_ATIVO = "partic_ativo";

    public static final String TABLE_REGISTRO = "registro";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_REGISTRO + "( "
            + ID + " integer primary key autoincrement, "
            + ID_EVENTO + " integer not null, "
            + ID_PARTICIPANTE + " integer not null, "
            + PARTIC_ATIVO + " text not null);";

    private HelperDB helperDB;

    private String[] allColumns = {
            RegistroDAO.ID,
            RegistroDAO.ID_EVENTO,
            RegistroDAO.ID_PARTICIPANTE,
            RegistroDAO.PARTIC_ATIVO
    };

    public RegistroDAO(Context context){
        helperDB = new HelperDB(context);
    }

    public boolean salvar(Registro registro){
        SQLiteDatabase database = helperDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(registro.getId() != 0){
            values.put(RegistroDAO.ID, registro.getId());
        }
        values.put(RegistroDAO.ID_EVENTO, registro.getIdEvento());
        values.put(RegistroDAO.ID_PARTICIPANTE, registro.getIdParticipante());
        values.put(RegistroDAO.PARTIC_ATIVO, registro.isParticipanteAtivo());

        long insertId = database.insert(RegistroDAO.TABLE_REGISTRO, null, values);

        return insertId > 0;
    }

    public boolean inativarParticipante(Registro registro){
        SQLiteDatabase database = helperDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RegistroDAO.PARTIC_ATIVO, false);

        int insertId = database.update(RegistroDAO.TABLE_REGISTRO, values, ID + "=?",
                new String[]{String.valueOf(registro.getId())});

        return insertId > 0;
    }

    public Registro buscarPorIdParticipante(long idParticipante){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(RegistroDAO.TABLE_REGISTRO, allColumns, RegistroDAO.ID_PARTICIPANTE + "='"
                + idParticipante + "'", null, null,null,null);
        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    public int contParticipantesPorIdEvento(long idEvento){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(RegistroDAO.TABLE_REGISTRO, allColumns, RegistroDAO.ID_EVENTO + "='"
                + idEvento + "' AND " + RegistroDAO.PARTIC_ATIVO + "=1", null, null,null,null);

        return cursor.getCount();
    }

    public ArrayList<Participante> listarParticipantesDoEvento(long idEvento){
        SQLiteDatabase database = helperDB.getReadableDatabase();
//        SELECT * FROM participantes INNER JOIN registro ON participantes.ID = registros.Id_participante
//        WHERE registro.id_evento = id_evento AND registro.part_ativo = 1
        Cursor cursor = database.rawQuery("SELECT * FROM " + ParticipanteDAO.TABLE_PARTICIPANTE +
                        " INNER JOIN " + TABLE_REGISTRO +
                        " ON " + ParticipanteDAO.TABLE_PARTICIPANTE + "." + ParticipanteDAO.ID + "=" +
                        TABLE_REGISTRO + "." + ID_PARTICIPANTE +
                        " WHERE " + TABLE_REGISTRO + "." + ID_EVENTO + "=" + idEvento +
                        " AND " + TABLE_REGISTRO + "." + PARTIC_ATIVO + "=1", null);
        ArrayList<Participante> participantes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            int indId = cursor.getColumnIndex(ParticipanteDAO.ID);
            int indNome = cursor.getColumnIndex(ParticipanteDAO.NOME);
            int indEmail = cursor.getColumnIndex(ParticipanteDAO.EMAIL);
            int indTelefone = cursor.getColumnIndex(ParticipanteDAO.TELEFONE);
            int indConheceTema = cursor.getColumnIndex(ParticipanteDAO.CONHECE_TEMA);
            int indIdUsuario = cursor.getColumnIndex(ParticipanteDAO.ID_USUARIO);
            do {
                Participante participante = new Participante();
                participante.setId(cursor.getLong(indId));
                participante.setNome(cursor.getString(indNome));
                participante.setEmail(cursor.getString(indEmail));
                participante.setTelefone(cursor.getString(indTelefone));
                participante.setConheceTema(cursor.getInt(indConheceTema) > 0);
                participante.setIdUsuario(cursor.getInt(indIdUsuario));
                participantes.add(participante);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return participantes;
    }

    private Registro cursorToObject(Cursor cursor){
        int indId = cursor.getColumnIndex(RegistroDAO.ID);
        int indIdEvento = cursor.getColumnIndex(RegistroDAO.ID_EVENTO);
        int indIdUsuario = cursor.getColumnIndex(RegistroDAO.ID_PARTICIPANTE);
        int indPartAtivo = cursor.getColumnIndex(RegistroDAO.PARTIC_ATIVO);

        Registro registro = new Registro();
        registro.setId(cursor.getLong(indId));
        registro.setIdEvento(cursor.getLong(indIdEvento));
        registro.setIdParticipante(cursor.getLong(indIdUsuario));
        registro.setParticipanteAtivo(cursor.getInt(indPartAtivo) > 0);
        return registro;
    }

//        public Registro buscarPorId(long id){
//        SQLiteDatabase database = helperDB.getReadableDatabase();
//        Cursor cursor = database.query(RegistroDAO.TABLE_REGISTRO, allColumns, RegistroDAO.ID + "='"
//                + id + "'", null, null,null,null);
//        cursor.moveToFirst();
//        return cursorToObject(cursor);
//    }

//    public ArrayList<Registro> buscarTodos(){
//        SQLiteDatabase database = helperDB.getReadableDatabase();
//        Cursor cursor = database.query(RegistroDAO.TABLE_REGISTRO, allColumns, null, null,null,null,null);
//        ArrayList<Registro> registros = new ArrayList<>();
//        if (cursor.moveToFirst()) {
//            int indId = cursor.getColumnIndex(RegistroDAO.ID);
//            int indIdEvento = cursor.getColumnIndex(RegistroDAO.ID_EVENTO);
//            int indIdUsuario = cursor.getColumnIndex(RegistroDAO.ID_PARTICIPANTE);
//            int indPartAtivo = cursor.getColumnIndex(RegistroDAO.PARTIC_ATIVO);
//            do {
//                Registro registro = new Registro();
//                registro.setId(cursor.getLong(indId));
//                registro.setIdEvento(cursor.getLong(indIdEvento));
//                registro.setIdParticipante(cursor.getLong(indIdUsuario));
//                registro.setParticipanteAtivo(cursor.getInt(indPartAtivo) > 0);
//                registros.add(registro);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return registros;
//    }

}
