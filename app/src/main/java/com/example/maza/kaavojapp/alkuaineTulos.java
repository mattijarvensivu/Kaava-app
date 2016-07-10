package com.example.maza.kaavojapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;


/**
 * Created by janne on 28.6.2016.
 */
public class alkuaineTulos extends Tulos {

    private ArrayList<isotooppiTulos> isotoopit;

    public alkuaineTulos (HashMap<String,String> values)
    {
        layoutLarge = R.layout.alkuaine_large;
        layoutSmall = R.layout.alkuaine_small;
        tiedot = values;
        isotoopit = new ArrayList<>();
        type = 1;
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = infl.inflate(layoutSmall, paren, false);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNumber)).setText(tiedot.get("jarjestyluku"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(Html.fromHtml(tiedot.get("nimi")));
        ((TextView)pal.findViewById(R.id.txvSymbol)).setText(tiedot.get("symbol"));
        ((TextView)pal.findViewById(R.id.txvMolar)).setText(tiedot.get("moolimassa"));

        return pal;
    }

    public void boldaa(String haku)
    {
        tiedot.put("nimi", StringValidator.boldaa(tiedot.get("nimi"),haku));
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä vievissä näytetään kaikki alkuaineen tiedot
    public View getLargeView (final LayoutInflater infl, final ViewGroup paren)
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
        ((TextView) pal.findViewById(R.id.txvNimiMain)).setText(tiedot.get("fiName"));
        TextView aSadeT = (TextView) pal.findViewById(R.id.txvTASField);
        TextView aSadeK = (TextView) pal.findViewById(R.id.txvKASField);
        LinearLayout IonE = (LinearLayout) pal.findViewById(R.id.lvoIonE);
        LinearLayout isot = (LinearLayout) pal.findViewById(R.id.lnlIsot);

        //luodaan olio joka tuottaa kuvan kaavasta. Parempi tehdä näin kuin kirjoittaa joka kerta ne rivit uudestaan.
        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(oMuoto.getTextSize()/ pal.getResources().getDisplayMetrics().density)); //viimeinen parametri laskee käytetyn teksti koon.
        //lisätään elektorni rakennetta kuvaava kuva. Periaatteessa tämän voisi hoita myös html tägeillä.
        ((ImageView) pal.findViewById(R.id.txvRakenneField)).setImageDrawable(kf.getBmD(tiedot.get("elektronirakenneLyhyt")));

        //luodaan ja asetetaan paikoillen celsius kuva
        BitmapDrawable celsius = kf.getBmD("C^{\\circ}");

        ((ImageView) pal.findViewById(R.id.mwSpUnit)).setImageDrawable(celsius);
        ((ImageView) pal.findViewById(R.id.mwKpUnit)).setImageDrawable(celsius);

        //asetetaan loput yksiköt paikoilleen
        ((ImageView)pal.findViewById(R.id.mwDenUnit)).setImageDrawable(kf.getBmD("\\frac{g}{dm^{3}}"));
        ((ImageView)pal.findViewById(R.id.mwOLUnit)).setImageDrawable(kf.getBmD("\\frac{J}{K*kg}"));
        ((ImageView)pal.findViewById(R.id.mwLJUnit)).setImageDrawable(kf.getBmD("\\frac{W}{K*m}"));


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
        IonE.removeAllViews(); //tarvitaanko? tuskin
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

        //Esitetään isotoopit
        //ei syystä tai toisesta hyväksy ArrayList<isotooppiTulos> parametrinä, täytyy olla ArrayList<Tulos>
        for(int i = 0; i < isotoopit.size(); i++)
        {
            View tmp = isotoopit.get(i).getSmallView(infl,isot);
            tmp.setClickable(true);
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String klikattuNimi = ((TextView)v.findViewById(R.id.txvParticles)).getText().toString();
                    for(int i = 0; i < isotoopit.size(); i++)
                    {
                        if(klikattuNimi.compareTo(isotoopit.get(i).getName()) == 0)
                        {
                            //löytyi klikattu isotooppi
                            paren.removeAllViews(); //Viite tähän olioon mahdollisesti katoaa kun tyhjennetään layoutti...
                            paren.addView(isotoopit.get(i).getLargeView(infl,paren));
                            break;
                        }
                    }
                }
            });
            isot.addView(tmp);
        }



        return pal;
    }

    public void addIsotoopit(ArrayList<Tulos> vals)
    {
        for(int i = 0; i < vals.size(); i++) {
            isotoopit.add((isotooppiTulos)vals.get(i));
        }

    }
}
