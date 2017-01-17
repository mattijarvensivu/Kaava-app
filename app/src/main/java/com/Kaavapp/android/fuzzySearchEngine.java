package com.Kaavapp.android;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by janne on 11.1.2017.
 */
public class FuzzySearchEngine {

    private HashMap<Integer,ArrayList<String>> indeksi;
    private int maxEDT = 4;

    public FuzzySearchEngine()
    {
        indeksi = new HashMap<>();
    }

    public void setIndeksi(ArrayList<Tulos> names)
    {
        indeksi = new HashMap<>();
        ArrayList<String> stringit = new ArrayList<>();
        ArrayList<String> tagit = new ArrayList<>();
        for(Tulos t: names)
        {
            stringit.add(t.getNimi());
            stringit.addAll(t.getTagit());
        }
        stringit = getUnique(stringit);
        for(String s: stringit)
        {
            if(indeksi.get(s.length()) == null) indeksi.put(s.length(), new ArrayList<String>());
            indeksi.get(s.length()).add(s);
        }
        Log.d("SEDTesti","annettu: enegy. lähin: " + findClosest("enegy"));
    }

    private ArrayList<String> getUnique(ArrayList<String> raw)
    {
        ArrayList<String> pal = new ArrayList<>();
        HashMap<Integer,ArrayList<String>> jarj = new HashMap<>();
        String prev = "EI EDELLISTÄ";
        int i = 0;
        for(String s:raw)
        {
            /* TESTI. POISTA RELEASISTA
            i = raw.indexOf(s);
            if(s == null)
            {
                Log.d("SEDTesti","Null arvo, edellinen " + prev);
            }
            */
            if(jarj.get(s.length()) == null) jarj.put(s.length(), new ArrayList<String>());
            boolean lisataan = true;
            for(String ss: jarj.get(s.length()))
            {
                lisataan = lisataan && !(ss.compareTo(s) == 0);
            }
            if(lisataan)
            {
                jarj.get(s.length()).add(s);
                pal.add(s);
            }
            /* TESTI. POISTA RELEASESTA
            prev = s;
            */

        }
        return pal;

    }


    //Lasketaan shortest edit distance 2 stringin välillä käyttäen sitä videon metodia. Tätä voi varmasti tehostaa.
    private int calculateSED(String s1, String s2, int best)
    {
        //alustetaan taulu
        int[][] taulu = new int[s1.length()+1][s2.length()+1];
        //lasketaan SED null stringiä vastaan jokaiselle osa stringille.
        for(int i = 0; i < taulu[0].length; i++)
        {
            taulu[0][i] = i;
        }
        for(int i = 0; i < taulu.length; i++)
        {
            taulu[i][0] = i;
        }
        //aloitetaan algoritmi
        int edRivinPienin = 0;
        for(int i = 1; i < taulu.length; i++)
        {
            for(int j = 1; j < taulu[i].length; j++)
            {
                if(Math.min(edRivinPienin,taulu[i][j-1]) > best )
                {
                    //kaikki ylemmän rivin, ja tämän rivin eka numero ovat yli maxEDT -> ei ole mahdollista että SED =< maxEDT
                    //Ei tunneta kuitenkaan todellista SED! mutta ei haittaa.
                    return Integer.MAX_VALUE;
                }
                //tarkistetaan ovatko kirjaimet samoja
                if(s1.charAt(i-1) == s2.charAt(j-1))
                {
                    //kirjaimet ovat samoja, otetaan diagonaali suoraan
                    taulu[i][j] = taulu[i-1][j-1];
                }else{
                    //kirjaimet eivät ole samoja. otetaan pienin ympäröivä +1
                    //verrataan aluksi suoraan yläpuolella olevaa, [-1][0], ja luoteessa olevaa, [-1][-1]. Valitaan näistä pienin.
                    //verrataan tätä suoraan vasemalla olevaan [0][-1] ja valitaan pienin.
                     taulu[i][j] = Math.min(Math.min(taulu[i-1][j],taulu[i-1][j-1]),taulu[i][j-1])+1;
                }
            }
        }
        //tulos on alin solu oikealla.
        return taulu[s1.length()][s2.length()];
    }

    public String findClosest (String given)
    {
        int best = maxEDT;
        String bestStr = null;
        boolean jatketaan = true;
        int dPit = 0;
        while(jatketaan)
        {
             if(indeksi.get(given.length() + dPit) != null)
             {
                 //array on olemassa
                 for(String s: indeksi.get(given.length() + dPit))
                 {
                     //lasketaan SED yhden stringin suhteen
                     int ehdokas = calculateSED(given, s, best);
                     if(ehdokas <= best)
                     {
                         best = ehdokas;
                         bestStr = s;
                     }
                 }
             }
            //hieman tyhmä tapa...
            if(indeksi.get(given.length() - dPit) != null)
            {
                //array on olemassa
                for(String s: indeksi.get(given.length() - dPit))
                {
                    //lasketaan SED yhden stringin suhteen
                    int ehdokas = calculateSED(given, s, best);
                    if(ehdokas <= best)
                    {
                        best = ehdokas;
                        bestStr = s;
                    }
                }
            }
            dPit++;
            Log.d("FSETesti","Paras arvo:" + best + " jonka stringi on " + bestStr + " ja dPit on " + dPit);
            if(dPit > best) return bestStr; //seuraavassa tasossa tulisi lisätä tai poistaa enemmän kirjaimia kuin tämänhetkinen SED parhaaseen sanaan.
            if(indeksi.get(given.length() + dPit) == null && indeksi.get(given.length() - dPit) == null) return bestStr; //ei ole olemassa sanoja joiden pituus olisi given.l +- dPit
        }
        return null; //tähän ei pitäisi koskaan tulla!

    }



}
