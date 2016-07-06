package com.example.maza.kaavojapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import io.github.kexanie.library.MathView;

/**
 * Created by maza on 7/5/2016.
 */
public class kaavaTulos extends Tulos{

    //onko järkevää. Nyt luodaan potentiaalisesti suuri määrä täysin samanlaisia olioita. Jos palautetaan kaikki kaavat, luodaan jokaiselle kaavalle oma instanssinsa muttujista ja vakioista
    //paras tapaan olisi hakea muuttujat ja vakiot vasta kun sitä tarvitaan.
    private ArrayList<Tulos> vakiot;
    private ArrayList<Tulos> muuttujat;

    public kaavaTulos(HashMap<String,String> values) {
        layoutLarge = -1;
        layoutSmall = -1;
        tiedot = values;
    }

}

