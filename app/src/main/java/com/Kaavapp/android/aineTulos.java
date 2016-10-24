package com.Kaavapp.android;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janne on 22.10.2016.
 */
public class aineTulos extends Tulos {

    private happoTulos happo;

    public aineTulos(HashMap<String,String> vals)
    {
        tiedot = vals;
        layoutSmall = R.layout.aine_small;
        layoutLarge = R.layout.aine_large;
        Log.d("minun","ollaan aineTuloksessa");
        type = "aine";
        happo = null; //turha, mutta varmistetaan
    }

    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        if(happo != null)
        {
            return happo.getSmallView(infl,paren);
        }
        View pal = super.getSmallView(infl,paren);
        //laitetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(tiedot.get("name"));
        ((TextView)pal.findViewById(R.id.txvSulamis)).setText(tiedot.get("sp"));
        ((TextView)pal.findViewById(R.id.txvKiehumis)).setText(tiedot.get("kp"));

        //laitetaan yksiköt paikoilleen
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density));
        BitmapDrawable celsius = kf.getBmD("C^{\\circ}");

        ((ImageView) pal.findViewById(R.id.imgCelsius)).setImageDrawable(celsius);
        ((ImageView) pal.findViewById(R.id.imgCelsius2)).setImageDrawable(celsius);
        return pal;
    }

    public View getLargeView(LayoutInflater infl, ViewGroup paren)
    {
        View pal = infl.inflate(layoutLarge, paren, false);
        //laitetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(tiedot.get("name"));
        ((TextView)pal.findViewById(R.id.txvSulamis)).setText(tiedot.get("sp"));
        ((TextView)pal.findViewById(R.id.txvKiehumis)).setText(tiedot.get("kp"));
        ((TextView)pal.findViewById(R.id.txvTiheys)).setText(tiedot.get("tiheys"));
        ((TextView)pal.findViewById(R.id.txvResistanssi)).setText(tiedot.get("resistanssi"));
        ((TextView)pal.findViewById(R.id.txvOminaislampo)).setText(tiedot.get("olk"));

        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density));

        //määritellään kaava/kuva. Jos ensimmäinen merkki on _ on kyseessä kaava.
        String kaavaString =tiedot.get("kaava");
        if(kaavaString != null && kaavaString.charAt(0) == '_')
        {
            //kyseessä on kaava
            ((ImageView)pal.findViewById(R.id.imgKaava)).setImageDrawable(kf.getBmD(kaavaString.substring(1)));
        }else
        {
            //kyseessä on kuva
            try {
                ((ImageView)pal.findViewById(R.id.imgKaava)).setImageResource(R.drawable.class.getField(tiedot.get("kaava")).getInt(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        //laitetaan yksiköt paikoilleen
        BitmapDrawable celsius = kf.getBmD("C^{\\circ}");

        ((ImageView) pal.findViewById(R.id.imgCelsius)).setImageDrawable(celsius);
        ((ImageView) pal.findViewById(R.id.imgCelsius2)).setImageDrawable(celsius);
        ((ImageView)pal.findViewById(R.id.imgTiheys)).setImageDrawable(kf.getBmD("\\frac{g}{dm^{3}}"));
        ((ImageView)pal.findViewById(R.id.imgOminaislampo)).setImageDrawable(kf.getBmD("\\frac{J}{K*kg}"));
        ((ImageView)pal.findViewById(R.id.imgResistanssi)).setImageDrawable(kf.getBmD("\\Omega m")); //onko tässä bugi, vai miksi ei näy iso omega? pikku omega ja esim iso alpha näkyy!


        //laitetaan happo osa näkyviin, jos sellainen on
        if(happo != null)
        {
            LinearLayout lin = (LinearLayout)pal.findViewById(R.id.lnlHappo);
            lin.addView(happo.getLargeView(infl,lin));
        }
        return pal;
    }

    public void setHappo(ArrayList<Tulos> data)
    {
        if(data.size() == 0) return;
        if(data.size() > 1) //vain debugasuta varten!
        {
            Log.d("minun","liikaa tuloksia aineessa");
            return;
        }
        //tässä kohtaa, ainakin debugauksessa, teidetään että on vain 1 tulos... ja sitten tehdään leap of faith, joka ehkä tulee kusemaan tulevaisuudessa
        happo = (happoTulos) data.get(0);
    }
}
