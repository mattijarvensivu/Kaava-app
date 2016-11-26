package com.Kaavapp.android;

import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by janne on 28.6.2016.
 */
public class alkuaineTulos extends Tulos {

    private ArrayList<isotooppiTulos> isotoopit;
private String nimiarvo;
    public alkuaineTulos (HashMap<String,String> values)
    {
        layoutLarge = R.layout.alkuaine_large;
        layoutSmall = R.layout.alkuaine_small;
        tiedot = values;
        isotoopit = new ArrayList<>();

        type = "Alkuaineet";
        taulu = "Alkuaineet";
        tagiTaulu = "AlkuaineetTag";
        linkkiTaulu = "Alkuaineet_tag";
    }

    @Override
    //tuottaa Viewin annetulla inflaterillä ja isännällä. Tässä viewissä näytetään nopea yhteen veto tuloksesta
    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {

        if(checkLanguage()){
           this.nimiarvo = "nimi";
        }else{
            this.nimiarvo = "fiName";

        }
        Log.d("NAME", this.nimiarvo);
        View pal = super.getSmallView(infl, paren);
        //asetetaan tiedot paikoilleen
        ((TextView)pal.findViewById(R.id.txvNumber)).setText(tiedot.get("jarjestyluku"));
        ((TextView)pal.findViewById(R.id.txvName)).setText(Html.fromHtml(tiedot.get(this.nimiarvo)));
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
        String url;
        if(this.isEnglish) {
            url = "https://en.m.wikipedia.org/wiki/"+ tiedot.get("nimi");
        }else{
            url = "https://fi.m.wikipedia.org/wiki/"+ tiedot.get("fiName");
        }
        View pal = infl.inflate(layoutLarge, paren, false);

        Log.d("URL!!!!!!!!!!!!!", url);
        final WebView myWebView = (WebView) pal.findViewById(R.id.webview);
        myWebView.loadUrl(url);

        Button wikibutton = (Button) pal.findViewById(R.id.wikibutton);
        wikibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myWebView.getVisibility()==View.GONE){
                    myWebView.setVisibility(View.VISIBLE);
                }else{
                    myWebView.setVisibility(View.GONE);
                }

            }
        });


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

        if(!this.isEnglish) {
            ((TextView) pal.findViewById(R.id.txvNimiMain)).setText(tiedot.get("fiName"));
        }else{
            ((TextView) pal.findViewById(R.id.txvNimiMain)).setVisibility(View.GONE);
        }
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
            aSadeT.setText(R.string.eiTietoa);
        } else {
            aSadeT.setText(tiedot.get("atomiSadeTheo") + "pm");
        }

        if (tiedot.get("atomiSadeEmpi").compareTo("no data") == 0) {
            aSadeK.setText(R.string.eiTietoa);
        } else {
            aSadeK.setText(tiedot.get("atomiSadeEmpi") + "pm");
        }

        //olomuoto ja luokka on koodattu tietokannassa. Tässä numero muutetaan sanaksi
        switch (Integer.parseInt(tiedot.get("olomuoto"))) {
            case 1:
                oMuoto.setText(R.string.kiintea);
                break;

            case 2:
                oMuoto.setText(R.string.neste);
                break;

            case 3:
                oMuoto.setText(R.string.kaasu);
                break;
        }

        switch (Integer.parseInt(tiedot.get("luokka"))) {
            case 0:
                luokka.setText(R.string.alkaaliMetalli);
                break;

            case 1:
                luokka.setText(R.string.maaAlkaaliMetalli);
                break;

            case 2:
                luokka.setText(R.string.siirtymaMetalli);
                break;

            case 3:
                luokka.setText(R.string.muuMetalli);
                break;

            case 4:
                luokka.setText(R.string.puoliMetalli);
                break;

            case 5:
                luokka.setText(R.string.lantanoidi);
                break;

            case 6:
                luokka.setText(R.string.aktanoidi);
                break;

            case 7:
                luokka.setText(R.string.epametalli);
                break;

            case 8:
                luokka.setText(R.string.halogeeni);
                break;

            case 9:
                luokka.setText(R.string.jalokaasu);
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
        if(!dataHaettu) GAD.findData(this); //haetaan isotoopit vain jos niiden lista on tyhjä
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

        Locale current = paren.getResources().getConfiguration().locale;
        Log.d("minun", current.getDisplayLanguage());


        return pal;
    }

    public void addIsotoopit(ArrayList<Tulos> vals)
    {
        dataHaettu = true;
        for(int i = 0; i < vals.size(); i++) {
            isotoopit.add((isotooppiTulos)vals.get(i));
        }

    }
}
