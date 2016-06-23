package com.example.maza.kaavojapp;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.HashMap;

import io.github.kexanie.library.MathView;

/**
 * Created by janne on 20.6.2016.
 */
public class elementsDisplayer extends LinearLayout {


    //otetaan kentät johon laitetaan dataaa muistiin
    TextView symbol;
    TextView nimi;
    TextView jLuku;
    TextView mooliMassa;
    TextView tiheys;
    TextView kiehumis;
    TextView sulamis;
    TextView eNeg;
    TextView hLuvut;
    TextView oLamp;
    TextView lJohto;
    TextView oMuoto;
    TextView luokka;
    MathView eRakenne;
    TextView fiNimi;
    TextView aSadeT;
    TextView aSadeK;
    LinearLayout IonE;

    public elementsDisplayer(Context context, AttributeSet atri) {
        super(context,atri);

        //suoraan netistä kopioitu. aka arcane knowledge
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.element_displayer_layout, this, true); //tässä ilmeisesti linkataan .xml tiedosto tähän luokkaan.
        //tästä alkaa oma koodi

        //alustetaan kentät johon laitetaan tietoa
        symbol = (TextView) findViewById(R.id.txvSymbol);
        nimi= (TextView) findViewById(R.id.txvENimi);
        jLuku= (TextView) findViewById(R.id.txvJLuku);
        mooliMassa= (TextView) findViewById(R.id.txvMMass);
        tiheys= (TextView) findViewById(R.id.txvDenField);
        kiehumis= (TextView) findViewById(R.id.txvKPField);
        sulamis= (TextView) findViewById(R.id.txvSPField);
        eNeg= (TextView) findViewById(R.id.txvENegField);
        hLuvut= (TextView) findViewById(R.id.txvHLField);
        oLamp= (TextView) findViewById(R.id.txvOmLField);
        lJohto= (TextView) findViewById(R.id.txvLJField);
        oMuoto= (TextView) findViewById(R.id.txvOMField);
        luokka= (TextView) findViewById(R.id.txvLuokka);
        eRakenne= (MathView) findViewById(R.id.txvRakenneField);
        fiNimi= (TextView) findViewById(R.id.txvNimiMain);
        aSadeT= (TextView) findViewById(R.id.txvTASField);
        aSadeK= (TextView) findViewById(R.id.txvKASField);
        IonE = (LinearLayout) findViewById(R.id.lvoIonE);
        resetFields();
    }

    //esitetään kentät. On tärkeää että tälle annetaan alkuaineet taulun hashmappi.
    public void displayInfo(HashMap<String,String> values) {
        //määritellään desimaali formaatit pyöristyksiä varten
        DecimalFormat df4 = new DecimalFormat("#.####");
        DecimalFormat df2 = new DecimalFormat("#.##");

        symbol.setText(values.get("symbol"));
        nimi.setText(values.get("nimi"));
        jLuku.setText(values.get("jarjestyluku"));
        mooliMassa.setText(values.get("moolimassa"));
        tiheys.setText(values.get("tiheys"));
        kiehumis.setText(df2.format(Float.parseFloat(values.get("kiehumispiste")))); //luku pyöristetään. tarkkuus(2) lienee sopiva
        sulamis.setText(df2.format(Float.parseFloat(values.get("sulamispiste")))); //kuten yllä
        eNeg.setText(values.get("elektronegatiivisuus"));
        hLuvut.setText(Html.fromHtml(values.get("hapettumisluvut"))); //Kutsutaan html formatointia, koska tämä on helpoin tapa lihavoida pätkiä tekstissä. Ei kannata ruveta rakentamaan mathViewiä tähän
        oLamp.setText(values.get("ominaislampokapasiteetti"));
        lJohto.setText(values.get("lammonjohtavuus"));
        eRakenne.setText(values.get("elektronirakenneLyhyt"));
        fiNimi.setText(values.get("fiName"));

        //muutetaan atomisäteen no data suomenkieliseksi ja lisätään yksikkö
        if (values.get("atomiSadeTheo").compareTo("no data") == 0) {
            aSadeT.setText("Ei tietoa");
        } else {
            aSadeT.setText(values.get("atomiSadeTheo") + "pm");
        }

        if (values.get("atomiSadeEmpi").compareTo("no data") == 0) {
            aSadeT.setText("Ei tietoa");
        } else {
            aSadeK.setText(values.get("atomiSadeEmpi") + "pm");
        }

        //olomuoto ja luokka on koodattu tietokannassa. Tässä numero muutetaan sanaksi
        switch (Integer.parseInt(values.get("olomuoto"))) {
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

        switch (Integer.parseInt(values.get("luokka"))) {
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
        String[] ies = {values.get("ionisaatioenergia1"), values.get("ionisaatioenergia2"), values.get("ionisaatioenergia3"), values.get("ionisaatioenergia4")};

        for (int i = 0; i < ies.length; i++) {
            if (Float.parseFloat(ies[i]) != 0) {
                TextView textView = new TextView(this.getContext());
                textView.setText((i + 1) + ": " + df4.format(Float.parseFloat(ies[i])));
                LayoutParams textViewLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT); //width, height
                textView.setLayoutParams(textViewLayoutParams);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //Tulee tarkastella onko tämä järkevä ratkaisu. Onko api 23 liian uusi?
                    textView.setTextAppearance(android.R.style.TextAppearance_Large);
                }
                IonE.addView(textView);
            }


        }
    }

    private void resetFields()
    {
        symbol.setText("");
        nimi.setText("");
        jLuku.setText("");
        mooliMassa.setText("");
        tiheys.setText("");
        kiehumis.setText(""); //luku pyöristetään. tarkkuus(2) lienee sopiva
        sulamis.setText(""); //kuten yllä
        eNeg.setText("");
        hLuvut.setText(""); //Kutsutaan html formatointia, koska tämä on helpoin tapa lihavoida pätkiä tekstissä. Ei kannata ruveta rakentamaan mathViewiä tähän
        oLamp.setText("");
        lJohto.setText("");
        eRakenne.setText("");
        fiNimi.setText("");
        IonE.removeAllViews();
        luokka.setText("");
        aSadeK.setText("");
        aSadeT.setText("");

    }



}
