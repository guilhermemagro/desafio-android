package com.everis.guilherme.desafio1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.everis.guilherme.desafio1.Infra.HelperDB;
import com.everis.guilherme.desafio1.Domain.Participante;

import java.util.ArrayList;

public class ParticipanteDAO {

    public static final String TABLE_PARTICIPANTE = "participante";

    public static final String ID = "_id";
    public static final String NOME = "nome";
    public static final String EMAIL = "email";
    public static final String TELEFONE = "telefone";
    public static final String CONHECE_TEMA = "conhece_tema";
    public static final String ID_USUARIO = "id_usuario";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_PARTICIPANTE + "( "
            + ID + " integer primary key autoincrement, "
            + NOME + " text not null, "
            + EMAIL + " text not null, "
            + TELEFONE + " text not null, "
            + CONHECE_TEMA + " text not null, "
            + ID_USUARIO + " integer not null);";

    private HelperDB helperDB;

    private String[] allColumns = {
            ParticipanteDAO.ID,
            ParticipanteDAO.NOME,
            ParticipanteDAO.EMAIL,
            ParticipanteDAO.TELEFONE,
            ParticipanteDAO.CONHECE_TEMA,
            ParticipanteDAO.ID_USUARIO
    };

    public ParticipanteDAO(Context context){
        helperDB = new HelperDB(context);
    }

    public boolean salvar(Participante participante){
        SQLiteDatabase database = helperDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(participante.getId() != 0){
            values.put(ParticipanteDAO.ID, participante.getId());
        }
        values.put(ParticipanteDAO.NOME, participante.getNome());
        values.put(ParticipanteDAO.EMAIL, participante.getEmail());
        values.put(ParticipanteDAO.TELEFONE, participante.getTelefone());
        values.put(ParticipanteDAO.CONHECE_TEMA, participante.isConheceTema());
        values.put(ParticipanteDAO.ID_USUARIO, participante.getIdUsuario());
        long insertId = database.insert(ParticipanteDAO.TABLE_PARTICIPANTE, null, values);
        return insertId > 0;
    }

    public long buscarIdPorEmail(String email){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(ParticipanteDAO.TABLE_PARTICIPANTE, allColumns, ParticipanteDAO.EMAIL + "='"
                + email + "'", null, null,null,null);
        cursor.moveToFirst();
        return cursorToObject(cursor).getId();
    }

    public boolean verificarSeParticipanteExiste(String email){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(ParticipanteDAO.TABLE_PARTICIPANTE, allColumns, ParticipanteDAO.EMAIL + "='"
                + email + "'", null, null,null,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private Participante cursorToObject(Cursor cursor){
        int indId = cursor.getColumnIndex(ParticipanteDAO.ID);
        int indNome = cursor.getColumnIndex(ParticipanteDAO.NOME);
        int indEmail = cursor.getColumnIndex(ParticipanteDAO.EMAIL);
        int indTelefone = cursor.getColumnIndex(ParticipanteDAO.TELEFONE);
        int indConheceTema = cursor.getColumnIndex(ParticipanteDAO.CONHECE_TEMA);
        int indIdUsuario = cursor.getColumnIndex(ParticipanteDAO.ID_USUARIO);

        Participante participante = new Participante();
        participante.setId(cursor.getLong(indId));
        participante.setNome(cursor.getString(indNome));
        participante.setEmail(cursor.getString(indEmail));
        participante.setTelefone(cursor.getString(indTelefone));
        participante.setConheceTema(cursor.getInt(indConheceTema) > 0);
        participante.setIdUsuario(cursor.getLong(indIdUsuario));
        return participante;
    }

//        public Participante buscarPorId(long id){
//        SQLiteDatabase database = helperDB.getReadableDatabase();
//
//        Cursor cursor = database.query(ParticipanteDAO.TABLE_PARTICIPANTE, allColumns, ParticipanteDAO.ID + "='"
//                + id + "'", null, null,null,null);
//        cursor.moveToFirst();
//
//        return cursorToObject(cursor);
//    }

//    public Participante buscarPorEmail(String email){
//        SQLiteDatabase database = helperDB.getReadableDatabase();
//
//        Cursor cursor = database.query(ParticipanteDAO.TABLE_PARTICIPANTE, allColumns, ParticipanteDAO.EMAIL + "='"
//                + email + "'", null, null,null,null);
//        cursor.moveToFirst();
//
//        return cursorToObject(cursor);
//    }

//        public ArrayList<Participante> buscarTodos(){
//        SQLiteDatabase database = helperDB.getReadableDatabase();
//
//        Cursor cursor = database.query(ParticipanteDAO.TABLE_PARTICIPANTE, allColumns, null, null,null,null,null);
//        ArrayList<Participante> participantes = new ArrayList<>();
//
//        if (cursor.moveToFirst()) {
//            int indId = cursor.getColumnIndex(ParticipanteDAO.ID);
//            int indNome = cursor.getColumnIndex(ParticipanteDAO.NOME);
//            int indEmail = cursor.getColumnIndex(ParticipanteDAO.EMAIL);
//            int indTelefone = cursor.getColumnIndex(ParticipanteDAO.TELEFONE);
//            int indConheceTema = cursor.getColumnIndex(ParticipanteDAO.CONHECE_TEMA);
//            int indIdUsuario = cursor.getColumnIndex(ParticipanteDAO.ID_USUARIO);
//
//            do {
//                Participante participante = new Participante();
//
//                    participante.setId(cursor.getLong(indId));
//                    participante.setNome(cursor.getString(indNome));
//                    participante.setEmail(cursor.getString(indEmail));
//                    participante.setTelefone(cursor.getString(indTelefone));
//                    participante.setConheceTema(cursor.getInt(indConheceTema) > 0);
//                    participante.setIdUsuario(cursor.getInt(indIdUsuario));
//
//                participantes.add(participante);
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return participantes;
//    }

}



