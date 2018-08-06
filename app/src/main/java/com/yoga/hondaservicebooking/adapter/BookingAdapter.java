package com.yoga.hondaservicebooking.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoga.hondaservicebooking.R;
import com.yoga.hondaservicebooking.model.m_booking;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder>{
    private ArrayList<m_booking> data = new ArrayList<>();
    private m_booking item;
    private Context context;
    private OnItemClickListener listener;

    public BookingAdapter(Context context, ArrayList<m_booking> data, OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_booking, parent, false);

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
        void onItemClick(m_booking item, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView lblVariant, lblTahun, lblPlat, lblKode, lblTanggal, lblKet, lblCatatan, lblStatus, btnEdit;


        public ViewHolder(View itemView) {
            super(itemView);
            lblVariant = (TextView) itemView.findViewById(R.id.lblVariant);
            lblTahun = (TextView) itemView.findViewById(R.id.lblTahun);
            lblPlat = (TextView) itemView.findViewById(R.id.lblPlat);
            lblKode = (TextView) itemView.findViewById(R.id.lblKode);
            lblTanggal = (TextView) itemView.findViewById(R.id.lblTgl);
            lblKet = (TextView) itemView.findViewById(R.id.lblKet);
            lblCatatan = (TextView) itemView.findViewById(R.id.lblCatatan);
            lblStatus = (TextView) itemView.findViewById(R.id.lblStatus);
            btnEdit = (TextView) itemView.findViewById(R.id.btnEdit);
        }

        public void onBind(final int position, final OnItemClickListener listener) {
            item = data.get(position);
            lblVariant.setText(item.getType_nama() + " " + item.getVariant_nama());
            lblTahun.setText(item.getTahun());
            lblPlat.setText(item.getPlat());
            lblKode.setText("#" + item.getKode());
            lblTanggal.setText(item.getTanggal());
            lblCatatan.setText(item.getCatatan());
            lblKet.setText(item.getKet());

            if (item.getStatus()==1){
                lblStatus.setText("Menunggu");
                btnEdit.setVisibility(View.VISIBLE);
            }else if (item.getStatus()==2){
                lblStatus.setText("Diterima");
                btnEdit.setVisibility(View.GONE);
            }else if (item.getStatus()==0){
                lblStatus.setText("Ditolak");
                btnEdit.setVisibility(View.GONE);
            }

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item = data.get(position);
                    listener.onItemClick(item, position);
                }
            });



        }

    }
}
