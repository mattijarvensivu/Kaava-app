package com.Kaavapp.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


/**
 * Created by maza on 7/5/2016.
 */
public class vakioTulos extends Tulos{


    public vakioTulos(HashMap<String,String> values) {
        layoutLarge = -1;
        layoutSmall = R.layout.vakio_small;
        tiedot = values;

        type = "Vakio";
        tagiTaulu = "VakioTag";
        linkkiTaulu = "Vakio_tag";

        defaultCategory = R.string.vakioCat;

    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren) {
        View pal = super.getSmallView(infl, paren);


        ((TextView)pal.findViewById(R.id.txvKuvaus)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvArvo)).setText(tiedot.get("arvo"));

        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvArvo)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.

        //((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));
        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));

        String yksikkoStr = tiedot.get("yksikko");

        if(yksikkoStr.compareTo("-") != 0) {
            ImageView yksikko = (ImageView) pal.findViewById(R.id.mvYksikko);


            ((ImageView) pal.findViewById(R.id.mvYksikko)).setImageDrawable(kf.getBmD(tiedot.get("yksikko")));
        }
        return pal;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getLargeView (LayoutInflater infl, ViewGroup paren) {
        //En keksinyt mitenkä pitkä jä lyhyt kuvaus eroaisivat toisistaan. Mielestäni vakion ei tarvitse kuin lyhyen, ainakin näillä toiminnoilla
        return getSmallView(infl,paren);
    }
}
