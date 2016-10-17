package com.Kaavapp.android;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by janne on 14.10.2016.
 */
public class ioniTulos extends Tulos {

    private boolean isKationi; //onko atomin varaus positiivinen

    public ioniTulos(HashMap<String,String> vals)
    {
        tiedot = vals;

        //otetaan muistiin onko kyseessä anioni vai kationi ja muutetaan varaus poisitiivieksi luvuksi
        isKationi = true;
        if(tiedot.get("varaus").charAt(0) == '-') isKationi = false;
        tiedot.put("varaus", Math.abs(Integer.parseInt(tiedot.get("varaus")))+""); //otetaan talteen vain varauksen magnitudi
        layoutSmall = R.layout.ioni_small;
    }


    public View getSmallView(LayoutInflater infl, ViewGroup paren)
    {
        View pal = super.getSmallView(infl, paren);
        ((TextView)pal.findViewById(R.id.txvVaraus)).setText(tiedot.get("varaus"));
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvVaraus)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mvKaava)).setImageDrawable(kf.getBmD(tiedot.get("kaava")));
        return pal;

    }

    public boolean isKationi() {return isKationi;}



}
