package com.example.maza.kaavojapp;

import java.util.HashMap;

/**
 * Created by janne on 7/5/2016.
 */
public class muuttujaTulos extends Tulos{


    public muuttujaTulos(HashMap<String,String> values) {
        layoutLarge = -1;
        layoutSmall = -1;
        tiedot = values;
    }
}

