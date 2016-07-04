package com.example.maza.kaavojapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by janne on 4.7.2016.
 */
public class isotooppiTulos extends Tulos {


    public isotooppiTulos(HashMap<String,String> values)
    {
        tiedot = values;
        layoutSmall = R.layout.isotope_small;
        layoutLarge = R.layout.isotope_large;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = infl.inflate(layoutSmall, paren, false);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvParticles)).setText(tiedot.get("massaluku"));
        String pVal = tiedot.get("puoliintumisaika") + " " + tiedot.get("yksikko");
        if(tiedot.get("puoliintumisaika").compareTo("0") == 0) pVal = "stabiili";
        ((TextView)pal.findViewById(R.id.txvPuolAika)).setText(pVal);

        return pal;
    }
}
