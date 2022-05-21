package com.example.reclameapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.reclameapp.model.DbSQLite;
import com.example.reclameapp.model.Image;
import com.example.reclameapp.model.pgAdmin.Db;
import com.example.reclameapp.model.pgAdmin.DbExecuteUpdate;

public class ImageDAO {
    public void gravar(Context context, Image obj) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        ContentValues campos = new ContentValues();
        long id;
        try {
            conexao = new DbSQLite(context);
            bb = conexao.getWritableDatabase();
            campos.put("photo", obj.getPhoto());
            campos.put("issueId", obj.getIssueId());
            id = bb.insert("image", null, campos); //devolve o código gerado pelo sql lite
            obj.setId((int) id);
            bb.close();
        } catch (Exception ex) {
            throw new Exception("Erro ao gravar: " + ex.getMessage());
        }
    }

    public int gravarPG(Image obj) throws Exception {
        Db bb = null;
        DbExecuteUpdate bbExec = null;
        int resp = -1;
        String erro = "";

        try {
            bb = new Db();
            bb.comando = Db.conexao.prepareStatement(
                    "Insert into imagem(issueId,photo) values(?,?)");
            bb.comando.setInt(1, obj.getIssueId());
            bb.comando.setBytes(2, obj.getPhoto());

            bbExec = new DbExecuteUpdate(bb.comando);
            resp = bbExec.execute().get(); // execute passa os parãmetros para o doInBackground que pode ser nenhum ou muitos  e executa ele
            // já o get captura o valor de retorno do doInBackground.
            bb.desconectar();
            return (resp);
        } catch (Exception ex) {
            if ((bbExec != null) && (bbExec.erro.length() > 0))
                erro += "  " + bbExec.erro;
            throw new Exception("Erro ao gravar: " + ex.getMessage() + erro);
        }
    }

    public Cursor listarImg(Context context, int issueId) throws Exception {
        DbSQLite conexao;
        SQLiteDatabase bb;
        Cursor tabela = null;

        try {
            conexao = new DbSQLite(context);
            bb = conexao.getReadableDatabase();
            tabela = bb.rawQuery("select id,photo from image where issueId=?", new String[]{String.valueOf(issueId)});
            return (tabela);
        } catch (Exception ex) {
            throw new Exception("Erro ao listar: " + ex.getMessage());
        }
    }
}
