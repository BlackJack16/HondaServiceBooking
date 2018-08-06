package com.yoga.hondaservicebooking;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Locale;

public class Config {
    public static String ROOT = "http://10.0.0.54/";
    public static String DIR_DATA = ROOT +  "skripsi/booking/data/";
    public static String URL = ROOT + "skripsi/booking/android?m=";
    public static String URL_LOGIN = URL +  "auth";
    public static String URL_REGISTRASI = URL +  "registrasi";
    public static String URL_GANTIPWD = URL +  "gantipwd";
    public static String URL_EVENT = URL +  "event";
    public static String URL_MASTER_KENDARAAN = URL +  "master_kendaraan";
    public static String URL_KENDARAAN = URL +  "kendaraan";
    public static String URL_KENDARAAN_ADD = URL +  "kendaraan_add";
    public static String URL_KENDARAAN_EDIT = URL +  "kendaraan_edit";
    public static String URL_KENDARAAN_DELETE = URL +  "kendaraan_delete";
    public static String URL_BOOKING = URL +  "booking";
    public static String URL_BOOKING_ADD = URL +  "booking_add";
    public static String URL_BOOKING_EDIT = URL +  "booking_edit";
    public static String URL_BOOKING_DELETE = URL +  "booking_delete";
    public static String URL_RIWAYAT = URL +  "riwayat";
    public static String URL_SARAN = URL +  "saran";

    public static String number(long harga){
        NumberFormat num = NumberFormat.getInstance(Locale.GERMAN);

        return num.format(harga);
    }

    public static String format(String tanggal){
        return tanggal.substring(8,10)+'/'+tanggal.substring(5,7)+'/'+tanggal.substring(0,4);
    }


}