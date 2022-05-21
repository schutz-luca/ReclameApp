package com.example.reclameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
    }

    public void changeLang(View v) {
        try {
            String currentTag = String.valueOf(v.getTag());

            Locale lang = new Locale(currentTag);
            Locale.setDefault(lang);

            Context context = this;
            Resources res = context.getResources();
            Configuration config = new Configuration(res.getConfiguration());

            config.setLocale(lang);
            res.updateConfiguration(config, res.getDisplayMetrics());

            SharedPreferences.Editor dados = getSharedPreferences("reclameapp", MODE_PRIVATE).edit();
            dados.putString("lang", currentTag);
            dados.commit();

            goBack(v);
        } catch (Exception ex) {
        }

    }

    public void goBack(View v) {
        try {
            Intent inte = new Intent(LanguageActivity.this, ListActivity.class);
            startActivity(inte);
            finish();
        } catch (Exception ex) {

        }
    }
}