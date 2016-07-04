package com.example.maza.kaavojapp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maza on 7/4/2016.
 */
public class StringValidator {


    public Boolean CheckString(String tarkastettava){

        Boolean hyvaksytty = false;
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(tarkastettava);
        hyvaksytty = m.find();
        hyvaksytty = !hyvaksytty;
        return hyvaksytty;

    }
}
