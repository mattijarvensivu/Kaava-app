package com.Kaavapp.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


/**
 * Created by janne on 7/5/2016.
 */
public class muuttujaTulos extends Tulos{

private  String nimiarvo;
    public muuttujaTulos(HashMap<String,String> values) {
        layoutLarge = -1;
        layoutSmall = R.layout.muuttuja_small;
        tiedot = values;
        taulu = "Muuttuja";

        defaultCategory = R.string.muuttujaCat;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren) {
        if(checkLanguage()){
            this.nimiarvo = "enkuvaus";
        }else{
            this.nimiarvo = "kuvaus";

        }

        View pal = infl.inflate(layoutSmall, paren, false);

        ((TextView)pal.findViewById(R.id.txvKuvaus)).setText(tiedot.get(nimiarvo));
        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));

        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvKuvaus)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView) pal.findViewById(R.id.mvYksikko)).setImageDrawable(kf.getBmD(tiedot.get("yksikkö")));

        return pal;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getLargeView (LayoutInflater infl, ViewGroup paren) {
        //En keksinyt mitenkä pitkä jä lyhyt kuvaus eroaisivat toisistaan. Mielestäni muuttuja ei tarvitse kuin lyhyen, ainakin näillä toiminnoilla
        return getSmallView(infl,paren);
    }
}

