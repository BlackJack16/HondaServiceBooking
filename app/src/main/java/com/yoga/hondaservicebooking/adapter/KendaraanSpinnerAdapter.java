package com.yoga.hondaservicebooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yoga.hondaservicebooking.R;
import com.yoga.hondaservicebooking.model.m_kendaraan;

import java.util.ArrayList;

public class KendaraanSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<m_kendaraan> data;
    m_kendaraan item;

    TextView txtVariant, txtTahun, txtPlat;

    public KendaraanSpinnerAdapter(Context context, ArrayList<m_kendaraan> data){
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public int getSelectedPositionById(int id){
        int pos=0;
        for (m_kendaraan item: data) {
            if (item.getKendaraan_id() == id){
                return pos;
            }
            pos++;
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.kendaraan_item,viewGroup,false);

        item = data.get(i);
        txtVariant = (TextView) view.findViewById(R.id.lblVariant);
        txtTahun = (TextView) view.findViewById(R.id.lblTahun);
        txtPlat = (TextView) view.findViewById(R.id.lblPlat);
        txtVariant.setText(item.getType_nama() + " " + item.getVariant_nama());
        txtTahun.setText(item.getTahun());
        txtPlat.setText(item.getPlat());

        return view;
    }
}
