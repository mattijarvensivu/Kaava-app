package com.Kaavapp.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by janne on 4.7.2016.
 */
public class isotooppiTulos extends Tulos {

    String symbol = "";

    public isotooppiTulos(HashMap<String,String> values)
    {
        tiedot = values;
        layoutSmall = R.layout.isotope_small;
        layoutLarge = R.layout.isotope_large;

        type = "Isotoopit";
        taulu = "Isotoopit";

        defaultCategory = R.string.isotopeCat;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        if(!dataHaettu) GAD.findData(this);
        View pal = infl.inflate(layoutSmall, paren, false);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvParticles)).setText(tiedot.get("massaluku") + symbol);
        if(tiedot.get("puoliintumisaika").compareTo("0") == 0)
        {
            ((TextView)pal.findViewById(R.id.txvPuolAika)).setText(R.string.stabiili);

        }else {
            ((TextView) pal.findViewById(R.id.txvPuolAika)).setText(tiedot.get("puoliintumisaika") + " " + tiedot.get("yksikko"));
        }

        return pal;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta

    //Ei ehkä toimi. En päässyt testaamaan koska ei ole vielä keinoa jolla tästä luotaisiin instanssi. Tarkoitus olisi että kun alkuaineessa klikkaa isotooppia niin tämä ponnahtaisi isoksi.
    //Toisaalta onko tilanne jossa isotooppi tulisi näkyviin kun suoritetaan haku?
    public View getLargeView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutLarge, paren, false);

        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("massaluku") + symbol);

        if(tiedot.get("puoliintumisaika").compareTo("0") == 0)
        {
            ((TextView)pal.findViewById(R.id.txvPuolAika)).setText(R.string.stabiili);

        }else {
            ((TextView) pal.findViewById(R.id.txvPuolAika)).setText(tiedot.get("puoliintumisaika") + " " + tiedot.get("yksikko"));
        }

        ((TextView)pal.findViewById(R.id.txvOsuus)).setText(tiedot.get("esiintymisprosentti") + "%");

        //hoidetaan hajoamis tavat
        int[] hajoamis = selvennaHajoaminen(tiedot.get("hajoamistapa").split("#"));
        LinearLayout hajoamisTavat = (LinearLayout)pal.findViewById(R.id.lnlHajoamis);

        for(int i =0; i < hajoamis.length; i++)
        {
            TextView textView = new TextView(paren.getContext());

            textView.setText(hajoamis[i]);
            LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //width, height
            textView.setLayoutParams(textViewLayoutParams);
            hajoamisTavat.addView(textView);

        }


        return pal;
    }

    private int[] selvennaHajoaminen(String[] hajoamis)
    {
       int[] pal = new int[hajoamis.length];
        for(int i = 0; i < hajoamis.length; i++)
        {
            if(hajoamis[i].compareTo("SF") == 0)
            {
                pal[i] = R.string.spontaanifissio;
            }else  if(hajoamis[i].compareTo("EC") == 0)
            {
                pal[i] = R.string.elektronisieppaus;
            }else  if(hajoamis[i].compareTo("p") == 0)
            {
                pal[i] = R.string.protoniemissio;
            }else  if(hajoamis[i].compareTo("n") == 0)
            {
                pal[i] = R.string.neutroniemisio;
            }else  if(hajoamis[i].compareTo("IT") == 0)
            {
                pal[i] = R.string.isomeeritransfor;
            }else  if(hajoamis[i].compareTo("0") == 0)
            {
                pal[i] = R.string.stabiili;
            }else  if(hajoamis[i].compareTo("a") == 0)
            {
                pal[i] = R.string.alpfa;
            }else  if(hajoamis[i].compareTo("b+") == 0)
            {
                pal[i] = R.string.betaplus;
            }else  if(hajoamis[i].compareTo("b-") == 0)
            {
                pal[i] = R.string.betaminus;
            }
            /*
            String[] tmp = hajoamis[i].split( Character.toString('+') ); //Ei suostu ottamaan "+", joten tehdään sitten hölmöllä tavalla. Ei toimi tälläkään tavalla
            if(tmp.length > 1)
            {
                String[] osat = selvennaHajoaminen(tmp); //oletus, hajoaminen voi olla maksimissaan kahden summa
                pal[i] = osat[0] + "+" + osat[1];
            }
            */
        }
        return pal;

    }

    public void setSymbol(String value)
    {
        symbol = value;
        dataHaettu = true;
    }
    public String getName()
    {
        return tiedot.get("massaluku") + symbol;
    }
}
