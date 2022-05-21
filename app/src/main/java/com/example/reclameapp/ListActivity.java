package com.example.reclameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.reclameapp.controller.ImageDAO;
import com.example.reclameapp.controller.IssueDAO;
import com.example.reclameapp.model.Image;
import com.example.reclameapp.model.Issue;
import com.example.reclameapp.model.Logged;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private String currentId;
    private EditText edtDescription;
    private ListView listItems;

    private static ArrayList<String> lista;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


        edtDescription = (EditText) findViewById(R.id.edtDescriptionEdit);
        listItems = (ListView) findViewById(R.id.listItems);

        lista = new ArrayList<String>();

        list();

        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int pos = i;
                Issue obj;
                IssueDAO dao;

                Cursor tabela;

                try {
                    if ((pos >= 0)) {
                        dao = new IssueDAO();

                        tabela = dao.listar(getBaseContext(), Logged.doc);

                        if (tabela != null) {
                            while (tabela.moveToNext()) {
                                if (tabela.getPosition() == pos) {
                                    currentId = tabela.getString(0);
                                    edtDescription.setText(tabela.getString(2));
                                }
                            }

                        }
                    }
                } catch (Exception ex) {
                    MessageBox(getString(R.string.txtError) + ": " + ex.getMessage(), "");
                }
            }
        });
    }

    public void MessageBox(String texto, String titulo) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton(getString(R.string.txtOk), null);
        dialogo.show();
    }

    public void goBack(View v) {
        try {
            Logged.doc = "";
            Logged.id = -1;
            Logged.name = "";
            finish();
        } catch (Exception ex) {

        }
    }

    public void update(View v) {
        Issue obj = new Issue();
        IssueDAO dao = new IssueDAO();
        try {
            obj.setId(Integer.parseInt(currentId));
            obj.setDescription(edtDescription.getText().toString());
            dao.alterar(getBaseContext(), obj);
            MessageBox(getString(R.string.txtSuccessEdit), getString(R.string.txtEdit));
            obj.setSinc("Offline");
            dao.alterarStatus(getBaseContext(), obj);
            edtDescription.setText("");
            currentId = "";
            list();
        } catch (Exception ex) {
            MessageBox(ex.getMessage(), getString(R.string.txtError));
        }
    }

    public void delete(View v) {
        Issue obj = new Issue();
        IssueDAO dao = new IssueDAO();
        try {
            obj.setId(Integer.parseInt(currentId));
            dao.remover(getBaseContext(), obj);
            MessageBox(getString(R.string.txtSuccessDelete), getString(R.string.txtDelete));
            edtDescription.setText("");
            currentId = "";
            list();
        } catch (Exception ex) {
            MessageBox(ex.getMessage(), getString(R.string.txtError));
        }
    }

    public void openCreate(View v) {
        Intent inte = new Intent(ListActivity.this, CreateActivity.class);
        startActivity(inte);
        finish();
    }

    public void openChangeLang(View v) {
        Intent inte = new Intent(ListActivity.this, LanguageActivity.class);
        startActivity(inte);
        finish();
    }

    public void sync(View v) {
        Image img = new Image();
        ImageDAO imgDao = new ImageDAO();
        Issue pro = new Issue();
        IssueDAO issueDao = new IssueDAO();

        ByteArrayOutputStream foto;

        Cursor tabela, tabelaImg;
        try {
            tabela = issueDao.listar(getBaseContext(), Logged.doc);
            lista.clear();
            while (tabela.moveToNext()) {
                if (tabela != null) {
                    pro.setUser(Logged.doc);
                    pro.setDescription(tabela.getString(2));
                    pro.setDay(Timestamp.valueOf(tabela.getString(3)));
                    pro.setLatitude(Double.parseDouble(tabela.getString(4)));
                    pro.setLongitude(Double.parseDouble(tabela.getString(5)));
                    issueDao.gravarPG(pro);
                    pro.setSinc("Sync");
                    issueDao.alterarStatus(getBaseContext(), pro);

                    //tabelaImg = imgDao.listarImg(getBaseContext(), pro.getId());

                    //foto = new ByteArrayOutputStream();

                    img.setIssueId(pro.getId());
                    //img.setFoto(String.valueOf(tabelaImg.getString(1)));

                    imgDao.gravarPG(img);
                    MessageBox(getString(R.string.txtSuccessSync), getString(R.string.txtSync));
                    list();

                }
                adapter = new ArrayAdapter<>(this, R.layout.row, lista);
                listItems.setAdapter(adapter);

            }

        } catch (Exception ex) {
            MessageBox(ex.getMessage(), getString(R.string.txtError));
        }
    }

    public void list() {
        IssueDAO dao = new IssueDAO();
        Cursor tabela;
        try {
            tabela = dao.listar(getBaseContext(), Logged.doc);
            lista.clear();
            if (tabela != null) {
                while (tabela.moveToNext()) {
                    lista.add("\n" + getString(R.string.txtDescription) + ": " + tabela.getString(2) + "\n" + getString(R.string.txtDayHour) + ": " + tabela.getString(3) + "\n" + getString(R.string.txtStatus) + ": " + tabela.getString(6));
                }
                adapter = new ArrayAdapter<>(this, R.layout.row, lista);
                listItems.setAdapter(adapter);
            }
        } catch (Exception ex) {

        }
    }
}