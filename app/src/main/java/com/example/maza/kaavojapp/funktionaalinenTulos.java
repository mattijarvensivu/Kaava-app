package com.example.maza.kaavojapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by maza on 7/5/2016.
 */
public class funktionaalinenTulos extends Tulos{

    private ArrayList<piikkiTulos> irpiikit;

    public funktionaalinenTulos(HashMap<String,String> values) {
        layoutLarge = R.layout.funktional_large;
        layoutSmall = R.layout.funktional_small;
        irpiikit = new ArrayList<>();
        tiedot = values;

        type = "Funktionaalinenryhma";
        tagiTaulu = "FunktionaalinenryhmaTag";
        linkkiTaulu = "Funktionaalinenryhma_tag";
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren) {
        View pal = super.getSmallView(infl, paren);
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

        //laitetaan piikit näkyville
        LinearLayout piikit = (LinearLayout)pal.findViewById(R.id.lnlPiikit);
        for(piikkiTulos p : irpiikit)
        {
            piikit.addView(p.getSmallView(infl,piikit));
        }

        return pal;
    }

    public void setPiikit(ArrayList<Tulos> given)
    {
        for(Tulos t: given)
        {
            irpiikit.add((piikkiTulos)t);
        }
    }
}
