package com.yoga.hondaservicebooking.model;

public class m_kendaraan {
    private String type_nama, variant_nama, tahun, plat;
    private int kendaraan_id, type_id, variant_id;

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

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }

    public int getKendaraan_id() {
        return kendaraan_id;
    }

    public void setKendaraan_id(int kendaraan_id) {
        this.kendaraan_id = kendaraan_id;
    }
}
