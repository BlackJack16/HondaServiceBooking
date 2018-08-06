package com.yoga.hondaservicebooking.model;

public class m_event {
    private String Judul, Periode, Url, Ket;
    private int Res, ID;

    public String getJudul() {
        return Judul;
    }

    public void setJudul(String judul) {
        Judul = judul;
    }

    public String getPeriode() {
        return Periode;
    }

    public void setPeriode(String periode) {
        Periode = periode;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public int getRes() {
        return Res;
    }

    public void setRes(int res) {
        Res = res;
    }

    public String getKet() {
        return Ket;
    }

    public void setKet(String ket) {
        Ket = ket;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
