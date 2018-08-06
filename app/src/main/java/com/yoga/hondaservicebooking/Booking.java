package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.firebase.client.Firebase;
import com.yoga.hondaservicebooking.adapter.BookingAdapter;
import com.yoga.hondaservicebooking.lib.Constants;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;
import com.yoga.hondaservicebooking.model.m_booking;
import com.yoga.hondaservicebooking.model.m_kendaraan;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Booking extends AppCompatActivity {
    RecyclerView lvData;
    LinearLayout layoutEmpty;
    BookingAdapter adapter;
    private ArrayList<m_booking> data = new ArrayList<>();
    private m_booking item;

    m_auth auth;
    m_akun akun;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = new m_auth(this);
        akun = auth.getAkun();

        layoutEmpty = (LinearLayout) findViewById(R.id.layoutEmpty);
        lvData = (RecyclerView) findViewById(R.id.lvData);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new BookingAdapter(this, data, new BookingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(m_booking item, int pos) {
                Intent intent = new Intent(Booking.this, BookingEdit.class);
                intent.putExtra("booking_id", item.getBooking_id());
                intent.putExtra("kendaraan_id", item.getKendaraan_id());
                intent.putExtra("tanggal", item.getTgl());
                intent.putExtra("jam", item.getJam());
                intent.putExtra("menit", item.getMenit());
                intent.putExtra("catatan", item.getCatatan());
                startActivityForResult(intent, 11);
            }
        });
        lvData.setHasFixedSize(true);
        lvData.setLayoutManager(layoutManager);
        lvData.setAdapter(adapter);

        Firebase firebase = new Firebase(Constants.FIREBASE_APP + akun.getID() + "/booking/");
        firebase.removeValue();

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
            if (!Launcher.isActivityVisible()){
                Intent intent = new Intent(this, Main.class);
                startActivity(intent);
            }
            finish();
        }else if(item.getItemId() == R.id.action_add){
            Intent intent = new Intent(this, BookingAdd.class);
            startActivityForResult(intent, 10);
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

            pDialog = new ProgressDialog(Booking.this);
            pDialog.setMessage("Mengunduh Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            param.add(new BasicNameValuePair("usr",akun.getEMAIL()));
            JSONObject json = jsonParser.makeHttpRequest(Config.URL_BOOKING, "POST", param);
            try {
                JSONArray arr = json.getJSONArray("data");
                for (int i = 0; i<arr.length();i++){
                    JSONObject r = arr.getJSONObject(i);
                    item = new m_booking();
                    item.setBooking_id(r.getInt("booking_id"));
                    item.setKode(String.valueOf(r.getInt("booking_id")));
                    item.setCatatan(r.getString("booking_catatan"));
                    item.setKet(r.getString("booking_keterangan"));
                    item.setTanggal(r.getString("booking_jadwal"));
                    item.setTgl(r.getString("booking_tgl"));
                    item.setJam(r.getString("jam"));
                    item.setMenit(r.getString("menit"));
                    item.setKendaraan_id(r.getInt("kendCos_id"));
                    item.setType_nama(r.getString("type_nama"));
                    item.setVariant_nama(r.getString("variant_nama"));
                    item.setTahun(r.getString("kendCos_thn"));
                    item.setPlat(r.getString("kendCos_nopol"));
                    item.setStatus(r.getInt("booking_status"));
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
