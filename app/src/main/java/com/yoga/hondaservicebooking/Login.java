package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    m_auth auth;
    m_akun akun = new m_akun();
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    EditText txtUSR, txtPWD;
    String USR, PWD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = new m_auth(this);
        if (auth.isLogged()){
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
            finish();
        }else {
            setContentView(R.layout.activity_login);

            txtUSR = (EditText) findViewById(R.id.txtUSR);
            txtPWD = (EditText) findViewById(R.id.txtPWD);

        }
    }

    public void showMain(View v){
        USR = txtUSR.getText().toString().trim();
        PWD = txtPWD.getText().toString().trim();

        new doLogin().execute();
    }

    public void showRegister(View v){
        Intent intent = new Intent(this, Register.class);
        startActivityForResult(intent,10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==1){
            finish();
        }
    }

    private class doLogin extends AsyncTask<String, String, String> {

        int success = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Sedang mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("usr", USR));
            params.add(new BasicNameValuePair("pwd",PWD));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_LOGIN,
                        "POST", params);
                success = json.getInt("hasil");
                if (success==1){
                    akun = new m_akun();
                    akun.setID(json.getString("costumer_identitas"));
                    akun.setNAMA(json.getString("costumer_nama"));
                    akun.setTEMPAT(json.getString("costumer_tempat"));
                    akun.setLAHIR(json.getString("costumer_tgl"));
                    akun.setJK(json.getString("costumer_jk"));
                    akun.setALAMAT(json.getString("costumer_alamat"));
                    akun.setAGAMA(json.getString("costumer_agama"));
                    akun.setEMAIL(json.getString("costumer_email"));
                    akun.setTELEPON(json.getString("costumer_telp"));
                }

            } catch (JSONException e) {
                success = 2;
            }

            return null;
        }




        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success == 1) {
                if (!pDialog.isShowing()) {
                    Toast.makeText(getWindow().getContext(), akun.getNAMA() + "Selamat datang di Aplikasi Service Booking Astra Honda Plaju Palembang!", Toast.LENGTH_LONG).show();

                    auth.setPrefAkun(akun);
                    Intent intent = new Intent(Login.this, Main.class);
                    startActivity(intent);
                    finish();
                }
            } else if(success == 2) {
                Toast.makeText(getWindow().getContext(), "Username atau Password Anda Salah!", Toast.LENGTH_LONG).show();
                txtUSR.requestFocus();
            }else {
                Toast.makeText(getWindow().getContext(), "Login Gagal!\nMohon cek koneksi internet Anda atau hubungi Administrator kami.", Toast.LENGTH_LONG).show();
                txtUSR.requestFocus();
            }
        }
    }
}
