package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.firebase.client.Firebase;
import com.yoga.hondaservicebooking.adapter.EventAdapter;
import com.yoga.hondaservicebooking.lib.Constants;
import com.yoga.hondaservicebooking.lib.CustomTextSliderView;
import com.yoga.hondaservicebooking.lib.FillListView;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;
import com.yoga.hondaservicebooking.model.m_event;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,BaseSliderView.OnSliderClickListener {
    HashMap<String,Integer> dataImage;
    ArrayList<m_event> dataEvent = new ArrayList<>();
    m_event event;
    EventAdapter eventAdapter;
    FillListView lvEvent;
    TextView lblEventEmpty;
    ScrollView mScrollView;

    Menu navbar;
    m_auth auth;
    m_akun akun;
    TextView lblNama, lblEmail;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Launcher.activityResumed();

        auth = new m_auth(this);
        akun = auth.getAkun();


        startService(new Intent(this, NotificationListener.class));
        Firebase firebase = new Firebase(Constants.FIREBASE_APP + akun.getID() + "/event/");
        firebase.removeValue();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navbar = navigationView.getMenu();


        lblNama = (TextView) navigationView.getHeaderView(0).findViewById(R.id.lblNama);
        lblEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.lblEmail);
        lblNama.setText(akun.getNAMA());
        lblEmail.setText(akun.getEMAIL());

        dataImage = new HashMap<>();
        dataImage.put("Honda Genuine Parts siap menjaga performa dan kualitas motor Honda. #OneHeart",R.drawable.bg1);
        dataImage.put("#SalamSatuHati",R.drawable.bg2);

        showSlideshow();

        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        lblEventEmpty = (TextView) findViewById(R.id.lblEventEmpty);
        lvEvent = (FillListView) findViewById(R.id.lvEvent);
        eventAdapter = new EventAdapter(this, dataEvent, new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(m_event item, int position) {
                Intent intent = new Intent(Main.this, Event.class);
                intent.putExtra("id", String.valueOf(item.getID()));
                intent.putExtra("judul", item.getJudul());
                startActivity(intent);
            }
        });
        lvEvent.setAdapter(eventAdapter);

        new getDataProcess().execute();

    }

    public void showAddBooking(View v){
        Intent intent = new Intent(this, BookingAdd.class);
        startActivity(intent);
    }

    private void showSlideshow(){
        SliderLayout gallery = (SliderLayout) findViewById(R.id.slider);

        for(String name : dataImage.keySet()){
            CustomTextSliderView textSliderView = new CustomTextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(dataImage.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            gallery.addSlider(textSliderView);
        }
        gallery.setPresetTransformer(SliderLayout.Transformer.Default);
        gallery.setCustomIndicator((PagerIndicator) findViewById(R.id.indicator));
        gallery.setDuration(9000);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Launcher.activityPaused();
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Launcher.activityResumed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_kendaraan) {
            intent = new Intent(this, Kendaraan.class);
            startActivity(intent);
        } else if (id == R.id.nav_booking) {
            intent = new Intent(this, Booking.class);
            startActivity(intent);
        } else if (id == R.id.nav_riwayat) {
            intent = new Intent(this, Riwayat.class);
            startActivity(intent);
        } else if (id == R.id.nav_pengaturan) {
            intent = new Intent(this, Pengaturan.class);
            startActivityForResult(intent,99);
        } else if (id == R.id.nav_saran) {
            intent = new Intent(this, Saran.class);
            startActivity(intent);
        } else if (id == R.id.nav_tentang) {
            intent = new Intent(this, Tentang.class);
            startActivity(intent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 99){
            if (resultCode==1){
                finish();
                startActivity(new Intent(this, Login.class));
            }
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }




    private class getDataProcess extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataEvent.clear();

            pDialog = new ProgressDialog(Main.this);
            pDialog.setMessage("Mengunduh Data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<>();
            JSONObject json = jsonParser.makeHttpRequest(Config.URL_EVENT, "POST", param);
            try {
                JSONArray arr = json.getJSONArray("data");
                for (int i = 0; i<arr.length();i++){
                    JSONObject r = arr.getJSONObject(i);
                    event = new m_event();
                    event.setID(r.getInt("event_id"));
                    event.setJudul(r.getString("event_nama"));
                    event.setPeriode(r.getString("event_periode"));
                    event.setKet(r.getString("event_ket"));
                    event.setUrl(Config.DIR_DATA + r.getString("event_foto"));
                    dataEvent.add(event);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();

            if (dataEvent.size() > 0){
                lblEventEmpty.setVisibility(View.GONE);
                lvEvent.setVisibility(View.VISIBLE);
                mScrollView.fullScroll(ScrollView.FOCUS_UP);
                mScrollView.pageScroll(View.FOCUS_UP);
            }else{
                lblEventEmpty.setVisibility(View.VISIBLE);
                lvEvent.setVisibility(View.GONE);
            }

            eventAdapter.notifyDataSetChanged();

        }
    }

}
