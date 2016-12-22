package com.Kaavapp.android;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by maza on 7/4/2016.
 */
public class StringValidator {


    public Boolean CheckString(String tarkastettava){

        Boolean hyvaksytty = false;
        Pattern p = Pattern.compile("[^a-z0-9, %äö]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(tarkastettava);
        hyvaksytty = m.find();
        hyvaksytty = !hyvaksytty;
        return hyvaksytty;

    }

    public static String boldaa (String initial, String hakuString)
    {
        String pal = "";
        int alkuPiste = initial.indexOf(hakuString);
        if(alkuPiste == -1) return initial;
        //on olemassa instanssi
        //poistetaan edeltävä osa
        pal = initial.substring(0,alkuPiste) + "<b>" + hakuString + "</b>" + boldaa(initial.substring(alkuPiste+hakuString.length(),initial.length()),hakuString);

        return pal;

    }
}
