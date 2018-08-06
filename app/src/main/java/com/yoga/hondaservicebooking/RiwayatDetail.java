package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.yoga.hondaservicebooking.adapter.BiayaAdapter;
import com.yoga.hondaservicebooking.lib.Constants;
import com.yoga.hondaservicebooking.lib.FillListView;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;
import com.yoga.hondaservicebooking.model.m_biaya;
import com.yoga.hondaservicebooking.model.m_service;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RiwayatDetail extends AppCompatActivity {
    BiayaAdapter partAdapter;
    BiayaAdapter jasaAdapter;
    ArrayList<m_biaya> dataPart = new ArrayList<>();
    ArrayList<m_biaya> dataJasa = new ArrayList<>();
    m_biaya item;
    FillListView lvPart, lvJasa;

    m_service data = new m_service();
    m_auth auth;
    m_akun akun;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    TextView lblKode, lblTanggal, lblCatatan, lblTahun, lblVariant, lblPlat, lblTotalPart, lblTotalJasa, lblTotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = new m_auth(this);
        akun = auth.getAkun();

        data.setService_id(getIntent().getIntExtra("id",0));

        lblKode     = (TextView) findViewById(R.id.lblKode);
        lblTanggal     = (TextView) findViewById(R.id.lblTanggal);
        lblCatatan     = (TextView) findViewById(R.id.lblCatatan);
        lblTahun     = (TextView) findViewById(R.id.lblTahun);
        lblVariant     = (TextView) findViewById(R.id.lblVariant);
        lblPlat     = (TextView) findViewById(R.id.lblPlat);
        lblTotalPart     = (TextView) findViewById(R.id.lblTotalPart);
        lblTotalJasa     = (TextView) findViewById(R.id.lblTotalJasa);
        lblTotal     = (TextView) findViewById(R.id.lblTotal);

        lvPart      = (FillListView) findViewById(R.id.lvPart);
        lvJasa      = (FillListView) findViewById(R.id.lvJasa);

        partAdapter = new BiayaAdapter(this, dataPart);
        jasaAdapter    = new BiayaAdapter(this, dataJasa);

        lvPart.setAdapter(partAdapter);
        lvJasa.setAdapter(jasaAdapter);

        Firebase firebase = new Firebase(Constants.FIREBASE_APP + akun.getID() + "/service/");
        firebase.child(String.valueOf(data.getService_id())).removeValue();

        new getDataProcess().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            if (!Launcher.isActivityVisible()){
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!Launcher.isActivityVisible()){
            Intent intent = new Intent(this, Main.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    private class getDataProcess extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(RiwayatDetail.this);
            pDialog.setMessage("Mengunduh Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("id",String.valueOf(data.getService_id())));
            param.add(new BasicNameValuePair("usr",akun.getEMAIL()));
            JSONObject json = jsonParser.makeHttpRequest(Config.URL_RIWAYAT, "POST", param);
            try {

                JSONObject r = json.getJSONObject("data");
                data.setKode(String.valueOf(r.getInt("booking_id")));
                data.setTanggal(r.getString("booking_jadwal"));
                data.setCatatan(r.getString("booking_catatan"));
                data.setType_nama(r.getString("type_nama"));
                data.setVariant_nama(r.getString("variant_nama"));
                data.setTahun(r.getString("kendCos_thn"));
                data.setPlat(r.getString("kendCos_nopol"));
                data.setTotal(r.getString("total"));
                data.setTotalPart(r.getString("total_part"));
                data.setTotalService(r.getString("total_service"));
                JSONArray arr_part = r.getJSONArray("sparepart");
                JSONArray arr_service = r.getJSONArray("service");

                dataPart.clear();
                for (int i = 0; i<arr_part.length();i++){
                    JSONObject p = arr_part.getJSONObject(i);
                    item = new m_biaya();
                    item.setNama(p.getString("sparepart_nama"));
                    item.setBiaya(p.getInt("sparepart_harga"));
                    dataPart.add(item);
                }

                dataJasa.clear();
                for (int i = 0; i<arr_service.length();i++){
                    JSONObject p = arr_service.getJSONObject(i);
                    item = new m_biaya();
                    item.setNama(p.getString("service_nama"));
                    item.setBiaya(p.getInt("service_harga"));
                    dataJasa.add(item);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();

            lblKode.setText("#"+data.getKode());
            lblTanggal.setText(data.getTanggal());
            lblVariant.setText(data.getType_nama() + " " + data.getVariant_nama());
            lblTahun.setText(data.getTahun());
            lblPlat.setText(data.getPlat());
            lblCatatan.setText(data.getCatatan());
            lblTotalPart.setText("RP "+ data.getTotalPart());
            lblTotalJasa.setText("RP "+ data.getTotalService());
            lblTotal.setText("RP "+ data.getTotal());

            jasaAdapter.notifyDataSetChanged();
            partAdapter.notifyDataSetChanged();
        }
    }
}
