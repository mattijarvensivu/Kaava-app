package com.Kaavapp.android;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janne on 27.6.2016.
 * Olio joka pitää siällään yhden rivin tiedot. Tarkoitus on että tästä luokasta peritään jokaiselle taululle oma Tulos olio variantti
 * Tärkein (ainut) tehtävä on antaa UI:lle sen haluamat view komponentit jossa esitetään olion, eli rivin, sisältämä tieto
 */
public class Tulos {

    protected int layoutSmall;
    protected int layoutLarge;
    protected HashMap<String,String> tiedot;
    protected String type = "raw";
    protected boolean isSuosikki = false;

    protected String taulu;
    protected String tagiTaulu;
    protected String linkkiTaulu;

    protected suosikkiToggleListener suosikkiToggle;

    protected ArrayList<String> tagit;


    public Tulos()
    {
        taulu = "raw";
    }

    public Tulos(HashMap<String,String> vals)
    {
        tiedot = vals;
    }


    //tällä metodilla luodaan oikean tyyppinen tulos luokka.
    @Nullable
    public static Tulos getTulos(HashMap<String,String> values)
    {
        //tässä rikotaan hieman suunnittelu periaatetta jonka mukaan ohjelman tulisi maagisesti tuntea tietokantaa mahdollisimman vähän.
        //jos halutaan kuitenkin luoda spesifit tulos luokat, en keksi parempaa tapaa toteuttaa tätä.
        switch (values.size())
        {
            case 23: //kyseesssä alkuaine
                return new alkuaineTulos(values);
            case 8:
                return new isotooppiTulos(values);
            case 4:
                if(values.get("_ryhmaid") != null)
                    return new funktionaalinenTulos(values); //funktionaalinen ryhmä
                if(values.get("_kaavaid") != null)
                    return new kaavaTulos(values); //kaava
                return new muuttujaTulos(values);//muuttuja

            case 5:
                if(values.get("yksikko") != null)
                {
                    if(values.get("yksikko").compareTo("trigKey") == 0 )
                        return new triFunMasterTulos(values);
                    return new vakioTulos(values);//vakio
                }
                if(values.get("_aste") != null) return new Tulos(values); //trigonometrisetArvot tulos
                return new ioniTulos(values);

            case 6:
                return new piikkiTulos(values);
            case 13:
                return new happoTulos(values);
            case 10:
                return new aineTulos(values);
            default:
                Log.d("Tulos virhe","Palautetaan default tulos");
                return new Tulos(values); //mahdollisesti vaarallista, mutta toisaalta mahdollistetaan puhtaan tiedon siirtely.
        }

    }

    public void setOnSuosikkiToggleListener(suosikkiToggleListener s)
    {
        suosikkiToggle = s;
    }

    public String getType()
    {
        return type;
    }

    //super metodi jonka tehtävä on huolehtia suosikki tähdestä.
    public View getSmallView(LayoutInflater infl, ViewGroup paren)
    {
        View pal = infl.inflate(layoutSmall, paren, false);
        //laitetaan tähdelle onClick logiikka
        ImageView tahti = (ImageView)pal.findViewById(R.id.imgSuosikki);
        if(tahti != null) {
            tahti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleSuosikki(v);
                }
            });
            setTahti(pal); //siirrettiin tämä äsken tänne tuosta ulkoa
        }
        return pal;
    }

    public View getLargeView(LayoutInflater infl, ViewGroup paren)
    {
        Log.d("minun","ollaan super tulos metodissa");
        return null;
    }

    public String getValue(String name)
    {
        return tiedot.get(name);

    }

    //palautetaan onko tämä asetettu suosikiksi.
    //nimeäminen vähän so-so. Vaarana on että sekoitetaan itse muuttujaan. Jos rupee ahistaan niin sitten muutetaan
    //TARVITAANKO!!!
    public boolean isSuosikki()
    {
        return isSuosikki;
    }

    //asetetaan tägit. Samalla tarkistetaan onko tämä suosikki (onko tällä suosikki tägi)
    public void setTagit(ArrayList<String> tagit)
    {

        this.tagit = tagit;
        if(tagit == null) tagit = new ArrayList<>();
        if(tagit.indexOf("suosikki") >= 0) isSuosikki = true;
    }

    //asetetaan oikea tähden grafiikka
    protected void setTahti(View target)
    {
        //haetaan oikea tähti
        int tahti = R.drawable.tyhjatahti;
        if(isSuosikki) tahti = R.drawable.taysitahti;

        //asetetaan tähti näkyviin
        ImageView tahtiImage = (ImageView)target.findViewById(R.id.imgSuosikki);
        if(tahtiImage == null) return; //tarkistetaan että tähti on olemassa.
        if(suosikkiToggle == null) return; //katsotaan onko listeneri togglelle. Jos ei ole, oletetaan ettei ole mahdolista asettää tätä suosikiksi
        tahtiImage.setImageResource(tahti);
    }

    //tähden tilaa on muutettu. Toimitaan.
    protected void toggleSuosikki(View v)
    {
        isSuosikki = !isSuosikki;
        setTahti(v);
        suosikkiToggle.onToggleSuosikki(this); //kerrotaan listenerille että suosikki status on muuttunut
    }

    public String getTaulu(){ return taulu;}


}

interface suosikkiToggleListener
{
    void onToggleSuosikki(Tulos kohde);
}
