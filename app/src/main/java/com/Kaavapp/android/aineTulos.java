package com.Kaavapp.android;

import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janne on 22.10.2016.
 */
public class aineTulos extends Tulos {

    public aineTulos(HashMap<String,String> vals)
    {
        tiedot = vals;
        layoutSmall = R.layout.aine_small;
        layoutLarge = R.layout.aine_large;

        type = "aine";
        taulu = "aine";
        tagiTaulu = "aineTag";
        linkkiTaulu = "aine_tag";
    }

    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        //määritellään desimaali formaatit pyöristyksiä varten
        DecimalFormat df2 = new DecimalFormat("#.##");

        View pal = super.getSmallView(infl,paren);
        //laitetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
       // ((TextView)pal.findViewById(R.id.txvName)).setText(tiedot.get("name"));
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density));
        BitmapDrawable celsius = kf.getBmD("C^{\\circ}");

        if(!dataHaettu) GAD.findData(this);

        if(tiedot.get("sulamispiste").compareTo("-") == 0)
        {
            ((ViewGroup)pal.findViewById(R.id.lnlMp)).removeAllViews();

        }else {
            ((TextView)pal.findViewById(R.id.txvSulamis)).setText(df2.format(Float.parseFloat(tiedot.get("sulamispiste"))));
            ((ImageView) pal.findViewById(R.id.imgCelsius)).setImageDrawable(celsius);
        }
        if(tiedot.get("kiehumispiste").compareTo("-") == 0)
        {
            ((ViewGroup)pal.findViewById(R.id.lnlBp)).removeAllViews();

        }else {
            ((TextView)pal.findViewById(R.id.txvKiehumis)).setText(df2.format(Float.parseFloat(tiedot.get("kiehumispiste"))));
            ((ImageView) pal.findViewById(R.id.imgCelsius2)).setImageDrawable(celsius);
        }

        return pal;
    }

    public View getLargeView(LayoutInflater infl, ViewGroup paren)
    {
        //määritellään desimaali formaatit pyöristyksiä varten
        DecimalFormat df2 = new DecimalFormat("#.##");
        View pal = infl.inflate(layoutLarge, paren, false);
        //laitetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(tiedot.get("name"));
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density));
        BitmapDrawable celsius = kf.getBmD("C^{\\circ}");
        if(tiedot.get("sulamispiste").compareTo("-") == 0)
        {
            ((ViewGroup)pal.findViewById(R.id.lnlMp)).removeAllViews();

        }else {
            ((TextView) pal.findViewById(R.id.txvSulamis)).setText(df2.format(Float.parseFloat(tiedot.get("sulamispiste"))));
            ((ImageView) pal.findViewById(R.id.imgCelsius)).setImageDrawable(celsius);
        }
        if(tiedot.get("kiehumispiste").compareTo("-") == 0)
        {
            ((ViewGroup)pal.findViewById(R.id.lnlBp)).removeAllViews();

        }else {
            ((TextView)pal.findViewById(R.id.txvKiehumis)).setText(df2.format(Float.parseFloat(tiedot.get("kiehumispiste"))));
            ((ImageView) pal.findViewById(R.id.imgCelsius2)).setImageDrawable(celsius);
        }
        if(tiedot.get("resistanssi").compareTo("-") == 0)
        {
            ((ViewGroup)pal.findViewById(R.id.lnlRes)).removeAllViews();

        }else {
            ((TextView)pal.findViewById(R.id.txvResistanssi)).setText(tiedot.get("resistanssi"));
            ((ImageView)pal.findViewById(R.id.imgResistanssi)).setImageDrawable(kf.getBmD("\\Omega m")); //onko tässä bugi, vai miksi ei näy iso omega? pikku omega ja esim iso alpha näkyy!
        }
        if(tiedot.get("ominaislampokapasiteetti").compareTo("-") == 0)
        {
            ((ViewGroup)pal.findViewById(R.id.lnlOlk)).removeAllViews();

        }else {
            ((TextView)pal.findViewById(R.id.txvOminaislampo)).setText(tiedot.get("ominaislampokapasiteetti"));
            ((ImageView)pal.findViewById(R.id.imgOminaislampo)).setImageDrawable(kf.getBmD("\\frac{J}{K*kg}"));

        }
        if(tiedot.get("lampolaajeneminen").compareTo("-")==0){
        ((ViewGroup)pal.findViewById(R.id.lnlLampoL)).removeAllViews();
    }
        else{
            ((TextView)pal.findViewById(R.id.txvlampolaajeneminen)).setText(tiedot.get("lampolaajeneminen"));
            ((ImageView)pal.findViewById(R.id.imglampolaajeneminen)).setImageDrawable(kf.getBmD("\\frac{1}{C^{\\circ}}"));
    }

        ((TextView)pal.findViewById(R.id.txvTiheys)).setText(tiedot.get("tiheys"));





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




        ((ImageView)pal.findViewById(R.id.imgTiheys)).setImageDrawable(kf.getBmD("\\frac{g}{cm^{3}}"));



        return pal;
    }

    public void setAlkuaine(ArrayList<Tulos> data)
    {
        dataHaettu = true;
        if (data == null) return;
        tiedot.put("sulamispiste",data.get(0).getValue("sulamispiste"));
        tiedot.put("kiehumispiste",data.get(0).getValue("kiehumispiste"));
        tiedot.put("tiheys",data.get(0).getValue("tiheys"));
        tiedot.put("ominaislampokapasiteetti",data.get(0).getValue("ominaislampokapasiteetti"));
    }
}
