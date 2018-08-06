package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.client.Firebase;
import com.yoga.hondaservicebooking.adapter.ServiceAdapter;
import com.yoga.hondaservicebooking.lib.Constants;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;
import com.yoga.hondaservicebooking.model.m_service;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Riwayat extends AppCompatActivity {
    RecyclerView lvData;
    LinearLayout layoutEmpty;
    ServiceAdapter adapter;
    private ArrayList<m_service> data = new ArrayList<>();
    private m_service item;

    m_auth auth;
    m_akun akun;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = new m_auth(this);
        akun = auth.getAkun();

        layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
        lvData = (RecyclerView) findViewById(R.id.lvData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new ServiceAdapter(this, data, new ServiceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(m_service item, int pos) {
                Intent intent = new Intent(Riwayat.this, RiwayatDetail.class);
                intent.putExtra("id", item.getService_id());
                startActivity(intent);
            }
        });
        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(layoutManager);
        lvData.setAdapter(adapter);

        Firebase firebase = new Firebase(Constants.FIREBASE_APP + akun.getID() + "/service/");
        firebase.removeValue();

        new getDataProcess().execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class getDataProcess extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data.clear();

            pDialog = new ProgressDialog(Riwayat.this);
            pDialog.setMessage("Mengunduh Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("usr",akun.getEMAIL()));
            JSONObject json = jsonParser.makeHttpRequest(Config.URL_RIWAYAT, "POST", param);
            try {
                JSONArray arr = json.getJSONArray("data");
                for (int i = 0; i<arr.length();i++){
                    JSONObject r = arr.getJSONObject(i);
                    item = new m_service();
                    item.setService_id(r.getInt("trs_id"));
                    item.setKode(String.valueOf(r.getInt("booking_id")));
                    item.setTotal(r.getString("total"));
                    item.setTanggal(r.getString("booking_jadwal"));
                    item.setType_nama(r.getString("type_nama"));
                    item.setVariant_nama(r.getString("variant_nama"));
                    item.setTahun(r.getString("kendCos_thn"));
                    item.setPlat(r.getString("kendCos_nopol"));
                    data.add(item);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();

            adapter.notifyDataSetChanged();

            if (data.size() < 1){
                lvData.setVisibility(View.GONE);
                layoutEmpty.setVisibility(View.VISIBLE);
            }else{
                lvData.setVisibility(View.VISIBLE);
                layoutEmpty.setVisibility(View.GONE);
            }
        }
    }
}
