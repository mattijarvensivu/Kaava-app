package com.Kaavapp.android;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by janne on 23.11.2016.
 */
public class arvoTulos extends Tulos {
    private ArrayList<Tulos> arvot;
    private View largev;
private String nimiarvo;
    //on tärkeää että arvoId on dummy vakion yksikön arvon indeksi,
    private int arvoId = -1;//-1:error; 0:taitekerroin; 1:permitiivisyys; 2: permabiiteetti; 3:äänen nopeus
    static String[] arvoAvaimet = new String[]{"refra","permitivity","permeability","aanennopeus"};

    //tarkistetaan onko annettu hashmappi jonkin arvo taulun dummy vakio
    public static boolean isArvo(HashMap<String,String> vals)
    {
        for(String s: arvoAvaimet)
        {
            if(vals.get("yksikko") != null && s.compareTo(vals.get("yksikko"))== 0) return true;
        }
        return false;
    }

    public arvoTulos(HashMap<String,String> vals) {
        tiedot = vals;
        layoutSmall = R.layout.arvo_small;
        layoutLarge = R.layout.arvo_large;

        type = "arvo";
        taulu = "Vakio";
        tagiTaulu = "vakioTag";
        linkkiTaulu = "vakio_tag";

        defaultCategory = R.string.arvoCat;

        //asetetaan muuttuja jonka perusteella laitetaan UIhin tekstit.
        for(String s: arvoAvaimet)
        {
            arvoId++;
            if(vals.get("yksikko") != null && s.compareTo(vals.get("yksikko"))== 0) break;
        }

    }

    public String getArvoTaulu()
    {
        switch (arvoId) {
            case 0:
                return "taitekerroin";
            case 1:
                return "permitivity";
            case 2:
                return "permeability";
            case 3:
                return "aanennopeus";
        }
        return "virhe getArvoTaulussa";

    }
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = super.getSmallView(infl,paren);

        if(checkLanguage()){
            this.nimiarvo = "enaine";
        }else{
            this.nimiarvo = "aine";

        }

        int teksti = R.string.virhearvossa;
        switch (arvoId) {
            case 0:
                teksti = R.string.refraction;
                break;
            case 1:
                teksti = R.string.permitivity;
                break;
            case 2:
                teksti = R.string.permeability;
                break;
            case 3:
                teksti = R.string.aanennopeus;
                break;
        }
        ((TextView)pal.findViewById(R.id.txvKuvaus)).setText(teksti);
        return pal;
    }
    public View getLargeView (LayoutInflater infl, ViewGroup paren)
    {
        if(!dataHaettu) GAD.findData(this);
        View pal = infl.inflate(layoutLarge, paren, false);
        ((TextView)pal.findViewById(R.id.txvMateriaali)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ei ole olemass tilannetta missä tätä voitaisiin kutsua ilman että olisi olemassa large view
                for(Tulos t:arvot)
                {
                    t.sortKey = nimiarvo;
                }
                Collections.sort(arvot);
                ((TableLayout)largev.findViewById(R.id.tblMain)).removeAllViews();
                setTaulukko(largev);

            }
        });((TextView)pal.findViewById(R.id.txvKerroin)).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //ei ole olemass tilannetta missä tätä voitaisiin kutsua ilman että olisi olemassa large view
            for(Tulos t:arvot)
            {
                t.sortKey = "arvo";
            }
            Collections.sort(arvot);
            ((TableLayout)largev.findViewById(R.id.tblMain)).removeAllViews();
            setTaulukko(largev);

        }
    });

        int teksti = R.string.virhearvossa;
        int headerTeksti = -1;
        switch (arvoId) {
            case 0:
                teksti = R.string.kerroin;
                headerTeksti = R.string.kerroinTeksti;
                break;
            case 1:
                teksti = R.string.permitiivisyys;
                headerTeksti = R.string.huoneenlammossa;
                break;
            case 2:
                teksti = R.string.permeabilityheader;
                headerTeksti = R.string.huoneenlammossa;
                break;
            case 3:
                teksti = R.string.aanennopeusheader;
                headerTeksti = R.string.huoneenlammossa;
                break;
        }
        ((TextView)pal.findViewById(R.id.txvKerroin)).setText(teksti);

        if(headerTeksti != -1)
        ((TextView)pal.findViewById(R.id.txvHead)).setText(headerTeksti);

        setTaulukko(pal);

        largev = pal;
        return pal;
    }

    private void setTaulukko(View kohde)
    {
        TableLayout mainTbl = (TableLayout) kohde.findViewById(R.id.tblMain);
        for(Tulos t : arvot)
        {
            //luodaan yksittäinen rivi
            //luodaan asteiden teksti kenttä
            TableRow tmp = new TableRow(kohde.getContext());
            TextView tmpKerroin = new TextView(tmp.getContext());
            TextView tmpMateriaali = new TextView(tmp.getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Tulee tarkastella onko tämä järkevä ratkaisu. Onko api 23 liian uusi?
                tmpKerroin.setTextAppearance(android.R.style.TextAppearance_Large);
                tmpMateriaali.setTextAppearance(android.R.style.TextAppearance_Large);
            }
            tmpKerroin.setTextColor(Color.BLACK);
            tmpMateriaali.setTextColor(Color.BLACK);
            tmpKerroin.setText(t.getValue("arvo"));
            tmpMateriaali.setText(t.getValue(nimiarvo));
            //luodaan yksittäinen solu
            tmpMateriaali.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            tmp.addView(tmpMateriaali);
            tmp.addView(tmpKerroin);
            mainTbl.addView(tmp);
        }

    }

    public void setArvot(ArrayList<Tulos> vals)
    {
        arvot = vals;
        for(Tulos t:arvot)
        {
            t.sortKey = "arvo";
        }
        Collections.sort(arvot);
        dataHaettu = true;
    }
}
