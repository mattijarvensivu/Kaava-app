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
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        if(!dataHaettu) GAD.findData(this);
        View pal = infl.inflate(layoutSmall, paren, false);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvParticles)).setText(tiedot.get("massaluku") + symbol);
        String pVal = tiedot.get("puoliintumisaika") + " " + tiedot.get("yksikko");
        if(tiedot.get("puoliintumisaika").compareTo("0") == 0) pVal = "stabiili";
        ((TextView)pal.findViewById(R.id.txvPuolAika)).setText(pVal);

        return pal;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta

    //Ei ehkä toimi. En päässyt testaamaan koska ei ole vielä keinoa jolla tästä luotaisiin instanssi. Tarkoitus olisi että kun alkuaineessa klikkaa isotooppia niin tämä ponnahtaisi isoksi.
    //Toisaalta onko tilanne jossa isotooppi tulisi näkyviin kun suoritetaan haku?
    public View getLargeView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutLarge, paren, false);

        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("massaluku") + symbol);

        String pVal = tiedot.get("puoliintumisaika") + " " + tiedot.get("yksikko");
        if(tiedot.get("puoliintumisaika").compareTo("0") == 0) pVal = "stabiili";
        ((TextView)pal.findViewById(R.id.txvPuolAika)).setText(pVal);

        ((TextView)pal.findViewById(R.id.txvOsuus)).setText(tiedot.get("esiintymisprosentti") + "%");

        //hoidetaan hajoamis tavat
        String[] hajoamis = selvennaHajoaminen(tiedot.get("hajoamistapa").split("#"));
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

    private String[] selvennaHajoaminen(String[] hajoamis)
    {
        String[] pal = new String[hajoamis.length];
        for(int i = 0; i < hajoamis.length; i++)
        {
            pal[i] = hajoamis[i];
            if(hajoamis[i].compareTo("SF") == 0)
            {
                pal[i] = "Spontaani fissio";
            }else  if(hajoamis[i].compareTo("EC") == 0)
            {
                pal[i] = "Elektroni sieppaus";
            }else  if(hajoamis[i].compareTo("p") == 0)
            {
                pal[i] = "Protoni emissio";
            }else  if(hajoamis[i].compareTo("n") == 0)
            {
                pal[i] = "Neutroni emissio";
            }else  if(hajoamis[i].compareTo("IT") == 0)
            {
                pal[i] = "Isomeri transitio";
            }else  if(hajoamis[i].compareTo("0") == 0)
            {
                pal[i] = "Stabiili";
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
