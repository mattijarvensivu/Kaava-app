package com.example.maza.kaavojapp;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created by maza on 7/5/2016.
 */
public class kaavaTulos extends Tulos{

    //onko järkevää. Nyt luodaan potentiaalisesti suuri määrä täysin samanlaisia olioita. Jos palautetaan kaikki kaavat, luodaan jokaiselle kaavalle oma instanssinsa muttujista ja vakioista
    //paras tapaan olisi hakea muuttujat ja vakiot vasta kun sitä tarvitaan.
    private ArrayList<Tulos> vakiot;
    private ArrayList<Tulos> muuttujat;
    //private String kaikkimuuttujat;


    public kaavaTulos(HashMap<String,String> values) {
        layoutLarge = R.layout.kaava_large;
        layoutSmall = R.layout.kaava_small;
        tiedot = values;
        muuttujat = new ArrayList<>();
        vakiot = new ArrayList<>();

        tagiTaulu = "KaavaTag";
        linkkiTaulu = "Kaava_tag";
        type = "Kaava";


    }


    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
       View pal = super.getSmallView(infl,paren);

        //asetetaan tiedot paikoilleen
        String nimi = tiedot.get("nimi");
        if(nimi.compareTo("-")== 0) nimi = "";
        ((TextView)pal.findViewById(R.id.kaava_nimi)).setText(nimi);

        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.kaava_nimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.kaava_lause_small)).setImageDrawable(kf.getBmD(tiedot.get("lause")));



        return pal;
    }

    public View getLargeView (LayoutInflater infl, ViewGroup paren) {
        View pal = infl.inflate(layoutLarge, paren, false);
        KaavaFactory ka = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.kaava_nimi_largeee)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.kaava_lause_large)).setImageDrawable(ka.getBmD(tiedot.get("lause")));
        String nimi = tiedot.get("nimi");
        if(nimi.compareTo("-")== 0) nimi = "";
        ((TextView)pal.findViewById(R.id.kaava_nimi_largeee)).setText(nimi);


       /* kaikkimuuttujat = "";
        MathView muut = (MathView)pal.findViewById(R.id.kaikmuut);
       for(int i =0; i< muuttujat.size(); i++){
           kaikkimuuttujat += muuttujat.get(i).getValue("symbol") + " = " +  muuttujat.get(i).getValue("kuvaus") + "  "+  muuttujat.get(i).getValue("yksikkö")+"\n";
        }
      */  //muut.setText(kaikkimuuttujat);
        LinearLayout muuttujaLL = (LinearLayout) pal.findViewById(R.id.muuttujatLL);
        LinearLayout VakioLL = (LinearLayout) pal.findViewById(R.id.vakiotLL);
        for(int i = 0; i < muuttujat.size(); i++)
        {
            View tmp = muuttujat.get(i).getSmallView(infl,muuttujaLL);
            //tmp.setClickable(true);


            muuttujaLL.addView(tmp);
        }
        for(int j=0; j < vakiot.size(); j++) {
            View tmp2 = vakiot.get(j).getSmallView(infl, VakioLL);
            VakioLL.addView(tmp2);

        }

        //asetetaan kuva
        try {
            if(tiedot.get("kuva")!= null)
            ((ImageView)pal.findViewById(R.id.imgKuva)).setImageResource(R.drawable.class.getField(tiedot.get("kuva")).getInt(null));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


        return pal;
    }

    public void addMuuttujat(ArrayList<Tulos> vals)
    {
        for(int i = 0; i < vals.size(); i++) {
            muuttujat.add((muuttujaTulos)vals.get(i));
            Log.d("Lisätty",vals.get(i).getValue("kuvaus"));
            //Log.d("KAIKKIMUUTTUJAT",muuttujat.get(i).getValue("kuvaus"));
            //this.kaikkimuuttujat += muuttujat.get(i).getValue("symbol") + " = " +  muuttujat.get(i).getValue("kuvaus") + "  "+  muuttujat.get(i).getValue("yksikkö")+"\n";
        }
    }
    public void addVakiot(ArrayList<Tulos> vals)
    {
        for(int i = 0; i < vals.size(); i++) {
            vakiot.add((vakioTulos)vals.get(i));
            Log.d("Lisätty",vals.get(i).getValue("nimi"));
            //Log.d("KAIKKIMUUTTUJAT",muuttujat.get(i).getValue("kuvaus"));
            //this.kaikkimuuttujat += muuttujat.get(i).getValue("symbol") + " = " +  muuttujat.get(i).getValue("kuvaus") + "  "+  muuttujat.get(i).getValue("yksikkö")+"\n";
        }
    }



}

