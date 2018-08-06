package com.yoga.hondaservicebooking.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.yoga.hondaservicebooking.R;

public class CustomTextSliderView extends BaseSliderView {
    private Context context ;
    public CustomTextSliderView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(context).inflate(R.layout.slider_item,null);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);

        description.setText(getDescription());

        bindEventAndShow(v, target);
        return v;
    }
}