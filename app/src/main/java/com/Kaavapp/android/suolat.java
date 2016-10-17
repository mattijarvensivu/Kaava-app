package com.Kaavapp.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by janne on 14.10.2016.
 */
public class suolat {

    private ArrayList<Tulos> suolotLista = new ArrayList<>();
    //private int lista;
    private int suolo; //layoutti jolla esitetään suola
    private AdapterView.OnItemClickListener listen;
    private int[] sanat;
    private ListView lista;

    public suolat(ArrayList<Tulos> annetut, AdapterView.OnItemClickListener l, Context con)
    {
        suolotLista = annetut;
        listen = l;
        //lista = R.layout.suolat_lista;
        lista = new ListView(con);
        sanat = new int[]{R.string.liukoinen, R.string.osittain, R.string.niukka, R.string.reagoi}; //tämä siksi jotta voidaan loopata koko homma
    }

    public View getList()
    {
        return lista;
    }

    public void setIonit()
    {
        resultAdapter a = new resultAdapter(lista.getContext(), -1, suolotLista); //toimiiko tuo kontekstin haku? tuleeko oikea konteksti?
        lista.setAdapter(a);
        lista.setOnItemClickListener(listen);
    }

    public ioniTulos getAtPosition(int n)
    {
        return (ioniTulos)suolotLista.get(n);
    }


    //rakentaa suolaTulokset ioni tulosten perusteella.
    public void setSuolat(ArrayList<ioniTulos>[] liukoisuudet, ioniTulos mainIon)
    {
        ArrayList<Tulos> suolat = new ArrayList<>();
        for(int i = 0; i < sanat.length; i++) {
            for (ioniTulos it : liukoisuudet[i]) {
                suolat.add(new suolaTulos(it,mainIon, sanat[i]));
            }
        }
       lista.setAdapter(new resultAdapter(lista.getContext(), -1, suolat));
        lista.setOnItemClickListener(null); //aiheuttaako null pointterin vai kestääkö? Tietenkin voidaan tehdä aina dummy listeneri
        //tarvitseeko käsitellä listeneriä? Varmaankin, muuten onKlikkiin jää ionien logiikka
    }



}
