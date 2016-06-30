package com.example.maza.kaavojapp;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;

import io.github.kexanie.library.MathView;

/**
 * Created by janne on 29.6.2016.
 */
public class happoTulos extends Tulos {


    public happoTulos(HashMap<String,String> vals)
    {
        tiedot = vals;
        layoutSmall = R.layout.happo_small_layout;
        layoutLarge = R.layout.happo_large_layout;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        DecimalFormat df2 = new DecimalFormat("#.##");

        View pal = infl.inflate(layoutSmall, paren, false);
        MathView kaavaIk = (MathView)pal.findViewById(R.id.mvKaava);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get("name"));
        ((TextView)pal.findViewById(R.id.txvPKa)).setText(tiedot.get("happovakio1"));
        kaavaIk.setText(tiedot.get("kaava"));
        ((TextView)pal.findViewById(R.id.txvPh)).setText(df2.format(calculatePh(1))+"");

        //diseiblataan mathViewin onTouch jotta voidaan klikata riviä. Ei kovin hyvä jos se tulee tehdä tälleesti
        //Ilmeisesti johtuu webViewistä, mistä mathView on peritty.
        kaavaIk.setClickable(false);
        kaavaIk.setLongClickable(false);
        kaavaIk.setFocusable(false);
        kaavaIk.setFocusableInTouchMode(false);

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
        ((MathView)pal.findViewById(R.id.mvKaava)).setText(tiedot.get("kaava"));
        ((TextView)pal.findViewById(R.id.txvMoolimassa)).setText(tiedot.get("moolimassa"));
        ((TextView)pal.findViewById(R.id.txvDensity)).setText(tiedot.get("tiheys"));
        ((TextView)pal.findViewById(R.id.txvSp)).setText(tiedot.get("sulamispiste"));
        ((TextView)pal.findViewById(R.id.txvKp)).setText(tiedot.get("kiehumispiste"));
        phView.setText(df2.format(calculatePh(1))+"");
        LinearLayout mainLay = (LinearLayout)pal.findViewById(R.id.lnMain);

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

        return pal;
    }

    //lasketaan tämän hapon pH annetulla konsentraatiolla. consent, eli hapon konsentraatio tulee antaa yksikössä mol/l. ATM käsittelee kaikkia happoja yhden arvoisina!
    public double calculatePh (float consent)
    {
        if (consent == 0) return -1;
        double Ka = Math.pow(10, -1*Double.parseDouble(tiedot.get("happovakio1")));
        double nom = Math.sqrt(Math.pow(Ka,2) + 4*consent*Ka);
        double cons = Math.max((Ka-nom)/-2,(Ka+nom)/-2);
        return Math.log10(1/cons);
    }



}
