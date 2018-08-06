package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yoga.hondaservicebooking.lib.Constants;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Event extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbar;
    Picasso picasso;
    RequestCreator rq = null;
    String id, Judul, Url, Periode, Ket;
    int Res;

    m_akun akun;
    m_auth auth;

    ImageView imgBackdrop;
    TextView lblJudul, lblPeriode, lblKet;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        auth = new m_auth(this);
        akun = auth.getAkun();

        id = getIntent().getStringExtra("id");
        Judul = getIntent().getStringExtra("judul");

        lblJudul = (TextView) findViewById(R.id.lblJudul);
        lblPeriode = (TextView) findViewById(R.id.lblPeriode);
        lblKet = (TextView) findViewById(R.id.lblKet);

        lblJudul.setText(Judul);
        lblPeriode.setText(Periode);
        lblKet.setText(Ket);



        new getDataProcess().execute();

        Firebase firebase = new Firebase(Constants.FIREBASE_APP + akun.getID() + "/event/");
        firebase.child(id).removeValue();
    }

    private void loadBackdrop() {
        picasso = Picasso.with(this);
        imgBackdrop = (ImageView) findViewById(R.id.backdrop);
        if (Url != null){
            rq = picasso.load(Url);
        }else{
            rq = picasso.load(Res);
            imgBackdrop.setImageResource(Res);
        }
        rq.into(imgBackdrop,new Callback() {
            @Override
            public void onSuccess() {
                if(findViewById(com.daimajia.slider.library.R.id.loading_bar) != null){
                    findViewById(com.daimajia.slider.library.R.id.loading_bar).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError() {
                if(findViewById(com.daimajia.slider.library.R.id.loading_bar) != null){
                    findViewById(com.daimajia.slider.library.R.id.loading_bar).setVisibility(View.INVISIBLE);
                }
            }
        });
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
            collapsingToolbar.setTitle("Event");
        }else {

            collapsingToolbar.setTitle("");
        }
    }

    private class getDataProcess extends AsyncTask<String, String, String> {

        int success = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Event.this);
            pDialog.setMessage("Mengunduh data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", id));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_EVENT,
                        "POST", params);
                JSONObject r = json.getJSONObject("data");
                if (r.length() > 0) {
                    success =1;
                    Ket =  r.getString("event_ket");
                    Periode = r.getString("event_periode");
                    Url = Config.DIR_DATA + r.getString("event_foto");
                }
            } catch (JSONException e) {
                success = 2;
            }

            return null;
        }




        protected void onPostExecute(String file_url) {
            pDialog.dismiss();

            if (success == 1) {
                lblKet.setText(Ket);
                lblPeriode.setText(Periode);
                loadBackdrop();
            }
        }
    }


}
