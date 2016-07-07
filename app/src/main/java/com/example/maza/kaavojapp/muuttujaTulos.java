package com.example.maza.kaavojapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import io.github.kexanie.library.MathView;

/**
 * Created by janne on 7/5/2016.
 */
public class muuttujaTulos extends Tulos{


    public muuttujaTulos(HashMap<String,String> values) {
        layoutLarge = -1;
        layoutSmall = R.layout.muuttuja_small;
        tiedot = values;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutSmall, paren, false);

        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));
        ((TextView)pal.findViewById(R.id.txvKuvaus)).setText(tiedot.get("kuvaus"));

        MathView yksikko = (MathView)pal.findViewById(R.id.mvYksikko);
        yksikko.setText(tiedot.get("yksikkö"));
        yksikko.setClickable(false);
        yksikko.setLongClickable(false);
        yksikko.setFocusable(false);
        yksikko.setFocusableInTouchMode(false);
        return pal;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getLargeView (LayoutInflater infl, ViewGroup paren) {
        //En keksinyt mitenkä pitkä jä lyhyt kuvaus eroaisivat toisistaan. Mielestäni muuttuja ei tarvitse kuin lyhyen, ainakin näillä toiminnoilla
        return getSmallView(infl,paren);
    }
}

