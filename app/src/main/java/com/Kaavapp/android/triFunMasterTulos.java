package com.Kaavapp.android;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * luokan tehtävänä on pitää listaa yksittäisistä trigonometrisetArvot taulun riveistä ja tuoda ne käyttäjälle näkäyniin. Tietokannassa tämä luokka sijaitsee tulos taulussa.
 * Created by janne on 28.10.2016.
 */
public class triFunMasterTulos extends Tulos {

    private ArrayList<Tulos> arvot;

    public triFunMasterTulos(HashMap<String,String> vals) {
        tiedot = vals;
        layoutSmall = R.layout.arvo_small;
        layoutLarge = R.layout.trifunm_large;

        type = "triFunMaster";
        taulu = "Vakio";
        tagiTaulu = "vakioTag";
        linkkiTaulu = "vakio_tag";

        defaultCategory = R.string.triFunCat;

    }

    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = super.getSmallView(infl,paren);
        ((TextView)pal.findViewById(R.id.txvKuvaus)).setText(R.string.trifunt);
        return pal;
    }

    public String getNimi(){if(checkLanguage())return tiedot.get("ennimi");return tiedot.get("nimi");}

    public View getLargeView (LayoutInflater infl, ViewGroup paren)
    {
        if(!dataHaettu) GAD.findData(this);
        View pal = infl.inflate(layoutLarge, paren, false);
        TableLayout mainTbl = (TableLayout) pal.findViewById(R.id.tblMain);
        String[] kentat = {"radian","cos","sin","tan"}; //fukit, en rupee kirjottamaan 4x koodia jolla rakkennetaan yksittäinen imageViewi
        for(Tulos t : arvot)
        {
            //luodaan yksittäinen rivi
            //luodaan asteiden teksti kenttä
            TableRow tmp = new TableRow(paren.getContext());
            tmp.setPadding(0,0,0,30);
            TextView tmpTV = new TextView(tmp.getContext());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Tulee tarkastella onko tämä järkevä ratkaisu. Onko api 23 liian uusi?
                tmpTV.setTextAppearance(android.R.style.TextAppearance_Large);
            }
            tmpTV.setText(t.getValue("_aste"));
            tmp.addView(tmpTV);
            KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(tmpTV.getTextSize()/ pal.getResources().getDisplayMetrics().density));
            for(String s: kentat)
            {
                //luodaan yksittäinen solu
                ImageView tmpIV = new ImageView(tmp.getContext());
                tmpIV.setImageDrawable(kf.getBmD(t.getValue(s)));
                tmp.addView(tmpIV);

            }
            mainTbl.addView(tmp);
        }

        return pal;
    }

    public void setArvot(ArrayList<Tulos> vals)
    {
        arvot = vals;
        dataHaettu = true;
    }

}
