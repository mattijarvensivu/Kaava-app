package com.example.maza.kaavojapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janne on 28.6.2016.
 */

//adapteri luokka jonka pitäis pystyä käsittelemään kaikkia tuloksia
public class resultAdapter extends ArrayAdapter<Tulos> {

    List<Tulos> vals;
    Context con;

    public resultAdapter(Context context, int resource, List<Tulos> objects) {
        super(context, resource, objects);
        vals = objects;
        con = context;
    }

    @Override
    //systeemi kutsu tätä kun se latoo tavaraa listaan. Palautettu View on yksittäinen rivi.
    public View getView(int position, View convertView, ViewGroup parent) {
        return vals.get(position).getSmallView((LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE),parent);

    }
}
