package com.yoga.hondaservicebooking.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.yoga.hondaservicebooking.Config;
import com.yoga.hondaservicebooking.R;
import com.yoga.hondaservicebooking.model.m_event;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    Context context;
    ArrayList<m_event> data;
    m_event item;

    TextView lblJudul, lblPeriode;
    ImageView imgFoto;
    CardView btnEvent;
    Picasso picasso;
    RequestCreator rq = null;
    private OnItemClickListener listener;

    public EventAdapter(Context context, ArrayList<m_event> data, OnItemClickListener listener){
        this.context = context;
        this.data = data;
        this.listener = listener;
        picasso = Picasso.with(this.context);
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

    public interface OnItemClickListener {
        void onItemClick(m_event item, int position);
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_event,viewGroup,false);

        item = data.get(position);
        btnEvent = (CardView) view.findViewById(R.id.btnEvent);
        lblJudul = (TextView) view.findViewById(R.id.lblJudul);
        lblPeriode = (TextView) view.findViewById(R.id.lblPeriode);
        lblJudul.setText(item.getJudul());
        lblPeriode.setText(item.getPeriode());

        imgFoto = (ImageView) view.findViewById(R.id.imgFoto);
        if (item.getUrl() != null){
            rq = picasso.load(item.getUrl());
        }else{
            rq = picasso.load(item.getRes());
        }
        final View finalView = view;
        rq.into(imgFoto,new Callback() {
            @Override
            public void onSuccess() {
                if(finalView.findViewById(com.daimajia.slider.library.R.id.loading_bar) != null){
                    finalView.findViewById(com.daimajia.slider.library.R.id.loading_bar).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError() {
                if(finalView.findViewById(com.daimajia.slider.library.R.id.loading_bar) != null){
                    finalView.findViewById(com.daimajia.slider.library.R.id.loading_bar).setVisibility(View.INVISIBLE);
                }
            }
        });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = data.get(position);
                listener.onItemClick(item, position);
            }
        });

        return view;
    }
}
