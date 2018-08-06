package com.yoga.hondaservicebooking.model;

public class m_service {
    private String type_nama, variant_nama, tahun, plat, kode, booking, tanggal, total, totalPart, totalService, Catatan;
    private int service_id;

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

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getTotalPart() {
        return totalPart;
    }

    public void setTotalPart(String totalPart) {
        this.totalPart = totalPart;
    }

    public String getTotalService() {
        return totalService;
    }

    public void setTotalService(String totalService) {
        this.totalService = totalService;
    }

    public String getCatatan() {
        return Catatan;
    }

    public void setCatatan(String catatan) {
        Catatan = catatan;
    }
}
