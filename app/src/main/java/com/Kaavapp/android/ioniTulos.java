package com.Kaavapp.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janne on 14.10.2016.
 */
public class ioniTulos extends Tulos {

    private boolean isKationi; //onko atomin varaus positiivinen
    private ArrayList<ioniTulos>[] vastinIonit;
    private int suolaLayout;

    public ioniTulos(HashMap<String,String> vals)
    {
        tiedot = vals;

        //otetaan muistiin onko kyseessä anioni vai kationi ja muutetaan varaus poisitiivieksi luvuksi
        isKationi = true;
        if(tiedot.get("varaus").charAt(0) == '-') isKationi = false;
        tiedot.put("varaus", Math.abs(Integer.parseInt(tiedot.get("varaus")))+""); //otetaan talteen vain varauksen magnitudi
        layoutSmall = R.layout.ioni_small;
        layoutLarge = R.layout.ioni_large;
        suolaLayout = R.layout.suola;
        type = "ionit";
        taulu = "ionit";

        defaultCategory = R.string.ionCat;
    }


    public View getSmallView(LayoutInflater infl, ViewGroup paren)
    {
        View pal = super.getSmallView(infl, paren);
        //((TextView)pal.findViewById(R.id.txvVaraus)).setText(tiedot.get("varaus"));
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("name"));
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mvKaava)).setImageDrawable(kf.getBmD(tiedot.get("kaava")));
        return pal;

    }

    public View getLargeView(LayoutInflater infl, ViewGroup paren)
    {
        if(!dataHaettu) GAD.findData(this);
        View pal = infl.inflate(layoutLarge, paren, false);
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(tiedot.get("name"));
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mvKaava)).setImageDrawable(kf.getBmD(tiedot.get("kaava")));

        TableLayout suolatLay = (TableLayout)pal.findViewById(R.id.tblSuolat);

        int[] sanat = new int[]{R.string.liukoinen, R.string.osittain, R.string.niukka, R.string.reagoi}; //tämä siksi jotta voidaan loopata koko homma
        for(int i = 0; i < vastinIonit.length; i++)
        {
            for(ioniTulos it : vastinIonit[i])
            {
                suolatLay.addView(getSuolaView(it,sanat[i],infl,null));
            }
        }

        return pal;
    }

    private View getSuolaView(ioniTulos a, int liukoisuus,LayoutInflater infl, ViewGroup paren)
    {
        View pal = infl.inflate(suolaLayout,paren);
        ((TextView)pal.findViewById(R.id.txvLiukoisuus)).setText(liukoisuus);
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvLiukoisuus)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mvKaava)).setImageDrawable(kf.getBmD(generateKaava(a)));
        return pal;
    }

    public void setLiukoisuudet( ArrayList<ioniTulos>[] annetut)
    {
        vastinIonit = annetut;
        dataHaettu = true;
    }

    public boolean isKationi() {return isKationi;}


    //generoidaan suolan kaava annetun ionin ja tämän ionin välillä
    private String generateKaava(ioniTulos a)
    {
        //lasketaan pienin yhteinen monikerta
        long pym = lcm(Long.parseLong(a.getValue("varaus")),Long.parseLong(getValue("varaus")));
        String aString = a.getValue("kaava").substring(0,a.getValue("kaava").indexOf('^'));
        String bString = getValue("kaava").substring(0,getValue("kaava").indexOf('^'));
        long aMulti =  pym/Long.parseLong(a.getValue("varaus"));
        long bMulti =  pym/Long.parseLong(getValue("varaus"));
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
        if(isKationi())
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



}
