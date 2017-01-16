package com.Kaavapp.android;

import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by janne on 27.6.2016.
 * Olio joka pitää siällään yhden rivin tiedot. Tarkoitus on että tästä luokasta peritään jokaiselle taululle oma Tulos olio variantti
 * Tärkein (ainut) tehtävä on antaa UI:lle sen haluamat view komponentit jossa esitetään olion, eli rivin, sisältämä tieto
 */
public class Tulos implements Comparable<Tulos> {

    protected  boolean isHeader = false;
    protected int layoutSmall;
    protected int layoutLarge;
    protected HashMap<String,String> tiedot;
    protected String type = "raw";
    protected boolean isSuosikki = false;
    public String sortKey = "RAW";

    protected String taulu;
    protected String tagiTaulu;
    protected String linkkiTaulu;

    protected suosikkiToggleListener suosikkiToggle;
    protected gatehrAddDataListener GAD;

    protected ArrayList<HashMap<String,String>> tagit;

    protected boolean dataHaettu = false;
    protected  boolean isEnglish;

    protected int defaultCategory;


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

            case 5:


                if(values.get("_muuttujaid") != null)
                    return new muuttujaTulos(values);//muuttuja
                if(values.get("_aste") != null) return new Tulos(values); //trigonometrisetArvot tulos
                return new ioniTulos(values);

            case 6:
                if(values.get("_yksikkoid") != null)
                    return new yksikkoTulos(values); //yksikkö
                if(values.get("yksikko") != null)
                {
                    if(values.get("yksikko").compareTo("trigKey") == 0 )
                        return new triFunMasterTulos(values);
                    if(values.get("yksikko").compareTo("redox") == 0 )
                        return new redoxTulos(values);
                    if(arvoTulos.isArvo(values) )
                        return new arvoTulos(values);
                    return new vakioTulos(values);//vakio
                }
                if(values.get("nimifragmentti") != null)
                    return new funktionaalinenTulos(values); //funktionaalinen ryhmä
                if(values.get("_kaavaid") != null)
                    return new kaavaTulos(values); //kaava
                return new piikkiTulos(values);
            case 13:
                return new happoTulos(values);
            case 11:
                return new aineTulos(values);
            default:

                return new Tulos(values); //mahdollisesti vaarallista, mutta toisaalta mahdollistetaan puhtaan tiedon siirtely.
        }

    }

    public void setOnSuosikkiToggleListener(suosikkiToggleListener s)
    {
        suosikkiToggle = s;
    }
    public void setGADListener(gatehrAddDataListener s)
    {
        GAD = s;
    }
    public String getNimi(){return null;}

    public String getType()
    {
        return type;
    }
    public String getSortValue(){return tiedot.get(sortKey);}

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
    public void setTagit(ArrayList<HashMap<String,String>> tagit)
    {

        this.tagit = tagit;
        if(tagit == null) tagit = new ArrayList<>();
        for(HashMap<String,String> h : tagit)
        {
            if(h.get("nimi").compareTo("suosikki") == 0) //tässä kaattuu mikäli HM ei sisällä avainta nimi. Ei pitäisi koskaan tulla tähän sellaista mappia
            {
                isSuosikki = true;
                break;
            }
        }
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
        if(suosikkiToggle == null) return;
        isSuosikki = !isSuosikki;
        setTahti(v);
        suosikkiToggle.onToggleSuosikki(this); //kerrotaan listenerille että suosikki status on muuttunut
    }

    public String getTaulu(){ return taulu;}

public boolean checkifheader(){
    return isHeader;
}

    public boolean checkLanguage(){

    String UsedLanguage = activityMaster.language;
        if(UsedLanguage.compareTo("en")==0){
            this.isEnglish = true;
        }
        else{
            this.isEnglish = false;
        }


        return this.isEnglish;
    }


    @Override
    //Aika rujon näkönen. Toivottavasti ei aiheuta ongelmia
    public int compareTo(Tulos another) {

        //pyritään muuttamaan saadut arvot floateiksi
        String tvs = getSortValue();
        String avs = another.getSortValue();
        if(tvs == null || avs == null) throw new ClassCastException();
        try{
            return Float.compare(Float.parseFloat(tvs),Float.parseFloat(avs));
        }catch (NumberFormatException e)
        {
            //ei ollu floatteja. Vertaillaan stringeinä
            return tvs.compareToIgnoreCase(avs);
        }
    }
    public String getCategory(String[] reqTags)
    {
        int highest = -1;
        if(tagit == null || tagit.size() == 0 || tagit.get(0).get("prior") == null) return defaultCategory+"";
        //etsitään ensimmäinen validi tagi
        int i = 0;
        while(i < tagit.size() && highest == -1)
        {
            //tarkistetaan onko tämä tägi vaadittujen listalla -> ei toimi väliotsikkona
            boolean valid = true;
            for(String s: reqTags)
            {

                if(s.compareTo(tagit.get(i).get("nimi")) == 0 || s.compareTo(tagit.get(i).get("ennimi")) == 0) valid = false;
            }
            if(valid && Integer.parseInt(tagit.get(i).get("prior")) > 0) highest = i;
            i++;
        }
        for(; i < tagit.size(); i++)
        {
            if(Integer.parseInt(tagit.get(i).get("prior")) >= Integer.parseInt(tagit.get(highest).get("prior")))
            {
                //tarkistetaan onko tämä tägi vaadittujen listalla -> ei toimi väliotsikkona
                boolean valid = true;
                for(String s: reqTags)
                {

                    if(s.compareTo(tagit.get(i).get("nimi")) == 0 || s.compareTo(tagit.get(i).get("ennimi")) == 0) valid = false;
                }
                if(valid) highest = i;
            }
        }
        if(highest == -1 && reqTags.length > 0) return getCategory(new String[]{}); //mikäli yksikään tägi ei ollut sallittu ja oli olemassa tägiRajtteita, poistetaan rajoitteet
        if(highest == -1) return defaultCategory+""; //ei ollut rajotteita, mutta ei myöskään kelvollisiä tägejä
        checkLanguage();
        if(isEnglish)
        {
            return tagit.get(highest).get("ennimi");
        }else {
            return tagit.get(highest).get("nimi");
        }

    }

    //ei ehkä ihan paras toteutus
    public int getCategoryValue(String category)
    {
        if(tagit == null) return -1;
        for(HashMap<String, String> h: tagit)
        {
            if(h.get("nimi").compareTo(category) == 0 || h.get("ennimi").compareTo(category) == 0) return Integer.parseInt(h.get("prior"));
        }
        return -1;
    }

    public ArrayList<String> getTagit()
    {
        String nimia = "nimi";
        if(checkLanguage()) nimia = "ennimi";
        ArrayList<String> pal = new ArrayList<>();
        for(HashMap<String,String> h: tagit)
        {
            pal.add(h.get(nimia));
        }
        return pal;
    }
}

interface suosikkiToggleListener
{
    void onToggleSuosikki(Tulos kohde);
}
