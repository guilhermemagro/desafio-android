package com.everis.guilherme.desafio1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.everis.guilherme.desafio1.Domain.Evento;
import com.everis.guilherme.desafio1.Infra.HelperDB;

import java.util.ArrayList;

public class EventoDAO {

    private static final String ID = "_id";
    private static final String NOME = "nome";
    private static final String LOCAL = "local";
    private static final String CIDADE = "cidade";
    private static final String DATA = "data";
    private static final String HORARIO = "horario";
    private static final String VAGAS = "vagas";
    private static final String IMAGEM = "imagem";
    private static final String ID_CRIADOR = "id_criador";
    private static final String ATIVO = "ativo";

    public static final String TABLE_EVENTO = "evento";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_EVENTO + "( "
            + ID + " integer primary key autoincrement, "
            + NOME + " text not null, "
            + LOCAL + " text not null, "
            + CIDADE + " text not null, "
            + DATA + " text not null, "
            + HORARIO + " text not null, "
            + VAGAS + " integer not null, "
            + IMAGEM + " integer not null, "
            + ID_CRIADOR + " integer not null, "
            + ATIVO + " text not null);";

    private HelperDB helperDB;

    private String[] allColumns = {
            EventoDAO.ID,
            EventoDAO.NOME,
            EventoDAO.LOCAL,
            EventoDAO.CIDADE,
            EventoDAO.DATA,
            EventoDAO.HORARIO,
            EventoDAO.VAGAS,
            EventoDAO.IMAGEM,
            EventoDAO.ID_CRIADOR,
            EventoDAO.ATIVO
    };

    public EventoDAO(Context context){
        helperDB = new HelperDB(context);
    }

    public boolean salvar(Evento evento){
        SQLiteDatabase database = helperDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(evento.getId() != 0){
            values.put(EventoDAO.ID, evento.getId());
        }
        values.put(EventoDAO.NOME, evento.getNome());
        values.put(EventoDAO.LOCAL, evento.getLocal());
        values.put(EventoDAO.CIDADE, evento.getCidade());
        values.put(EventoDAO.DATA, evento.getData());
        values.put(EventoDAO.HORARIO, evento.getHorario());
        values.put(EventoDAO.VAGAS, evento.getVagas());
        values.put(EventoDAO.IMAGEM, evento.getImagem());
        values.put(EventoDAO.ID_CRIADOR, evento.getIdCriador());
        values.put(EventoDAO.ATIVO, evento.isAtivo());

        long insertId = database.insert(EventoDAO.TABLE_EVENTO, null, values);

        return insertId > 0;
    }

    public boolean atualizarEvento(Evento evento){
        SQLiteDatabase database = helperDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventoDAO.ID, evento.getId());
        values.put(EventoDAO.NOME, evento.getNome());
        values.put(EventoDAO.LOCAL, evento.getLocal());
        values.put(EventoDAO.CIDADE, evento.getCidade());
        values.put(EventoDAO.DATA, evento.getData());
        values.put(EventoDAO.HORARIO, evento.getHorario());
        values.put(EventoDAO.VAGAS, evento.getVagas());
        values.put(EventoDAO.IMAGEM, evento.getImagem());
        values.put(EventoDAO.ID_CRIADOR, evento.getIdCriador());
        values.put(EventoDAO.ATIVO, evento.isAtivo());

        int insertId = database.update(EventoDAO.TABLE_EVENTO, values, ID + "=" + evento.getId(),
                null);

        return insertId > 0 ? Boolean.TRUE : Boolean.FALSE;
    }

    public boolean inativarEvento(Evento evento){
        SQLiteDatabase database = helperDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EventoDAO.ATIVO, false);

        int insertId = database.update(EventoDAO.TABLE_EVENTO, values, ID + "=" + evento.getId(),
                null);

        return insertId > 0;
    }

    public ArrayList<Evento> buscarTodos(){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(EventoDAO.TABLE_EVENTO, allColumns, ATIVO + "=1", null,null,null,null);
        ArrayList<Evento> eventos = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int indId = cursor.getColumnIndex(EventoDAO.ID);
            int indNome = cursor.getColumnIndex(EventoDAO.NOME);
            int indLocal = cursor.getColumnIndex(EventoDAO.LOCAL);
            int indCidade = cursor.getColumnIndex(EventoDAO.CIDADE);
            int indData = cursor.getColumnIndex(EventoDAO.DATA);
            int indHorario = cursor.getColumnIndex(EventoDAO.HORARIO);
            int indVagas = cursor.getColumnIndex(EventoDAO.VAGAS);
            int indImagem = cursor.getColumnIndex(EventoDAO.IMAGEM);
            int indIdCriador = cursor.getColumnIndex(EventoDAO.ID_CRIADOR);
            int indAtivo = cursor.getColumnIndex(EventoDAO.ATIVO);

            do {
                Evento evento = new Evento();
                evento.setId(cursor.getLong(indId));
                evento.setNome(cursor.getString(indNome));
                evento.setLocal(cursor.getString(indLocal));
                evento.setCidade(cursor.getString(indCidade));
                evento.setData(cursor.getString(indData));
                evento.setHorario(cursor.getString(indHorario));
                evento.setVagas(cursor.getInt(indVagas));
                evento.setImagem(cursor.getInt(indImagem));
                evento.setIdCriador(cursor.getLong(indIdCriador));
                evento.setAtivo(cursor.getInt(indAtivo) > 0);
                eventos.add(evento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return eventos;
    }

//        public Evento buscarPorId(long id){
//        SQLiteDatabase database = helperDB.getReadableDatabase();
//        Cursor cursor = database.query(EventoDAO.TABLE_EVENTO, allColumns, EventoDAO.ID + "='"
//                + id + "'", null, null,null,null);
//        cursor.moveToFirst();
//        return cursorToObject(cursor);
//    }

//    private Evento cursorToObject(Cursor cursor){
//        int indId = cursor.getColumnIndex(EventoDAO.ID);
//        int indNome = cursor.getColumnIndex(EventoDAO.NOME);
//        int indLocal = cursor.getColumnIndex(EventoDAO.LOCAL);
//        int indCidade = cursor.getColumnIndex(EventoDAO.CIDADE);
//        int indData = cursor.getColumnIndex(EventoDAO.DATA);
//        int indHorario = cursor.getColumnIndex(EventoDAO.HORARIO);
//        int indVagas = cursor.getColumnIndex(EventoDAO.VAGAS);
//        int indImagem = cursor.getColumnIndex(EventoDAO.IMAGEM);
//        int indIdCriador = cursor.getColumnIndex(EventoDAO.ID_CRIADOR);
//        int indAtivo = cursor.getColumnIndex(EventoDAO.ATIVO);
//
//        Evento evento = new Evento();
//        evento.setId(cursor.getLong(indId));
//        evento.setNome(cursor.getString(indNome));
//        evento.setLocal(cursor.getString(indLocal));
//        evento.setCidade(cursor.getString(indCidade));
//        evento.setData(cursor.getString(indData));
//        evento.setHorario(cursor.getString(indHorario));
//        evento.setVagas(cursor.getInt(indVagas));
//        evento.setImagem(cursor.getInt(indImagem));
//        evento.setIdCriador(cursor.getLong(indIdCriador));
//        evento.setAtivo(cursor.getInt(indAtivo) > 0);
//        return evento;
//    }

}