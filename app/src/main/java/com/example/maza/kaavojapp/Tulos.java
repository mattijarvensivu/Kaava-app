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


    public Tulos()
    {

    }


    //tällä metodilla luodaan oikean tyyppinen tulos luokka.
    @Nullable
    public static Tulos getTulos(HashMap<String,String> values)
    {
        switch (values.size())
        {
            case 23: //kyseesssä alkuaine
                return new alkuaineTulos(values);
            default:
                return null;
        }

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


}
