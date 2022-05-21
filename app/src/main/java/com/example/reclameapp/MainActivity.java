package com.example.reclameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.reclameapp.controller.UserDAO;
import com.example.reclameapp.model.Logged;
import com.example.reclameapp.model.User;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText edtDoc;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLang();
        setContentView(R.layout.activity_main);

        edtDoc = (EditText) findViewById(R.id.edtDoc);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    public void MessageBox(String texto, String titulo) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton(getString(R.string.txtOk), null);
        dialogo.show();
    }

    public void login(View v) {
        User obj;
        UserDAO dao = new UserDAO();
        try {

            obj = dao.login(getBaseContext(), edtDoc.getText().toString(), edtPassword.getText().toString());
            if (obj != null) {
                Logged.name = obj.getName();
                Logged.doc = obj.getDoc();
                edtDoc.setText("");
                edtPassword.setText("");
                Intent inte = new Intent(MainActivity.this, ListActivity.class);
                startActivity(inte);

            } else {
                MessageBox(getString(R.string.txtFailLogin), getString(R.string.txtLogin));
            }

        } catch (Exception ex) {
            MessageBox(ex.getMessage(), getString(R.string.txtError));
        }
    }

    public void getLang() {
        SharedPreferences dados = getSharedPreferences("reclameapp", MODE_PRIVATE);
        String lingua = dados.getString("lang", "pt");

        Locale lang = new Locale(lingua);
        Locale.setDefault(lang);

        Context context = this;
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());

        config.setLocale(lang);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public void openSignUp(View v) {
        Intent inte = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(inte);
    }
}