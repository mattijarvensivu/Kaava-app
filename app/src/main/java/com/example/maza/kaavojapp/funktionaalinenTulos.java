package com.example.maza.kaavojapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by maza on 7/5/2016.
 */
public class funktionaalinenTulos extends Tulos{


    public funktionaalinenTulos(HashMap<String,String> values) {
        layoutLarge = R.layout.funktional_large;
        layoutSmall = R.layout.funktional_small;
        tiedot = values;
        type = "Funktionaalinenryhma";
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutSmall, paren, false);
        //laitetaan kuva paikoilleen
        try {
            ((ImageView)pal.findViewById(R.id.imgKuva)).setImageResource(R.drawable.class.getField(tiedot.get("nimi")).getInt(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
        return pal;
    }

    public View getLargeView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutLarge, paren, false);

        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
        try {
            ((ImageView)pal.findViewById(R.id.imgKuva)).setImageResource(R.drawable.class.getField(tiedot.get("nimi")).getInt(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        ((TextView)pal.findViewById(R.id.txvNom)).setText(tiedot.get("nimifragmentti"));

        String irAlue = tiedot.get("iralku") + " - " + tiedot.get("irloppu");
        ((TextView)pal.findViewById(R.id.txvIR)).setText(irAlue);

        if(tiedot.get("nmralku").compareTo("0") != 0 || tiedot.get("nmralku").compareTo("0") != 0)
        {
            //on olemassa nmr siirtymä
            String nmrAlue = tiedot.get("nmralku") +" - " + tiedot.get("nmrloppu") + " pm";
            ((TextView)pal.findViewById(R.id.txvNMR)).setText(nmrAlue);

        }else
        {
            ((LinearLayout)pal.findViewById(R.id.lnlNMR)).removeAllViews();
        }

        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvIR)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mwIrUnit)).setImageDrawable(kf.getBmD("cm^{-1}"));

        return pal;
    }

    public String getKeyField()
    {
        return tiedot.get("nimi");
    }
}
