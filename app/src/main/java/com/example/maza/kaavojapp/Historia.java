package com.example.maza.kaavojapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by janne on 22.7.2016.
 */
public class Historia {

    //näiden tehtävä on pitää huolta yksittäisten taulujen käyttö kerroista
    private HashMap<String,Float> tauluOsumatShort; //tämän session aikana muodostuvat käyttökerrat
    private HashMap<String,Float> tauluOsumatLong; //Aikaisempien sessioiden käyttökerrat
    private HashMap<String,Float> tauluOsumatCurrent; //Effektiiviset (vanhat + shortKerroin*uudet) käyttökerrat

    //optioni arvoja
    private String[] listOfTables;
    private float partialValue = 0.1f;
    private float tauluHistoryScore = 50;
    private float riviHistoriaScore = 50;


    //Pidetään kirjaa usein käytettyjen taulujen rivien käytöstä
    private HashMap<String,HashMap<String,Float>> riviOsumatShort;
    private HashMap<String,HashMap<String,Float>> riviOsumatLong;
    private HashMap<String,HashMap<String,Float>> riviOsumatCurrent;


    public Historia()
    {
        listOfTables = new String[]{"Alkuaineet","Funktionaalinenryhma","Hapot","Isotoopit","Kaava","Muuttuja","Vakio"}; //tässäkin järjestys tärkeä... tulisi pyrkiä pois siitä.
        tauluOsumatShort = new HashMap<>();
        tauluOsumatLong = new HashMap<>();
        tauluOsumatCurrent = new HashMap<>();
        float[] testiArvot = new float[]{2,5,10,1,0,0,0}; //VAIN TESTAAMISEEN!!!
        riviOsumatShort = new HashMap<>();
        riviOsumatLong = new HashMap<>();
        riviOsumatCurrent = new HashMap<>();
        for(int i = 0; i < listOfTables.length; i++)
        {
            riviOsumatShort.put(listOfTables[i],new HashMap<String, Float>());
            riviOsumatLong.put(listOfTables[i],new HashMap<String, Float>());
            riviOsumatCurrent.put(listOfTables[i],new HashMap<String, Float>());
            tauluOsumatLong.put(listOfTables[i],testiArvot[i]);
            tauluOsumatShort.put(listOfTables[i],0f);
        }
        riviOsumatLong.get("Hapot").put("htesti",2f);
        riviOsumatLong.get("Hapot").put("h2testi",4f);
        //alustetaan current kentät. Kopioidaan niihin arvot long rakenteista.
        float taulutTotal = 0;
        float riviTotal = 0;
        for(String s:listOfTables)
        {
            taulutTotal += tauluOsumatLong.get(s);

            Set<String> avaimet = riviOsumatLong.get(s).keySet();
            for(String ss:avaimet)
            {
                riviTotal += riviOsumatLong.get(s).get(ss); // ei ole mahdollista että arvo olisi null koska avain on olemassa.
            }
        }
        for(String s: listOfTables)
        {
            tauluOsumatCurrent.put(s,tauluOsumatLong.get(s)*tauluHistoryScore/taulutTotal);
            riviOsumatCurrent.put(s,new HashMap<String, Float>());
            Set<String> avaimet = riviOsumatLong.get(s).keySet();
            for(String ss:avaimet)
            {
                riviOsumatCurrent.get(s).put(ss,riviOsumatLong.get(s).get(ss)*riviHistoriaScore/riviTotal);
            }
        }
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
        tauluOsumatShort.put(t.getType(), tauluOsumatShort.get(t.getType()) + val);
        tauluOsumatCurrent.put(t.getType(), tauluOsumatCurrent.get(t.getType()) + val);
        //tarkistetaan onko tulos korkeassa kategoriassa... tulee laskea avg ja sd
        double avg = calculateAvg();
        double sd = calculateSD();
        if(tauluOsumatCurrent.get(t.getType()) - avg > sd)
        {
            //kyseinen taulu on korkean käyttö asteen taulu lisätään osuma tuloksen nimeen
            Float vanhaCurrentVal = riviOsumatCurrent.get(t.getType()).get(t.getKeyField());
            Float vanhaShortVal = riviOsumatShort.get(t.getType()).get(t.getKeyField());
            if(vanhaCurrentVal == null) vanhaCurrentVal = 0f;
            if(vanhaShortVal == null) vanhaShortVal = 0f;
            riviOsumatCurrent.get(t.getType()).put(t.getKeyField(),vanhaCurrentVal + val);
            riviOsumatShort.get(t.getType()).put(t.getKeyField(),vanhaShortVal + val);

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
            variance += Math.pow(((tauluOsumatCurrent.get(s))-avg),2);
        }
        variance = variance / listOfTables.length;
        //lasketaan standardi poikkeama ottamalla varianssista neliöjuuri
        return Math.sqrt(variance);
    }

    public double calculateAvg()
    {
        //lasketaan tauluOsumatShort keskiarvo
        float avg = 0;
        for(String s : listOfTables)
        {
            avg += (tauluOsumatCurrent.get(s));
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
            if((tauluOsumatCurrent.get(s))-avg >= sd)
            {
                greater.add(s);
            }else if(avg - (tauluOsumatCurrent.get(s)) >= sd) //onko alkio enemmän kuin 1 sd pienempi kuin avg = onko avg 1 sd isompi kuin alkio
            {
                less.add(s);
            }else{
                eq.add(s);
            }
        }
        //Järjestetään eniten käytetyt taulut niiden käyttö kertojen mukaan
        qsort(greater, tauluOsumatCurrent,0,greater.size()-1);
        //haetaan tuloksesta korkean käyttöasteen taulujen tulokset, ja järjestetään ne jokaisen taulun sisällä
        ArrayList<Object> greaterTulokset = new ArrayList<>();
        ArrayList<Tulos> pal = new ArrayList<>();
        for(Object o: greater) {
            for (Tulos t : initial) {
                if (t.getType().compareTo(o.toString()) == 0) {
                    greaterTulokset.add(t);
                }
            }
            qsort(greaterTulokset, riviOsumatCurrent.get(o.toString()), 0, greaterTulokset.size() - 1);
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
        qsort(less, tauluOsumatCurrent,0,less.size()-1);
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
    private void qsort(ArrayList<Object> a,HashMap<String,Float> data, int lo, int hi)
    {
        if(lo < hi)
        {
            int p = partition(a,data,lo, hi);
            qsort(a,data,lo, p);
            qsort(a,data,p+1, hi);
        }
    }

    public void testi()
    {
        Log.d("minun","htesti c " + riviOsumatCurrent.get("Hapot").get("htesti"));
        Log.d("minun","h2testi c " + riviOsumatCurrent.get("Hapot").get("h2testi"));
        Log.d("minun","htesti s " + riviOsumatShort.get("Hapot").get("htesti"));
        Log.d("minun","h2testi s " + riviOsumatShort.get("Hapot").get("h2testi"));
        Log.d("minun","htesti l " + riviOsumatLong.get("Hapot").get("htesti"));
        Log.d("minun","h2testi l " + riviOsumatLong.get("Hapot").get("h2testi"));
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

    //Viedään tämän session käyttö historia pitkän ajan historiaan
    public void writeHistory()
    {
        Log.d("minun","kirjoitetaan historiaan");
        for(String s: listOfTables)
        {
            tauluOsumatLong.put(s,tauluOsumatLong.get(s)+tauluOsumatShort.get(s));

            Set<String> avaimet = riviOsumatShort.get(s).keySet();
            for(String ss:avaimet)
            {
                //kestääkö sen että long taulussa arvo on null? testataan. Ei kestä
                Float longVal = riviOsumatLong.get(s).get(ss);
                if(longVal == null) longVal = 0f;
                riviOsumatLong.get(s).put(ss,longVal + riviOsumatShort.get(s).get(ss));
            }
        }

        //TÄHÄN TULISI SITTEN LAITTAA KEINO JOLLA PÄIVITETTY HISTORIA TALLENNETTAISIIN JOHONKIN!!!
    }

}
