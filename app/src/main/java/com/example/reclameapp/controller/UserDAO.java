package com.example.reclameapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.reclameapp.model.DbSQLite;
import com.example.reclameapp.model.User;
import com.example.reclameapp.utils.CryptUtils;

public class UserDAO {
    public void gravar(Context context, User obj) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        ContentValues campos = new ContentValues();
        try {
            conexao = new DbSQLite(context);
            bb = conexao.getWritableDatabase();
            campos.put("doc", obj.getDoc());
            campos.put("password", obj.getPassword());
            bb.insert("user", null, campos); //devolve o código gerado pelo sql lite
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao gravar: " + ex.getMessage());
        }
    }

    public User login(Context context, String doc, String password) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela = null;
        User obj = new User();
        CryptUtils cryptUtils = new CryptUtils();
        String cryptPass = "";
        String decryptPass = "";

        try {
            conexao = new DbSQLite(context);
            bb = conexao.getReadableDatabase();

            tabela = bb.rawQuery("select id,doc,name,password from user where doc=?", new String[]{doc});

            if ((tabela != null) && (tabela.moveToNext())) {

                cryptPass = tabela.getString(3);
                decryptPass = cryptUtils.decryptMD5((cryptPass));

                if (decryptPass.equals(password)) {
                    obj.setId(tabela.getInt(0));
                    obj.setDoc(tabela.getString(1));
                    obj.setName(tabela.getString(2));
                    obj.setPassword(tabela.getString(3));
                } else
                    obj = null;
            } else
                obj = null;

            bb.close();
            return (obj);
        } catch (Exception ex) {
            throw new Exception("Erro ao logar: " + ex.getMessage());
        }
    }
}
