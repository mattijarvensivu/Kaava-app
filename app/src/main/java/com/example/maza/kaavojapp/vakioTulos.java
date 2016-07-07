package com.example.maza.kaavojapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import io.github.kexanie.library.MathView;

/**
 * Created by maza on 7/5/2016.
 */
public class vakioTulos extends Tulos{


    public vakioTulos(HashMap<String,String> values) {
        layoutLarge = -1;
        layoutSmall = R.layout.vakio_small;
        tiedot = values;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutSmall, paren, false);

        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));
        ((TextView)pal.findViewById(R.id.txvKuvaus)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvArvo)).setText(tiedot.get("arvo"));

        String yksikkoStr = tiedot.get("yksikko");

        if(yksikkoStr.compareTo("-") != 0) {
            MathView yksikko = (MathView) pal.findViewById(R.id.mvYksikko);
            yksikko.setText(yksikkoStr);
            yksikko.setClickable(false);
            yksikko.setLongClickable(false);
            yksikko.setFocusable(false);
            yksikko.setFocusableInTouchMode(false);
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
