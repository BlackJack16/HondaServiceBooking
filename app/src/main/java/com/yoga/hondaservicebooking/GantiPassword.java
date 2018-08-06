package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.yoga.hondaservicebooking.model.m_auth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GantiPassword extends AppCompatActivity {
    EditText txtPWDL, txtPWDB, txtPWDK;

    String USR, PWDL, PWDB, PWDK;
    m_auth auth;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = new m_auth(this);

        USR = auth.getAkun().getEMAIL();

        txtPWDL = (EditText) findViewById(R.id.txtPasswordLama);
        txtPWDB = (EditText) findViewById(R.id.txtPasswordBaru);
        txtPWDK = (EditText) findViewById(R.id.txtKPassword);
    }

    public void doGanti(View v){
        PWDL = txtPWDL.getText().toString().trim();
        PWDB = txtPWDB.getText().toString().trim();
        PWDK = txtPWDK.getText().toString().trim();

        if (PWDB.equalsIgnoreCase("") || PWDK.equalsIgnoreCase("")){
            Toast.makeText(getWindow().getContext(), "Password Tidak Boleh Kosong!", Toast.LENGTH_LONG).show();
            txtPWDB.requestFocus();
        }else if (!PWDB.equalsIgnoreCase(PWDK)){
            Toast.makeText(getWindow().getContext(), "Password tidak sama!", Toast.LENGTH_LONG).show();
            txtPWDB.setText("");
            txtPWDK.setText("");
            txtPWDB.requestFocus();
        }else if (PWDB.length() < 6){
            Toast.makeText(GantiPassword.this,"Password minimal 6 digit huruf atau angka!",Toast.LENGTH_LONG).show();
            txtPWDB.setText("");
            txtPWDK.setText("");
            txtPWDB.requestFocus();
        }else{
            new doGanti().execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class doGanti extends AsyncTask<String, String, String> {

        int success = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GantiPassword.this);
            pDialog.setMessage("Sedang mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("usr", USR));
            params.add(new BasicNameValuePair("pwdl",PWDL));
            params.add(new BasicNameValuePair("pwdb",PWDB));
            params.add(new BasicNameValuePair("pwdk",PWDK));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_GANTIPWD,
                        "POST", params);
                success = json.getInt("hasil");
            } catch (JSONException e) {
                success = 99;
            }

            return null;
        }




        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success == 1) {
                if (!pDialog.isShowing()) {
                    Toast.makeText(getWindow().getContext(),  "Password Anda berhasil diganti!", Toast.LENGTH_LONG).show();
                    finish();
                }
            } else if(success == 2) {
                Toast.makeText(getWindow().getContext(), "Password Lama Anda salah!", Toast.LENGTH_LONG).show();
                txtPWDL.requestFocus();
            }else {
                Toast.makeText(getWindow().getContext(), "Login Gagal!\nMohon cek koneksi internet Anda atau hubungi Administrator kami.", Toast.LENGTH_LONG).show();
                txtPWDL.requestFocus();
            }
        }
    }
}
