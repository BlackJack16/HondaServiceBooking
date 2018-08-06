package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
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

public class Saran extends AppCompatActivity {
    EditText txtIsi;
    String SARAN;

    m_auth auth;
    m_akun akun;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saran);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = new m_auth(this);
        akun = auth.getAkun();

        txtIsi = (EditText) findViewById(R.id.txtIsi);
    }

    public void doKirim(View v){
        SARAN = txtIsi.getText().toString().trim();

        if (SARAN.equalsIgnoreCase("")){
            Toast.makeText(this, "Isi Saran dan Kritik belum diisi !", Toast.LENGTH_LONG).show();
            txtIsi.requestFocus();
        }else{
            new doKirim().execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class doKirim extends AsyncTask<String, String, String> {

        int success = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Saran.this);
            pDialog.setMessage("Sedang mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("usr", akun.getEMAIL()));
            params.add(new BasicNameValuePair("isi", SARAN));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_SARAN,
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
                    Toast.makeText(getWindow().getContext(), "Terima Kasih\nSaran dan Kritik Anda berhasil kami terima!", Toast.LENGTH_LONG).show();
                    txtIsi.setText("");
                }
            } else {
                Toast.makeText(getWindow().getContext(), "Maaf, Saran dan Kritik Anda gagal dikirim!", Toast.LENGTH_LONG).show();
            }
            txtIsi.requestFocus();
        }
    }
}
