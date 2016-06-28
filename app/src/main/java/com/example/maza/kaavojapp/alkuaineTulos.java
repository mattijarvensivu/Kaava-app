package com.example.maza.kaavojapp;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import io.github.kexanie.library.MathView;

/**
 * Created by janne on 28.6.2016.
 */
public class alkuaineTulos extends Tulos {


    public alkuaineTulos (HashMap<String,String> values)
    {
        layoutLarge = R.layout.element_displayer_layout;
        layoutSmall = R.layout.element_displayer_small_layout;
        tiedot = values;

    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = infl.inflate(layoutSmall, paren, false);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNumber)).setText(tiedot.get("jarjestyluku"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(tiedot.get("nimi"));
        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));
        ((TextView)pal.findViewById(R.id.txvMolar)).setText(tiedot.get("moolimassa"));

        return pal;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä vievissä näytetään kaikki alkuaineen tiedot
    public View getLargeView (LayoutInflater infl, ViewGroup paren)
    {
        //määritellään desimaali formaatit pyöristyksiä varten
        DecimalFormat df4 = new DecimalFormat("#.####");
        DecimalFormat df2 = new DecimalFormat("#.##");

        View pal = infl.inflate(layoutLarge, paren, false);
        //laitetaan tiedot paikoilleen
        ((TextView) pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));
        ((TextView) pal.findViewById(R.id.txvENimi)).setText(tiedot.get("nimi"));
        ((TextView) pal.findViewById(R.id.txvJLuku)).setText(tiedot.get("jarjestyluku"));
        ((TextView) pal.findViewById(R.id.txvMMass)).setText(tiedot.get("moolimassa"));
        ((TextView) pal.findViewById(R.id.txvDenField)).setText(tiedot.get("tiheys"));
        ((TextView) pal.findViewById(R.id.txvKPField)).setText(df2.format(Float.parseFloat(tiedot.get("kiehumispiste")))); //luku pyöristetään. tarkkuus(2) lienee sopiva
        ((TextView) pal.findViewById(R.id.txvSPField)).setText(df2.format(Float.parseFloat(tiedot.get("sulamispiste")))); //kuten yllä
        ((TextView) pal.findViewById(R.id.txvENegField)).setText(tiedot.get("elektronegatiivisuus"));
        ((TextView) pal.findViewById(R.id.txvHLField)).setText(Html.fromHtml(tiedot.get("hapettumisluvut"))); //Kutsutaan html formatointia, koska tämä on helpoin tapa lihavoida pätkiä tekstissä. Ei kannata ruveta rakentamaan mathViewiä tähän
        ((TextView) pal.findViewById(R.id.txvOmLField)).setText(tiedot.get("ominaislampokapasiteetti"));
        ((TextView) pal.findViewById(R.id.txvLJField)).setText(tiedot.get("lammonjohtavuus"));
        TextView oMuoto =(TextView) pal.findViewById(R.id.txvOMField);
        TextView luokka =(TextView) pal.findViewById(R.id.txvLuokka);
        ((MathView) pal.findViewById(R.id.txvRakenneField)).setText(tiedot.get("elektronirakenneLyhyt"));
        ((TextView) pal.findViewById(R.id.txvNimiMain)).setText(tiedot.get("fiName"));
        TextView aSadeT = (TextView) pal.findViewById(R.id.txvTASField);
        TextView aSadeK = (TextView) pal.findViewById(R.id.txvKASField);
        LinearLayout IonE = (LinearLayout) pal.findViewById(R.id.lvoIonE);

        //muutetaan atomisäteen no data suomenkieliseksi ja lisätään yksikkö
        if (tiedot.get("atomiSadeTheo").compareTo("no data") == 0) {
            aSadeT.setText("Ei tietoa");
        } else {
            aSadeT.setText(tiedot.get("atomiSadeTheo") + "pm");
        }

        if (tiedot.get("atomiSadeEmpi").compareTo("no data") == 0) {
            aSadeK.setText("Ei tietoa");
        } else {
            aSadeK.setText(tiedot.get("atomiSadeEmpi") + "pm");
        }

        //olomuoto ja luokka on koodattu tietokannassa. Tässä numero muutetaan sanaksi
        switch (Integer.parseInt(tiedot.get("olomuoto"))) {
            case 1:
                oMuoto.setText("kiinteä");
                break;

            case 2:
                oMuoto.setText("neste");
                break;

            case 3:
                oMuoto.setText("kaasu");
                break;
        }

        switch (Integer.parseInt(tiedot.get("luokka"))) {
            case 0:
                luokka.setText("alkaali metalli");
                break;

            case 1:
                luokka.setText("maa-alkaali metalli");
                break;

            case 2:
                luokka.setText("siirtymä metalli");
                break;

            case 3:
                luokka.setText("muu metalli");
                break;

            case 4:
                luokka.setText("puoli metalli");
                break;

            case 5:
                luokka.setText("lantanoidi");
                break;

            case 6:
                luokka.setText("aktanoidi");
                break;

            case 7:
                luokka.setText("epämetalli");
                break;

            case 8:
                luokka.setText("halogeeni");
                break;

            case 9:
                luokka.setText("jalokaasu");
                break;
        }

        //käsitellään ionisaatio energiat.
        //jos IE != 0, on kyseinen ionisaatio energia olemassa. Lisätään textWiev listaan.
        IonE.removeAllViews();
        String[] ies = {tiedot.get("ionisaatioenergia1"), tiedot.get("ionisaatioenergia2"), tiedot.get("ionisaatioenergia3"), tiedot.get("ionisaatioenergia4")};

        for (int i = 0; i < ies.length; i++) {
            if (Float.parseFloat(ies[i]) != 0) {
                TextView textView = new TextView(paren.getContext());
                textView.setText((i + 1) + ": " + df4.format(Float.parseFloat(ies[i])));
                LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); //width, height
                textView.setLayoutParams(textViewLayoutParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Tulee tarkastella onko tämä järkevä ratkaisu. Onko api 23 liian uusi?
                    textView.setTextAppearance(android.R.style.TextAppearance_Large);
                }
                IonE.addView(textView);
            }


        }

        return pal;
    }
}
