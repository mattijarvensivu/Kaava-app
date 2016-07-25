package com.example.maza.kaavojapp;

import android.util.Log;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by janne on 22.7.2016.
 */
public class Historia {

    private HashMap<String,Float> tauluOsumat;
    private String[] listOfTables;
    private float partialValue = 0.1f;
    private HashMap<String,HashMap<String,Float>> riviOsumat;

    public Historia()
    {
        listOfTables = new String[]{"Alkuaineet","Funktionaalinenryhma","Hapot","Isotoopit","Kaava","Muuttuja","Vakio"}; //tässäkin järjestys tärkeä... tulisi pyrkiä pois siitä.
        tauluOsumat = new HashMap<>(); //TÄRKEÄÄ. alkion paikka määrittää mitä taulua se vastaa. indeks i = taulunTyyppiKoodi-1. Toisin sanoen, ÄLÄ MUUTA TAULUN ALKIOIDEN PAIKKAA
        float[] testiArvot = new float[]{2,5,10,1,0,0,0}; //VAIN TESTAAMISEEN!!!
        riviOsumat = new HashMap<>();
        for(int i = 0; i < listOfTables.length; i++)
        {
            riviOsumat.put(listOfTables[i],new HashMap<String, Float>());
            tauluOsumat.put(listOfTables[i],testiArvot[i]);
        }
        riviOsumat.get("Hapot").put("htesti",10f);
        riviOsumat.get("Hapot").put("h2testi",15f);
    }

    public void wasSelected(Tulos t)
    {

        addScore(t,1);
    }

    public void addPartial(Tulos t)
    {
        addScore(t,partialValue);
    }

    private void addScore(Tulos t, float val)
    {
                tauluOsumat.put(t.getType(),tauluOsumat.get(t.getType()) + val);
        //tarkistetaan onko tulos korkeassa kategoriassa... tulee laskea avg ja sd
        double avg = calculateAvg();
        double sd = calculateSD();
        if(tauluOsumat.get(t.getType()) - avg > sd)
        {
            //kyseinen taulu on korkean käyttö asteen taulu lisätään osuma tuloksen nimeen
            Float vanhaVal = riviOsumat.get(t.getType()).get(t.getKeyField());
            if(vanhaVal == null) vanhaVal = 0f;
            float uusiVal = vanhaVal + val; //ei periaatteessa tarviis luoda muuttujaa... mutta ehkä helpompi lukea näin, ei tuu monsteri kutsuja
            riviOsumat.get(t.getType()).put(t.getKeyField(),uusiVal);

        }
    }

    //lasketaan käytettyjen taulujen standardi poikkeama
    private double calculateSD()
    {

        double avg = calculateAvg();
        //lasketaan varianssi (yksittäisten arvojen keskiarvosta poikkeamien neliöiden keskiarvo)
        float variance = 0;
        for(String s:listOfTables)
        {
            variance += Math.pow((tauluOsumat.get(s)-avg),2);
        }
        variance = variance / listOfTables.length;
        //lasketaan standardi poikkeama ottamalla varianssista neliöjuuri
        return Math.sqrt(variance);
    }

    public double calculateAvg()
    {
        //lasketaan tauluOsumat keskiarvo
        float avg = 0;
        for(String s : listOfTables)
        {
            avg += tauluOsumat.get(s);
        }
        return avg/listOfTables.length;
    }

    public String[] getTables()
    {
        return listOfTables;
    }


    private void invertArray(ArrayList<Object> a)
    {
        int lo = 0;
        int hi = a.size()-1;
        while(lo < hi)
        {
            swap(a,lo, hi);
            hi--;
            lo++;
        }
    }

    public ArrayList<Tulos> sortTulos(ArrayList<Tulos> initial)
    {
        //lajitellaan taulut niiden käyttö asteen mukaan
        double sd = calculateSD();
        double avg = calculateAvg();
        ArrayList<Object> greater = new ArrayList<>();
        ArrayList<Object> less = new ArrayList<>();
        ArrayList<Object> eq = new ArrayList<>();
        for(String s: listOfTables)
        {
            if(tauluOsumat.get(s)-avg >= sd)
            {
                greater.add(s);
            }else if(avg - tauluOsumat.get(s) >= sd) //onko alkio enemmän kuin 1 sd pienempi kuin avg = onko avg 1 sd isompi kuin alkio
            {
                less.add(s);
            }else{
                eq.add(s);
            }
        }
        //Järjestetään eniten käytetyt taulut niiden käyttö kertojen mukaan
        qsort(greater,tauluOsumat,0,greater.size()-1);
        //haetaan tuloksesta korkean käyttöasteen taulujen tulokset, ja järjestetään ne jokaisen taulun sisällä
        ArrayList<Object> greaterTulokset = new ArrayList<>();
        ArrayList<Tulos> pal = new ArrayList<>();
        for(Object o: greater) {
            for (Tulos t : initial) {
                if (t.getType().compareTo(o.toString()) == 0) {
                    greaterTulokset.add(t);
                }
            }
            qsort(greaterTulokset, riviOsumat.get(o.toString()), 0, greaterTulokset.size() - 1);
            invertArray(greaterTulokset);
            for (Object oo : greaterTulokset) {
                pal.add((Tulos) oo);
            }
            greaterTulokset.clear();
        }
        //lisätään normaalin prioriteetin tulokset listaan
        for(Object o: eq) {
            for (Tulos t : initial) {
                if (t.getType().compareTo(o.toString()) == 0) {
                    pal.add(t);
                }
            }
        }
        //lisätään matalan prioriteetin tulokset. niin että vähiten käytetty taulun tulokset viimeisenä
        qsort(less,tauluOsumat,0,less.size()-1);
        invertArray(less);
        for(Object o: less) {
            for (Tulos t : initial) {
                if (t.getType().compareTo(o.toString()) == 0) {
                    pal.add(t);
                }
            }
        }

        return pal;
    }




    //tästä alaspäin Quicksort toteutusta
    private void qsort(ArrayList<Object> a,HashMap<String,Float> data,int lo, int hi)
    {
        if(lo < hi)
        {
            int p = partition(a,data,lo, hi);
            qsort(a,data,lo, p);
            qsort(a,data,p+1, hi);
        }
    }



    private int partition(ArrayList<Object> a,HashMap<String,Float> data,int lo, int hi)
    {
        float p;
        try {
           p  = data.get(a.get(lo).toString());
        }catch (NullPointerException e)
        {
            p = 0;
        }

        int i = lo-1;
        int j = hi+1;
        float val;
        while(true)
        {
            do
            {
                i++;
                try {
                    val = data.get(a.get(i).toString());
                }catch (NullPointerException e)
                {
                    val = 0;
                }
            }while(val < p);
            do
            {
                j--;
                try {
                    val = data.get(a.get(j).toString());
                }catch (NullPointerException e)
                {
                    val = 0;
                }
            }while(val > p);
            if( i >= j) return j;
            swap(a,i,j);

        }
    }

    private void swap(ArrayList<Object> a, int i, int j)
    {
        Object tmp = a.get(i);
        a.set(i,a.get(j));
        a.set(j,tmp);
    }

}
