package com.yoga.hondaservicebooking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.yoga.hondaservicebooking.adapter.KendaraanSpinnerAdapter;
import com.yoga.hondaservicebooking.lib.DatePickerFragment;
import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;
import com.yoga.hondaservicebooking.model.m_booking;
import com.yoga.hondaservicebooking.model.m_kendaraan;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookingEdit extends AppCompatActivity {
    DatePickerFragment datePicker;
    EditText txtTanggal, txtCatatan;
    Spinner cbKendaraan, cbJam, cbMenit;
    KendaraanSpinnerAdapter kendaraanAdapter;
    ArrayList<m_kendaraan> data = new ArrayList<>();
    m_kendaraan item;

    m_booking booking = new m_booking();

    String[] arr_jam, arr_menit;

    m_auth auth;
    m_akun akun;
    private ProgressDialog pDialog;
    private JSONParser jsonParser = new JSONParser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        auth = new m_auth(this);
        akun = auth.getAkun();

        booking.setBooking_id(getIntent().getIntExtra("booking_id",0));
        booking.setKendaraan_id(getIntent().getIntExtra("kendaraan_id",0));
        booking.setTgl(getIntent().getStringExtra("tanggal"));
        booking.setJam(getIntent().getStringExtra("jam"));
        booking.setMenit(getIntent().getStringExtra("menit"));
        booking.setCatatan(getIntent().getStringExtra("catatan"));

        txtTanggal  = (EditText) findViewById(R.id.txtTanggal);
        txtCatatan  = (EditText) findViewById(R.id.txtCatatan);
        cbJam       = (Spinner)  findViewById(R.id.cbJam);
        cbMenit     = (Spinner)  findViewById(R.id.cbMenit);
        cbKendaraan = (Spinner)  findViewById(R.id.cbKendaraan);
        kendaraanAdapter = new KendaraanSpinnerAdapter(this, data);
        cbKendaraan.setAdapter(kendaraanAdapter);

        arr_jam = getResources().getStringArray(R.array.arr_jam);
        arr_menit = getResources().getStringArray(R.array.arr_menit);



        new getMasterProcess().execute();
    }

    private int getSelected(String[] arr, String value){
        int pos = 0;
        for (String item: arr) {
            if (item.equalsIgnoreCase(value)){
                return pos;
            }
            pos++;
        }
        return 0;
    }

    public void showPicker(View v){
        datePicker = new DatePickerFragment(txtTanggal);
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void doSimpan(View v) throws ParseException {
        booking.setKendaraan_id(data.get(cbKendaraan.getSelectedItemPosition()).getKendaraan_id());
        booking.setTgl(txtTanggal.getText().toString().trim());
        booking.setJam(cbJam.getSelectedItem().toString().trim());
        booking.setMenit(cbMenit.getSelectedItem().toString().trim());
        booking.setCatatan(txtCatatan.getText().toString().trim());

        Calendar c = Calendar.getInstance();
        String nowDate = ""+c.get(Calendar.DATE)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR);


        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Long dateBooking =  formatter.parse(booking.getTgl().replace("/","-")).getTime()/1000;
        Long dateNow = formatter.parse(nowDate).getTime()/1000;
        int JAM = c.get(Calendar.HOUR_OF_DAY);
        int MENIT = c.get(Calendar.MINUTE);


        if (dateBooking < dateNow){
            Toast.makeText(this,"Tanggal Booking tidak boleh kurang dari Tanggal hari ini!", Toast.LENGTH_LONG).show();
        }else if((dateBooking.equals(dateNow) &&  Integer.parseInt(booking.getJam()) < JAM) || (dateBooking.equals(dateNow) && Integer.parseInt(booking.getJam()) == JAM && Integer.parseInt(booking.getMenit()) < MENIT)){
            Toast.makeText(this,"Jam Booking tidak boleh kurang dari Jam sekarang!", Toast.LENGTH_LONG).show();
        }else if (booking.getCatatan().equalsIgnoreCase("")){
            Toast.makeText(this,"Catatan tidak boleh kosong!", Toast.LENGTH_LONG).show();
            txtCatatan.requestFocus();
        }else{
            new doKirim().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            setResult(0);
            finish();
        }else if(item.getItemId() == R.id.action_delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin akan booking service kendaraan ini ?")
                    .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new doDelete().execute();
                        }
                    })
                    .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setResult(0);
        super.onBackPressed();
    }


    private class getMasterProcess extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            data.clear();

            pDialog = new ProgressDialog(BookingEdit.this);
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

            kendaraanAdapter.notifyDataSetChanged();

            cbKendaraan.setSelection(kendaraanAdapter.getSelectedPositionById(booking.getKendaraan_id()));
            cbJam.setSelection(getSelected(arr_jam,booking.getJam()));
            cbMenit.setSelection(getSelected(arr_menit,booking.getMenit()));
            txtTanggal.setText(booking.getTgl());
            txtCatatan.setText(booking.getCatatan());
        }
    }

    private class doKirim extends AsyncTask<String, String, String> {

        int success = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingEdit.this);
            pDialog.setMessage("Sedang mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("booking", String.valueOf(booking.getBooking_id())));
            params.add(new BasicNameValuePair("kendaraan", String.valueOf(booking.getKendaraan_id())));
            params.add(new BasicNameValuePair("tgl",String.valueOf(booking.getTgl())));
            params.add(new BasicNameValuePair("jam",booking.getJam() + ":" + booking.getMenit()));
            params.add(new BasicNameValuePair("catatan",booking.getCatatan()));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_BOOKING_EDIT,
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
                    Toast.makeText(getWindow().getContext(), "Booking Service berhasil diubah, silahkan tunggu konfirmasi dari Administrator!", Toast.LENGTH_LONG).show();
                    setResult(1);
                    finish();
                }
            } else {
                Toast.makeText(getWindow().getContext(), "Booking Service gagal diubah!", Toast.LENGTH_LONG).show();
                txtCatatan.requestFocus();
            }
        }
    }

    private class doDelete extends AsyncTask<String, String, String> {

        int success = 0;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BookingEdit.this);
            pDialog.setMessage("Sedang mengirim data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("id", String.valueOf(booking.getBooking_id())));

            try {
                JSONObject json = jsonParser.makeHttpRequest(Config.URL_BOOKING_DELETE,
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

                    Toast.makeText(BookingEdit.this, "Booking Service berhasil dihapus!",Toast.LENGTH_LONG).show();
                    setResult(1);
                    finish();
                }
            } else {
                Toast.makeText(getWindow().getContext(), "Booking Service gagal dihapus!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
