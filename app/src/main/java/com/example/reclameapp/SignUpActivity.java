package com.example.reclameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.reclameapp.controller.UserDAO;
import com.example.reclameapp.model.User;
import com.example.reclameapp.utils.CryptUtils;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtDoc;
    private EditText edtPassword;
    private EditText edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtDoc = (EditText) findViewById(R.id.edtDoc);
        edtPassword = (EditText) findViewById(R.id.edtPasswordCreate);
        edtName = (EditText) findViewById(R.id.edtName);
    }

    public void MessageBox(String texto, String titulo) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle(titulo);
        dialogo.setMessage(texto);
        dialogo.setNeutralButton(getString(R.string.txtOk), null);
        dialogo.show();
    }

    public void signUp(View v) {
        User obj = new User();
        UserDAO dao = new UserDAO();
        CryptUtils crypt = new CryptUtils();
        try {
            obj.setDoc(edtDoc.getText().toString().trim());
            obj.setPassword(crypt.cryptMD5(edtPassword.getText().toString().trim()));
            obj.setName(edtName.getText().toString().trim());
            dao.gravar(getBaseContext(), obj);
            MessageBox(getString(R.string.txtSuccessRegister), getString(R.string.txtSignUp));

            edtDoc.setText("");
            edtName.setText("");
            edtPassword.setText("");

        } catch (Exception ex) {

        }
    }

    public void goBack(View v) {
        try {
            finish();
        } catch (Exception ex) {

        }
    }
}