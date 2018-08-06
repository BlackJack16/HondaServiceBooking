package com.yoga.hondaservicebooking.model;


public class m_booking {
    private String type_nama, variant_nama, tahun, plat, kode, tanggal, ket, catatan, tgl, jam, menit;
    private int booking_id, kendaraan_id, status;


    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public int getKendaraan_id() {
        return kendaraan_id;
    }

    public void setKendaraan_id(int kendaraan_id) {
        this.kendaraan_id = kendaraan_id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getType_nama() {
        return type_nama;
    }

    public void setType_nama(String type_nama) {
        this.type_nama = type_nama;
    }

    public String getVariant_nama() {
        return variant_nama;
    }

    public void setVariant_nama(String variant_nama) {
        this.variant_nama = variant_nama;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }


    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public String getMenit() {
        return menit;
    }

    public void setMenit(String menit) {
        this.menit = menit;
    }
}
