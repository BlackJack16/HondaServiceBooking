package com.yoga.hondaservicebooking.model;

import java.util.ArrayList;

public class m_type {
    private int type_id;
    private String type_nama;
    private ArrayList<m_variant> variant;

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_nama() {
        return type_nama;
    }

    public void setType_nama(String type_nama) {
        this.type_nama = type_nama;
    }

    public ArrayList<m_variant> getVariant() {
        return variant;
    }

    public void setVariant(ArrayList<m_variant> variant) {
        this.variant = variant;
    }
}
