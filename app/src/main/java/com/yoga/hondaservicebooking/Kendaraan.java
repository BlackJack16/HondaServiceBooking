package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.yoga.hondaservicebooking.adapter.KendaraanAdapter;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;
import com.yoga.hondaservicebooking.model.m_kendaraan;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Kendaraan extends AppCompatActivity {
    RecyclerView lvData;
    LinearLayout layoutEmpty;
    KendaraanAdapter adapter;
    private ArrayList<m_kendaraan> data = new ArrayList<>();
    private m_kendaraan item;

    m_auth auth;
    m_akun akun;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kendaraan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = new m_auth(this);
        akun = auth.getAkun();

        layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
        lvData = (RecyclerView) findViewById(R.id.lvData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new KendaraanAdapter(this, data, new KendaraanAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(m_kendaraan item, int pos) {
                Intent intent = new Intent(Kendaraan.this, KendaraanEdit.class);
                intent.putExtra("id", item.getKendaraan_id());
                intent.putExtra("type_id", item.getType_id());
                intent.putExtra("variant_id", item.getVariant_id());
                intent.putExtra("tahun", item.getTahun());
                intent.putExtra("plat", item.getPlat());
                startActivityForResult(intent,11);
            }
        });
        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(layoutManager);
        lvData.setAdapter(adapter);

        new getDataProcess().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.action_add){
            Intent intent = new Intent(this, KendaraanAdd.class);
            startActivityForResult(intent,10);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==1){
            new getDataProcess().execute();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class getDataProcess extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data.clear();

            pDialog = new ProgressDialog(Kendaraan.this);
            pDialog.setMessage("Mengunduh Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("usr",akun.getEMAIL()));
            JSONObject json = jsonParser.makeHttpRequest(Config.URL_KENDARAAN, "POST", param);
            try {
                JSONArray arr = json.getJSONArray("data");
                for (int i = 0; i<arr.length();i++){
                    JSONObject r = arr.getJSONObject(i);
                    item = new m_kendaraan();
                    item.setKendaraan_id(r.getInt("kendCos_id"));
                    item.setType_id(r.getInt("type_id"));
                    item.setType_nama(r.getString("type_nama"));
                    item.setVariant_id(r.getInt("variant_id"));
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
