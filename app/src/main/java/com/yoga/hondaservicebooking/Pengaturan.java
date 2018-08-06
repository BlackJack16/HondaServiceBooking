package com.yoga.hondaservicebooking;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yoga.hondaservicebooking.model.m_akun;
import com.yoga.hondaservicebooking.model.m_auth;

public class Pengaturan extends AppCompatActivity {
    TextView lblNo, lblNama, lblAlamat, lblTTL, lblJK, lblAgama, lblTelepon, lblEmail;
    m_auth auth;
    m_akun akun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = new m_auth(this);
        akun = auth.getAkun();

        lblNo       = (TextView) findViewById(R.id.lblNo);
        lblNama     = (TextView) findViewById(R.id.lblNama);
        lblAlamat   = (TextView) findViewById(R.id.lblAlamat);
        lblTTL      = (TextView) findViewById(R.id.lblTTL);
        lblJK       = (TextView) findViewById(R.id.lblJK);
        lblAgama    = (TextView) findViewById(R.id.lblAgama);
        lblTelepon  = (TextView) findViewById(R.id.lblTelepon);
        lblEmail    = (TextView) findViewById(R.id.lblEmail);

        lblNo.setText(akun.getID());
        lblNama.setText(akun.getNAMA());
        lblAlamat.setText(akun.getALAMAT());
        lblTTL.setText(akun.getTEMPAT() + ", " + akun.getLAHIR());
        lblJK.setText(akun.getJK());
        lblAgama.setText(akun.getAGAMA());
        lblTelepon.setText(akun.getTELEPON());
        lblEmail.setText(akun.getEMAIL());
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

    public void showPassword(View v){
        Intent intent = new Intent(this, GantiPassword.class);
        startActivity(intent);
    }

    public void showLogout(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        auth.logout();
                        setResult(1);
                        finish();
                    }
                })
                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
