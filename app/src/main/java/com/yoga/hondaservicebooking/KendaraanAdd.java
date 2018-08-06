package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.yoga.hondaservicebooking.adapter.TypeSpinnerAdapter;
import com.yoga.hondaservicebooking.adapter.VariantSpinnerAdapter;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;
import com.yoga.hondaservicebooking.model.m_kendaraan;
import com.yoga.hondaservicebooking.model.m_type;
import com.yoga.hondaservicebooking.model.m_variant;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KendaraanAdd extends AppCompatActivity {
    ArrayList<m_type> dataType = new ArrayList<>();
    m_type type;
    ArrayList<m_variant> dataVariant = new ArrayList<>();
    m_variant variant;



    Spinner cbType, cbVariant;
    TypeSpinnerAdapter TypeAdapter;
    VariantSpinnerAdapter VariantAdapter;

    EditText txtTahun, txtPlat;
    Button btnSimpan;

    m_kendaraan data = new m_kendaraan();
    m_auth auth;
    m_akun akun;

    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        auth = new m_auth(this);
        akun = auth.getAkun();

        txtTahun = (EditText) findViewById(R.id.txtTahun);
        txtPlat  = (EditText) findViewById(R.id.txtPlat);
        txtPlat.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        cbType = (Spinner) findViewById(R.id.cbType);
        cbVariant = (Spinner) findViewById(R.id.cbVariant);

        new getMasterProcess().execute();

        cbType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataVariant = dataType.get(position).getVariant();

                VariantAdapter = new VariantSpinnerAdapter(KendaraanAdd.this, dataVariant);
                cbVariant.setAdapter(VariantAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSimpan = (Button) findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setType_id(dataType.get(cbType.getSelectedItemPosition()).getType_id());
                data.setVariant_id(dataVariant.get(cbVariant.getSelectedItemPosition()).getVariant_id());
                data.setTahun(txtTahun.getText().toString().trim());
                data.setPlat(txtPlat.getText().toString().trim());

                if (data.getTahun().equalsIgnoreCase("") || data.getPlat().equalsIgnoreCase("")){
                    Toast.makeText(KendaraanAdd.this, "Tahun dan Nomor Plat harus diisi!",Toast.LENGTH_LONG).show();
                    txtTahun.requestFocus();
                }else{
                    new doKirim().execute();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        setResult(0);
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            setResult(0);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getMasterProcess extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataType.clear();
            dataVariant.clear();

            pDialog = new ProgressDialog(KendaraanAdd.this);
            pDialog.setMessage("Mengunduh Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            JSONObject json = jsonParser.makeHttpRequest(Config.URL_MASTER_KENDARAAN, "POST", param);
            try {
                ArrayList<m_variant> tmpVariant;
                JSONArray arr = json.getJSONArray("type");
                for (int i = 0; i<arr.length();i++){
                    JSONObject r = arr.getJSONObject(i);
                    type = new m_type();
                    type.setType_id(r.getInt("type_id"));
                    type.setType_nama(r.getString("type_nama"));
                    JSONArray arr_tmp = r.getJSONArray("variant");
                    tmpVariant = new ArrayList<>();
                    for (int j = 0; j<arr_tmp.length();j++) {
                        JSONObject v = arr_tmp.getJSONObject(j);
                        variant = new m_variant();
                        variant.setVariant_id(v.getInt("variant_id"));
                        variant.setVariant_nama(v.getString("variant_nama"));
                        tmpVariant.add(variant);
                    }
                    type.setVariant(tmpVariant);
                    dataType.add(type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            TypeAdapter = new TypeSpinnerAdapter(KendaraanAdd.this, dataType);
            cbType.setAdapter(TypeAdapter);
        }
    }

    private class doKirim extends AsyncTask<String, String, String> {

        int success = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KendaraanAdd.this);
            pDialog.setMessage("Sedang mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("usr", akun.getEMAIL()));
            params.add(new BasicNameValuePair("variant",String.valueOf(data.getVariant_id())));
            params.add(new BasicNameValuePair("tahun",data.getTahun()));
            params.add(new BasicNameValuePair("plat",data.getPlat()));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_KENDARAAN_ADD,
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

                    Toast.makeText(KendaraanAdd.this, "Kendaraan berhasil ditambah!",Toast.LENGTH_LONG).show();
                    setResult(1);
                    finish();
                }
            } else {
                Toast.makeText(getWindow().getContext(), "Kendaraan gagal ditambah!", Toast.LENGTH_LONG).show();
                txtTahun.requestFocus();
            }
        }
    }
}
