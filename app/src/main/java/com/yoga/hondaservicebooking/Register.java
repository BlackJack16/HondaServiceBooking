package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.yoga.hondaservicebooking.lib.DatePickerFragment;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    DatePickerFragment datePicker;
    EditText txtLAHIR, txtID, txtNAMA, txtALAMAT, txtTEMPAT, txtEMAIL, txtTELP, txtPWD;
    RadioGroup rgJK;
    Spinner cbAgama;
    Button btnDaftar;
    CheckBox chkSetuju;

    m_akun data = new m_akun();
    m_auth auth;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = new m_auth(this);

        txtLAHIR = (EditText) findViewById(R.id.txtLahir);
        txtID = (EditText) findViewById(R.id.txtNo);
        txtNAMA = (EditText) findViewById(R.id.txtNama);
        txtALAMAT = (EditText) findViewById(R.id.txtAlamat);
        txtTEMPAT = (EditText) findViewById(R.id.txtTempatLahir);
        txtEMAIL = (EditText) findViewById(R.id.txtEmail);
        txtTELP = (EditText) findViewById(R.id.txtTelepon);
        txtPWD = (EditText) findViewById(R.id.txtPassword);

        rgJK = (RadioGroup) findViewById(R.id.rgJK);
        cbAgama = (Spinner) findViewById(R.id.cbAgama);
        chkSetuju = (CheckBox) findViewById(R.id.chkSetuju);

        txtLAHIR.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    showPicker(v);
                }
            }
        });

        btnDaftar = (Button) findViewById(R.id.btnDaftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setID(txtID.getText().toString().trim());
                data.setNAMA(txtNAMA.getText().toString().trim());
                data.setALAMAT(txtALAMAT.getText().toString().trim());
                data.setTEMPAT(txtTEMPAT.getText().toString().trim());
                data.setLAHIR(txtLAHIR.getText().toString().trim());
                data.setEMAIL(txtEMAIL.getText().toString().trim());
                data.setTELEPON(txtTELP.getText().toString().trim());
                data.setPWD(txtPWD.getText().toString().trim());

                data.setAGAMA(cbAgama.getSelectedItem().toString().trim());
                if (rgJK.getCheckedRadioButtonId() == R.id.rbL){
                    data.setJK("Laki - Laki");
                }else{
                    data.setJK("Perempuan");
                }

                if(chkSetuju.isChecked()){
                    if (checkForm()){
                        new doDaftar().execute();
                    }
                }else{
                    Toast.makeText(Register.this,"Tolong centang persetujuan formulir!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkForm(){
        if (data.getID().equalsIgnoreCase("") ||  data.getNAMA().equalsIgnoreCase("") ||
                data.getALAMAT().equalsIgnoreCase("") ||  data.getTEMPAT().equalsIgnoreCase("") ||
                data.getLAHIR().equalsIgnoreCase("") ||  data.getAGAMA().equalsIgnoreCase("") ||
                data.getEMAIL().equalsIgnoreCase("") ||  data.getPWD().equalsIgnoreCase("") ||
                data.getTELEPON().equalsIgnoreCase("") ||  data.getJK().equalsIgnoreCase("")
                ){
            Toast.makeText(Register.this,"Semua data harus diisi!",Toast.LENGTH_LONG).show();
            txtID.requestFocus();
            return false;
        }else if(data.getPWD().length() < 6){
            Toast.makeText(Register.this,"Password minimal 6 digit huruf atau angka!",Toast.LENGTH_LONG).show();
            txtPWD.requestFocus();
        }

        return true;
    }

    public void showPicker(View v){
        datePicker = new DatePickerFragment(txtLAHIR);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            setResult(0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        super.onBackPressed();
    }

    private class doDaftar extends AsyncTask<String, String, String> {

        int success = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Sedang mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", data.getID()));
            params.add(new BasicNameValuePair("nama", data.getNAMA()));
            params.add(new BasicNameValuePair("alamat", data.getALAMAT()));
            params.add(new BasicNameValuePair("tempat", data.getTEMPAT()));
            params.add(new BasicNameValuePair("lahir", data.getLAHIR()));
            params.add(new BasicNameValuePair("jk", data.getJK()));
            params.add(new BasicNameValuePair("agama", data.getAGAMA()));
            params.add(new BasicNameValuePair("email", data.getEMAIL()));
            params.add(new BasicNameValuePair("telp", data.getTELEPON()));
            params.add(new BasicNameValuePair("pwd", data.getPWD()));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_REGISTRASI,
                        "POST", params);
                success = json.getInt("hasil");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success == 1) {
                if (!pDialog.isShowing()) {
                    Toast.makeText(getWindow().getContext(), data.getNAMA() + "Selamat datang di Aplikasi Service Booking Astra Honda Plaju Palembang!", Toast.LENGTH_LONG).show();

                    auth.setPrefAkun(data);
                    Intent intent = new Intent(Register.this, Main.class);
                    startActivity(intent);
                    setResult(1);
                    finish();
                }
            } else if (success == 2){
                Toast.makeText(getWindow().getContext(), "Maaf Alamat Email sudah terdaftar, Coba gunakan alamat email lain.", Toast.LENGTH_LONG).show();
                txtEMAIL.requestFocus();
            }else {
                Toast.makeText(getWindow().getContext(), "Maaf Akun Anda gagal dibuat, Mohon cek koneksi Internet Anda atau Hubungi Administrator", Toast.LENGTH_LONG).show();
            }
        }
    }
}
