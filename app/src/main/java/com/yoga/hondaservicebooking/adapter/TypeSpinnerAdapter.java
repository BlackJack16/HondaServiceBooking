package com.yoga.hondaservicebooking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yoga.hondaservicebooking.model.m_type;

import java.util.ArrayList;

public class TypeSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<m_type> data;
    m_type item;

    TextView txtNama;

    public TypeSpinnerAdapter(Context context, ArrayList<m_type> data){
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
        for (m_type item: data) {
            if (item.getType_id() == id){
                return pos;
            }
            pos++;
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,viewGroup,false);

        item = data.get(i);
        txtNama = (TextView) view.findViewById(android.R.id.text1);
        txtNama.setText(item.getType_nama());

        return view;
    }
}
