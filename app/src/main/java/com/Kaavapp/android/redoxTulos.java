package com.Kaavapp.android;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by janne on 17.11.2016.
 */
public class redoxTulos extends Tulos {
    private ArrayList<Tulos> arvot;
    private View largev;

    public redoxTulos(HashMap<String,String> vals) {
        tiedot = vals;
        layoutSmall = R.layout.redox_small;
        layoutLarge = R.layout.redox_large;

        type = "redox";
        taulu = "Vakio";
        tagiTaulu = "vakioTag";
        linkkiTaulu = "vakio_tag";

        defaultCategory = R.string.redoxCat;

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
        ((TextView)pal.findViewById(R.id.txvReaction)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ei ole olemass tilannetta missä tätä voitaisiin kutsua ilman että olisi olemassa large view
                for(Tulos t:arvot)
                {
                    t.sortKey = "base";
                }
                Collections.sort(arvot);
                ((TableLayout)largev.findViewById(R.id.tblMain)).removeAllViews();
                setTaulukko(largev);

            }
        });((TextView)pal.findViewById(R.id.txvPotentiaali)).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //ei ole olemass tilannetta missä tätä voitaisiin kutsua ilman että olisi olemassa large view
            for(Tulos t:arvot)
            {
                t.sortKey = "potentiaali";
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
        KaavaFactory kf = new KaavaFactory(kohde.getContext(),kohde.getResources(),(int)Math.ceil(((TextView)kohde.findViewById(R.id.txvReaction)).getTextSize()/ kohde.getResources().getDisplayMetrics().density));
        for(Tulos t : arvot)
        {
            //luodaan yksittäinen rivi
            //luodaan asteiden teksti kenttä
            TableRow tmp = new TableRow(kohde.getContext());
            TextView tmpTV = new TextView(tmp.getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Tulee tarkastella onko tämä järkevä ratkaisu. Onko api 23 liian uusi?
                tmpTV.setTextAppearance(android.R.style.TextAppearance_Large);
            }
            tmpTV.setTextColor(Color.BLACK);
            tmpTV.setText(t.getValue("potentiaali"));
            //luodaan yksittäinen solu
            ImageView tmpIV = new ImageView(tmp.getContext());
            tmpIV.setImageDrawable(kf.getBmD(t.getValue("kaava")));
            tmpIV.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
            tmp.addView(tmpIV);
            tmp.addView(tmpTV);
            mainTbl.addView(tmp);
        }

    }

    public void setArvot(ArrayList<Tulos> vals)
    {
        arvot = vals;
        for(Tulos t:arvot)
        {
            t.sortKey = "potentiaali";
        }
        Collections.sort(arvot);
        dataHaettu = true;
    }
}
