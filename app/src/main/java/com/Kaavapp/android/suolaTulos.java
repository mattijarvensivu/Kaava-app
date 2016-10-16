package com.Kaavapp.android;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by janne on 15.10.2016.
 */
public class suolaTulos extends Tulos {


    //tällä konstruktorilla aiheutetaan reaktio kahden ionin välillä
    public suolaTulos(ioniTulos a, ioniTulos b, int liukoisuus)
    {
        tiedot = new HashMap<>();
        tiedot.put("liukoisuus",liukoisuus + "");
        tiedot.put("kaava",generateKaava(a,b));
        layoutSmall = R.layout.suola;
    }

    private String generateKaava(ioniTulos a, ioniTulos b)
    {
        //lasketaan pienin yhteinen monikerta
        long pym = lcm(Long.parseLong(a.getValue("varaus")),Long.parseLong(b.getValue("varaus")));
        String aString = a.getValue("kaava").substring(0,a.getValue("kaava").indexOf('^'));
        String bString = b.getValue("kaava").substring(0,b.getValue("kaava").indexOf('^'));
        long aMulti =  pym/Long.parseLong(a.getValue("varaus"));
        long bMulti =  pym/Long.parseLong(b.getValue("varaus"));
        Log.d("minun",a.getValue("varaus") + " ja " + b.getValue("varaus")+ " pym = " +pym + " amulti on " + aMulti + " bmulti on " + bMulti );
        if(aMulti > 1)
        {
            //ionia a tarvitaan enemmän kuin 1 kappale
            aString = "(" + aString + ")_" + aMulti;
        }
        if(bMulti > 1)
        {
            //ionia b tarvitaan enemmän kuin 1 kappale
            bString = "(" + bString + ")_" + bMulti;
        }
        String pal = aString + bString;
        //katsotaan lopulta kumpi on kationi. Veikkaus on että se on a.
        if(b.isKationi())
        {
            //väärin, b olikin kationi
            pal = bString + aString;
        }
        return pal;
    }

    //greatest common divisor
    private static long gcd(long a, long b)
    {
        while (b > 0)
        {
            long temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    //lowest common multiple
    private static long lcm(long a, long b)
    {
        return a * (b / gcd(a, b));
    }

    public View getSmallView(LayoutInflater infl, ViewGroup paren)
    {

        View pal = super.getSmallView(infl, paren);
        ((TextView)pal.findViewById(R.id.txvLiukoisuus)).setText(Integer.parseInt(tiedot.get("liukoisuus")));
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvLiukoisuus)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mvKaava)).setImageDrawable(kf.getBmD(tiedot.get("kaava")));
        return pal;

    }

}
