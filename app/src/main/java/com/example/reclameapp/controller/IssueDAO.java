package com.example.reclameapp.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.reclameapp.model.DbSQLite;
import com.example.reclameapp.model.Issue;
import com.example.reclameapp.model.pgAdmin.Db;
import com.example.reclameapp.model.pgAdmin.DbExecuteUpdate;

public class IssueDAO {
    public void gravar(Context context, Issue obj) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        try {
            conexao = new DbSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("insert into issue(user,description,latitude,longitude,sinc) values(?,?,?,?,?)", new String[]{obj.getUser(), obj.getDescription(), String.valueOf(obj.getLatitude()), String.valueOf(obj.getLongitude()), obj.getSinc()});

            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao gravar: " + ex.getMessage());
        }
    }

    public int gravarPG(Issue obj) throws Exception {
        Db bb = null;
        DbExecuteUpdate bbExec = null;
        int resp = -1;
        String erro = "";

        try {
            bb = new Db();
            bb.comando = Db.conexao.prepareStatement(
                    "Insert into issue(user,description,latitude,longitude, day) values(?,?,?,?,?)");
            bb.comando.setString(1, obj.getUser());
            bb.comando.setString(2, obj.getDescription());
            bb.comando.setDouble(3, obj.getLatitude());
            bb.comando.setDouble(4, obj.getLongitude());
            bb.comando.setTimestamp(5, obj.getDay());

            bbExec = new DbExecuteUpdate(bb.comando);
            resp = bbExec.execute().get(); // execute passa os parãmetros para o doInBackground que pode ser nenhum ou muitos  e executa ele
            // já o get captura o valor de retorno do doInBackground.
            bb.desconectar();
            return (resp);
        } catch (Exception ex) {
            if ((bbExec != null) && (bbExec.erro.length() > 0))
                erro += "  " + bbExec.erro;
            throw new Exception("Erro ao gravar P: " + ex.getMessage() + erro);
        }
    }

    public void alterar(Context context, Issue obj) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        try {
            conexao = new DbSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("update issue set description=? where id=?", new String[]{obj.getDescription(), String.valueOf(obj.getId())});

            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao alterar: " + ex.getMessage());
        }
    }

    public void alterarStatus(Context context, Issue obj) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        try {
            conexao = new DbSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("update issue set sinc=? where id=?", new String[]{obj.getSinc(), String.valueOf(obj.getId())});

            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao alterar: " + ex.getMessage());
        }
    }

    public void remover(Context context, Issue obj) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        try {
            conexao = new DbSQLite(context);
            bb = conexao.getWritableDatabase();
            bb.execSQL("Delete from issue where id= ?", new String[]{String.valueOf(obj.getId())});
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao remover: " + ex.getMessage());
        }
    }

    public Cursor listar(Context context, String doc) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela = null;

        try {
            conexao = new DbSQLite(context);
            bb = conexao.getReadableDatabase();
            tabela = bb.rawQuery("select id,user,description,day,latitude,longitude,sinc from issue where user=?", new String[]{doc});
            return (tabela);
        } catch (Exception ex) {
            throw new Exception("Erro ao listar: " + ex.getMessage());
        }
    }
}
