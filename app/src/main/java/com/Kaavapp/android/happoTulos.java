package com.Kaavapp.android;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;


/**
 * Created by janne on 29.6.2016.
 */
public class happoTulos extends Tulos {


    public happoTulos(HashMap<String,String> vals)
    {
        tiedot = vals;
        layoutSmall = R.layout.happo_small;
        layoutLarge = R.layout.happo_large;

        type = "Happo";
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        DecimalFormat df2 = new DecimalFormat("#.##");

        View pal = infl.inflate(layoutSmall, paren, false);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("name"));
        ((TextView)pal.findViewById(R.id.txvPKa)).setText(tiedot.get("happovakio1"));
        ((TextView)pal.findViewById(R.id.txvPh)).setText(df2.format(calculatePh(1))+"");
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mvKaava)).setImageDrawable(kf.getBmD(tiedot.get("kaava")));

        //asetetaan pKa/pKb labeli oikeaksi
        //jos on happo, ovat labelit valmiiksi oikein
        if(tiedot.get("isHappo").compareTo("1") != 0)
        {
            //kyseessä on emäs
            ((TextView)pal.findViewById(R.id.txvHappoVakioLabel)).setText(R.string.emäsvakiot);
        }

        return pal;
    }

    public View getLargeView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = infl.inflate(layoutLarge, paren, false);

        SeekBar conBar = (SeekBar) pal.findViewById(R.id.sbCons);
        final TextView phView = (TextView) pal.findViewById(R.id.txvPh);
        final TextView cons = (TextView) pal.findViewById(R.id.txvCons);
        final DecimalFormat df2 = new DecimalFormat("#.##");

        //asetetaan arvot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(tiedot.get("name"));
        ((TextView)pal.findViewById(R.id.txvMoolimassa)).setText(tiedot.get("moolimassa"));

        phView.setText(df2.format(calculatePh(1))+"");
        LinearLayout mainLay = (LinearLayout)pal.findViewById(R.id.lnMain);
        try {
            if(tiedot.get("kuva").compareTo("-") != 0)
            ((ImageView)pal.findViewById(R.id.imgKuva)).setImageResource(R.drawable.class.getField(tiedot.get("kuva")).getInt(null));
        } catch (IllegalAccessException e1) {
            e1.printStackTrace();
        } catch (NoSuchFieldException e1) {
            e1.printStackTrace();
        }

        //asetetaan yksikkö kuvat
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(phView.getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        ((ImageView)pal.findViewById(R.id.mvKaava)).setImageDrawable(kf.getBmD(tiedot.get("kaava")));

        BitmapDrawable celsius = kf.getBmD("C^{\\circ}");
        ((ImageView) pal.findViewById(R.id.mwSpUnit)).setImageDrawable(celsius);
        ((ImageView) pal.findViewById(R.id.mwKpUnit)).setImageDrawable(celsius);

        //asetetaan loput yksiköt paikoilleen
        ((ImageView)pal.findViewById(R.id.mwDenUnit)).setImageDrawable(kf.getBmD("\\frac{g}{dm^{3}}"));
        ((ImageView)pal.findViewById(R.id.mwConUnit)).setImageDrawable(kf.getBmD("\\frac{mol}{l}"));
        ((ImageView)pal.findViewById(R.id.mwMMUnit)).setImageDrawable(kf.getBmD("\\frac{g}{mol}"));

        //laitetaan happo vakiot.
        String[] ies = {tiedot.get("happovakio1"), tiedot.get("happovakio2"), tiedot.get("happovakio3")};

        for (int i = 0; i < ies.length; i++) {
            if (Float.parseFloat(ies[i]) != 0) {
                TextView textView = new TextView(paren.getContext());
                textView.setText((i + 1) + ": " + Float.parseFloat(ies[i]));
                LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //width, height
                textView.setLayoutParams(textViewLayoutParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Tulee tarkastella onko tämä järkevä ratkaisu. Onko api 23 liian uusi?
                    textView.setTextAppearance(android.R.style.TextAppearance_Large);
                }
                mainLay.addView(textView);
            }
        }

        //alustetaan konsentraatio seekBar
        conBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float consen = progress/100f + 0.01f;
                cons.setText(df2.format(consen));
                phView.setText(df2.format(calculatePh(consen)));
                }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //käsitellään tiheys
        String dens = tiedot.get("tiheys");
        if(dens.compareTo("-1") == 0)
        {
            dens = "Ei määritelty";

            ((ViewGroup)pal.findViewById(R.id.mwDenUnit).getParent()).removeView(pal.findViewById(R.id.mwDenUnit));

        }
        ((TextView)pal.findViewById(R.id.txvDensity)).setText(dens);

        //käsitellään sulamis ja kiehumis pisteet
        String mp = tiedot.get("sulamispiste");
        String bp = tiedot.get("kiehumispiste");
        if(mp.compareTo("-1") == 0)
        {
            mp = "Ei määritelty"; //Onko olemassa tilanne että aine hajoaa ennenkuin se sulaa?
            ((ViewGroup)pal.findViewById(R.id.mwSpUnit).getParent()).removeView(pal.findViewById(R.id.mwSpUnit));
        }
        if(bp.compareTo("-1") == 0)
        {
            if(mp.compareTo("Ei määritelty") == 0) {
                bp = "Ei määritelty";
            }else{
                bp = "Hajoaa";
            }
            ((ViewGroup)pal.findViewById(R.id.mwKpUnit).getParent()).removeView(pal.findViewById(R.id.mwKpUnit));
        }

        ((TextView)pal.findViewById(R.id.txvSp)).setText(mp);
        ((TextView)pal.findViewById(R.id.txvKp)).setText(bp);

        //asetetaan pKa/pKb labeli oikeaksi
        //jos on happo, ovat labelit valmiiksi oikein
        if(tiedot.get("isHappo").compareTo("1") != 0)
        {
            //kyseessä on emäs
            ((TextView)pal.findViewById(R.id.txvHappoVakioLabel)).setText(R.string.emäsvakiot);
        }

        return pal;
    }

    //lasketaan tämän hapon pH annetulla konsentraatiolla. consent, eli hapon konsentraatio tulee antaa yksikössä mol/l.
    //Tämä pätee niin mono- kuin polyproottisiin happoihin. Polyproottisissa hapoissa ensimmäinen dissosiaatio dominoi.
    public double calculatePh (float consent)
    {
        if (consent == 0) return -1;
        double Ka = Math.pow(10, -1*Double.parseDouble(tiedot.get("happovakio1"))); //jos on emäs niin silloin tämä on oikeasti Kb
        double nom = Math.sqrt(Math.pow(Ka,2) + 4*consent*Ka);
        double cons = Math.max((Ka-nom)/-2,(Ka+nom)/-2);
        double pal = Math.log10(1/cons);
        //jos on emäs, laskettiin pOH. pH = 14 - pOH
        if(tiedot.get("isHappo").compareTo("0") == 0)
        {
            pal = 14 - pal;
        }
        return pal;
    }



}
