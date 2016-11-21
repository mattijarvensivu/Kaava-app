package com.Kaavapp.android;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by janne on 21.11.2016.
 */
public class refraTulos extends Tulos {

    private ArrayList<Tulos> arvot;
    private View largev;

    public refraTulos(HashMap<String,String> vals) {
        tiedot = vals;
        layoutSmall = R.layout.refraction_small;
        layoutLarge = R.layout.refraction_large;

        type = "refraction";
        taulu = "Vakio";
        tagiTaulu = "vakioTag";
        linkkiTaulu = "vakio_tag";

    }
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = super.getSmallView(infl,paren);
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
                    t.sortKey = "materiaali";
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
                t.sortKey = "kerroin";
            }
            Collections.sort(arvot);
            ((TableLayout)largev.findViewById(R.id.tblMain)).removeAllViews();
            setTaulukko(largev);

        }
    });
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
            tmpKerroin.setText(t.getValue("kerroin"));
            tmpMateriaali.setText(t.getValue("materiaali"));
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
            t.sortKey = "kerroin";
        }
        Collections.sort(arvot);
        dataHaettu = true;
    }
}
