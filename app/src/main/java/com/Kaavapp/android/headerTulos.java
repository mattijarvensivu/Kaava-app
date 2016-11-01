package com.Kaavapp.android;

import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by maza on 11/1/2016.
 */
public class headerTulos extends Tulos {


    public headerTulos(HashMap<String,String> vals){

        tiedot = vals;
        layoutSmall = R.layout.header;
    }

    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = super.getSmallView(infl,paren);
        ((TextView)pal.findViewById(R.id.header_value)).setText(tiedot.get("nimi"));
        return pal;
    }
}
