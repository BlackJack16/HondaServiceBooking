package com.yoga.hondaservicebooking.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yoga.hondaservicebooking.R;
import com.yoga.hondaservicebooking.model.m_kendaraan;

import java.util.ArrayList;

public class KendaraanAdapter extends RecyclerView.Adapter<KendaraanAdapter.ViewHolder>{
    private ArrayList<m_kendaraan> data = new ArrayList<>();
    private m_kendaraan item;
    private Context context;
    private OnItemClickListener listener;

    public KendaraanAdapter(Context context, ArrayList<m_kendaraan> data, OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_kendaraan, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.onBind(position, listener);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public interface OnItemClickListener {
        void onItemClick(m_kendaraan item, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView lblVariant, lblTahun, lblPlat;
        protected LinearLayout btnDetail;


        public ViewHolder(View itemView) {
            super(itemView);
            lblVariant = (TextView) itemView.findViewById(R.id.lblVariant);
            lblTahun = (TextView) itemView.findViewById(R.id.lblTahun);
            lblPlat = (TextView) itemView.findViewById(R.id.lblPlat);

            btnDetail = (LinearLayout) itemView.findViewById(R.id.btnDetail);
        }

        public void onBind(final int position, final OnItemClickListener listener) {
            item = data.get(position);
            lblVariant.setText(item.getType_nama() + " " + item.getVariant_nama());
            lblTahun.setText(item.getTahun());
            lblPlat.setText(item.getPlat());

            btnDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item = data.get(position);
                    listener.onItemClick(item, position);
                }
            });


        }

    }
}
