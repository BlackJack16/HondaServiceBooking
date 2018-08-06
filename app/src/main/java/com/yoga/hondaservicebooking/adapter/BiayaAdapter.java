package com.yoga.hondaservicebooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yoga.hondaservicebooking.Config;
import com.yoga.hondaservicebooking.R;
import com.yoga.hondaservicebooking.model.m_biaya;

import java.util.ArrayList;

public class BiayaAdapter extends BaseAdapter {
    Context context;
    ArrayList<m_biaya> data;
    m_biaya item;

    TextView lblNama, lblBiaya;

    public BiayaAdapter(Context context, ArrayList<m_biaya> data){
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_biaya,viewGroup,false);

        item = data.get(i);
        lblNama = (TextView) view.findViewById(R.id.lblNama);
        lblBiaya = (TextView) view.findViewById(R.id.lblBiaya);
        lblNama.setText(item.getNama());
        lblBiaya.setText("RP "+ Config.number(item.getBiaya()));

        return view;
    }
}
