package com.Kaavapp.android;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by janne on 30.10.2016.
 */
public class yksikkoTulos extends Tulos {

    private ArrayList<yksikkoTulos> compoundYksikot; //paska nimi
private  String nimiarvo, suurearvo;

    public yksikkoTulos(HashMap<String,String> vals)
    {
        tiedot = vals;
        layoutSmall = R.layout.yksikko_small;
        layoutLarge = R.layout.yksikko_large;

        type = "yksikot";
        taulu = "yksikot";
        tagiTaulu = "yksikotTag";
        linkkiTaulu = "yksikot_tag";

        defaultCategory = R.string.yksikkoCat;
    }



    public View getSmallView (LayoutInflater infl, ViewGroup paren)
    {
        View pal = super.getSmallView(infl,paren);


        if(checkLanguage()){
            this.nimiarvo = "ennimi";
            this.suurearvo = "ensuure";
        }else{
            this.nimiarvo = "nimi";
            this.suurearvo = "suure";

        }

        ((TextView)pal.findViewById(R.id.txvNimi)).setText(tiedot.get(nimiarvo));
        ((TextView)pal.findViewById(R.id.txvKuvaus)).setText(tiedot.get(suurearvo));

        KaavaFactory kf = new KaavaFactory(pal.getContext(),pal.getResources(),(int)Math.ceil(((TextView)pal.findViewById(R.id.txvNimi)).getTextSize()/ pal.getResources().getDisplayMetrics().density));
        ((ImageView)pal.findViewById(R.id.imgYksikko)).setImageDrawable(kf.getBmD(tiedot.get("yksikko")));
        return pal;
    }

    public String getNimi(){if(checkLanguage())return tiedot.get("ennimi");return tiedot.get("nimi");}

    public View getLargeView (final LayoutInflater infl, final ViewGroup paren)
    {
        View pal = infl.inflate(layoutLarge, paren, false);
        if(!dataHaettu) GAD.findData(this);
        LinearLayout lnlMain = (LinearLayout) pal.findViewById(R.id.lnlMainBody);
        lnlMain.addView(getSmallView(infl,lnlMain));
        LinearLayout lnlComp = (LinearLayout)pal.findViewById(R.id.lnlCompoundUnits);
        for(Tulos t: compoundYksikot)
        {
            View tmp = t.getSmallView(infl,lnlComp);
            ((ViewGroup)tmp.findViewById(R.id.imgSuosikki).getParent()).removeView(pal.findViewById(R.id.imgSuosikki));
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String klikattuNimi = ((TextView)v.findViewById(R.id.txvNimi)).getText().toString();
                    for(int i = 0; i < compoundYksikot.size(); i++)
                    {
                        if(klikattuNimi.compareTo(compoundYksikot.get(i).getValue(nimiarvo)) == 0)
                        {
                            //löytyi klikattu isotooppi
                            paren.removeAllViews(); //Viite tähän olioon mahdollisesti katoaa kun tyhjennetään layoutti...
                            paren.addView(compoundYksikot.get(i).getLargeView(infl,paren));
                            break;
                        }
                    }
                }
            });
            lnlComp.addView(tmp);
        }
        return pal;
    }


    public void addCompound(ArrayList<Tulos> data)
    {
        dataHaettu = true;
        compoundYksikot = new ArrayList<>();
        if(data == null) { return;}
        for(Tulos t: data) {

            compoundYksikot.add((yksikkoTulos)t);
        }
    }
}
