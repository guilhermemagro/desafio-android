package com.everis.guilherme.desafio1.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.everis.guilherme.desafio1.Infra.HelperDB;
import com.everis.guilherme.desafio1.Domain.Usuario;

public class UsuarioDAO {
    public static final String TABLE_USUARIO = "usuario";

    private static final String ID = "_id";
    private static final String MATRICULA = "matricula";
    private static final String EMAIL = "email";
    private static final String SENHA = "senha";
    private static final String ADMIN = "admin";

    public static final String DATABASE_CREATE = "create table "
            + TABLE_USUARIO + "( "
            + ID + " integer primary key autoincrement, "
            + MATRICULA + " integer not null, "
            + EMAIL + " text not null, "
            + SENHA + " text not null, "
            + ADMIN + " text not null);";

    private HelperDB helperDB;

    private String[] allColumns = {
            UsuarioDAO.ID,
            UsuarioDAO.MATRICULA,
            UsuarioDAO.EMAIL,
            UsuarioDAO.SENHA,
            UsuarioDAO.ADMIN
    };

    public UsuarioDAO(Context context){
        helperDB = new HelperDB(context);
    }

    public boolean salvar(Usuario usuario){
        SQLiteDatabase database = helperDB.getWritableDatabase();
        ContentValues values = new ContentValues();
        if(usuario.getId() != 0){
            values.put(UsuarioDAO.ID, usuario.getId());
        }
        values.put(UsuarioDAO.MATRICULA, usuario.getMatricula());
        values.put(UsuarioDAO.EMAIL, usuario.getEmail());
        values.put(UsuarioDAO.SENHA, usuario.getSenha());
        values.put(UsuarioDAO.ADMIN, usuario.isAdmin());
        long insertId = database.insert(UsuarioDAO.TABLE_USUARIO, null, values);
        return insertId > 0;
    }

    public boolean verificarSeUsuarioExiste(String email){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(UsuarioDAO.TABLE_USUARIO, allColumns, UsuarioDAO.EMAIL + "='"
                + email + "'", null, null,null,null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Usuario buscarPorEmail(String email){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(UsuarioDAO.TABLE_USUARIO, allColumns, UsuarioDAO.EMAIL + "='"
                + email + "'", null, null,null,null);
        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    public Usuario buscarPorId(long id){
        SQLiteDatabase database = helperDB.getReadableDatabase();
        Cursor cursor = database.query(UsuarioDAO.TABLE_USUARIO, allColumns, UsuarioDAO.ID + "='"
                + id + "'", null, null,null,null);
        cursor.moveToFirst();
        return cursorToObject(cursor);
    }

    private Usuario cursorToObject(Cursor cursor){
        int indId = cursor.getColumnIndex(UsuarioDAO.ID);
        int indMatricula = cursor.getColumnIndex(UsuarioDAO.MATRICULA);
        int indEmail = cursor.getColumnIndex(UsuarioDAO.EMAIL);
        int indSenha = cursor.getColumnIndex(UsuarioDAO.SENHA);
        int indAdmin = cursor.getColumnIndex(UsuarioDAO.ADMIN);

        Usuario usuario = new Usuario();
        usuario.setId(cursor.getLong(indId));
        usuario.setMatricula(cursor.getInt(indMatricula));
        usuario.setEmail(cursor.getString(indEmail));
        usuario.setSenha(cursor.getString(indSenha));
        usuario.setAdmin(cursor.getInt(indAdmin) > 0);
        return usuario;
    }
}
