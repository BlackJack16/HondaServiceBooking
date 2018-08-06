package com.yoga.hondaservicebooking.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

public class m_auth {
    private Context context;
    m_akun data;

    private boolean LOGGED;
    private SharedPreferences prefs;

    public m_auth(Context context){
        this.context = context;
        prefs = context.getSharedPreferences("ASTRA", AppCompatActivity.MODE_PRIVATE);
    }

    public SharedPreferences getPreferences()
    {
        return prefs;
    }

    public boolean isLogged(){
        LOGGED = prefs.getBoolean("logged", false);

        if (!LOGGED){
            return false;
        }
        setAkun();
        return true;
    }

    private void setAkun(){
        data = new m_akun();
        data.setID(prefs.getString("id",null));
        data.setNAMA(prefs.getString("nama",null));
        data.setALAMAT(prefs.getString("alamat",null));
        data.setTEMPAT(prefs.getString("tempat",null));
        data.setLAHIR(prefs.getString("lahir",null));
        data.setJK(prefs.getString("jk",null));
        data.setAGAMA(prefs.getString("agama",null));
        data.setEMAIL(prefs.getString("email",null));
        data.setTELEPON(prefs.getString("telepon",null));
    }


    public void setPrefAkun(m_akun item){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("id", item.getID());
        editor.putString("nama", item.getNAMA());
        editor.putString("alamat", item.getALAMAT());
        editor.putString("tempat", item.getTEMPAT());
        editor.putString("lahir", item.getLAHIR());
        editor.putString("jk", item.getJK());
        editor.putString("agama", item.getAGAMA());
        editor.putString("email", item.getEMAIL());
        editor.putString("telepon", item.getTELEPON());
        editor.putBoolean("logged", true);
        editor.commit();

        setAkun();
    }


    public m_akun getAkun(){
        setAkun();
        return data;
    }

    public void logout(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
}
