package com.Kaavapp.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by janne on 6.8.2016.
 */
public class piikkiTulos extends Tulos {

    public piikkiTulos(HashMap<String,String> values) {
        layoutLarge = -1; //semiTulos... pää asiassa käytetään näyttämään tietoa osana toista tulos luokkaa
        layoutSmall = R.layout.piikki_small;
        tiedot = values;
        if(tiedot.get("sidos").contains("(ks)"))
        {
            //on olemassa kolmois sidos
            String tmp = tiedot.get("sidos").replace("(ks)","\u2261");
            tiedot.put("sidos",tmp);
        }
    }

    public View getSmallView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutSmall, paren, false);
        //laitetaan kuva paikoilleen
        ((TextView)pal.findViewById(R.id.txvSidos)).setText(tiedot.get("sidos"));
        ((TextView)pal.findViewById(R.id.txvLuonne)).setText(tiedot.get("luonne"));

        String irAlue = tiedot.get("alku") + " - " + tiedot.get("loppu");
        ((TextView)pal.findViewById(R.id.txvAlue)).setText(irAlue);

        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvSidos)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.imvYksikko)).setImageDrawable(kf.getBmD("cm^{-1}"));



        return pal;
    }
}
