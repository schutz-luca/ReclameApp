package com.example.reclameapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.reclameapp.controller.ImageDAO;
import com.example.reclameapp.controller.IssueDAO;
import com.example.reclameapp.model.Image;
import com.example.reclameapp.model.Issue;
import com.example.reclameapp.model.Logged;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    private ImageView imgfoto;
    private Bitmap foto;
    private Cursor tabela;
    private EditText descr;
    private LocationManager mgr;
    private Location bestLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        imgfoto = (ImageView) findViewById(R.id.imgView);
        descr = (EditText) findViewById(R.id.edtDescriptionEdit);
        mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        localizar();
    }

    public void capturaFoto(View v) {
        Intent appFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (appFoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(appFoto, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Bundle extras = data.getExtras();
            foto = (Bitmap) extras.get("data");
            imgfoto.setVisibility(View.VISIBLE);
            imgfoto.setImageBitmap(foto);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private Location localizar() {
        mgr = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> provedores;

        Location local;

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);//no lugar do um basta um maior que zero
        }

        provedores = mgr.getProviders(true);
        for (String provider : provedores) {
            local = mgr.getLastKnownLocation(provider);
            if (local != null) {
                if ((bestLocation == null) || (local.getAccuracy() < bestLocation.getAccuracy())) {
                    bestLocation = local;
                }
            }
        }
        return bestLocation;
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
            Intent inte = new Intent(CreateActivity.this, ListActivity.class);
            startActivity(inte);
            finish();
        } catch (Exception ex) {

        }
    }

    public void gravar(View v) {
        ImageDAO daoImg;
        Image objImg;
        Issue objPro;
        IssueDAO daoPro;

        ByteArrayOutputStream vetorByte;
        try {
            localizar();

            objImg = new Image();
            daoImg = new ImageDAO();

            objPro = new Issue();
            daoPro = new IssueDAO();

            objPro.setDescription(descr.getText().toString().trim());
            objPro.setLatitude(bestLocation.getLatitude());
            objPro.setLongitude(bestLocation.getLongitude());
            objPro.setUser(Logged.doc);
            objPro.setSinc("Offline");
            daoPro.gravar(getBaseContext(), objPro);

            vetorByte = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.PNG, 0, vetorByte);//public boolean compress (Bitmap.CompressFormat format, int quality (0-100)maxima qualidade, OutputStream stream)
            objImg.setPhoto(vetorByte.toByteArray());
            objImg.setIssueId(objPro.getId());
            daoImg.gravar(getBaseContext(), objImg);

            MessageBox(getString(R.string.txtSuccessCreate), getString(R.string.txtComplaints));
            descr.setText("");


        } catch (Exception ex) {
            Toast.makeText(this, getString(R.string.txtError) + ": " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}