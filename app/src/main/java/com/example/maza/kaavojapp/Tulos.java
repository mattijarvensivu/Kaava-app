package com.example.maza.kaavojapp;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    protected int type = 0;

    public Tulos()
    {

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
            case 11: //kyseessä happo. Ei kuitenkaan vielä kuva kenttää
                return new happoTulos(values);
            case 8:
                return new isotooppiTulos(values);
            case 3:
                return new kaavaTulos(values); //kaava
            case 4:
                if(values.get("_ryhmaid") != null)
                    return new funktionaalinenTulos(values); //funktionaalinen ryhmä
                return new muuttujaTulos(values);//muuttuja

            case 5:
                return new vakioTulos(values);//vakio

            case 6:
                return new piikkiTulos(values);
            default:
                return null;
        }

    }

    public int getType()
    {
        return type;
    }

    public View getSmallView(LayoutInflater infl, ViewGroup paren)
    {
        Log.d("minun","ollaan super tulos metodissa");
        return null;
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


}
