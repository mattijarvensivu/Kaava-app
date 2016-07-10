package com.example.maza.kaavojapp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import maximsblog.blogspot.com.jlatexmath.core.AjLatexMath;
import maximsblog.blogspot.com.jlatexmath.core.TeXConstants;
import maximsblog.blogspot.com.jlatexmath.core.TeXFormula;
import maximsblog.blogspot.com.jlatexmath.core.TeXIcon;

/**
 * Created by janne on 10.7.2016.
 */
public class KaavaFactory {

    //default tekstin koko
    static int textSize = 25;
    Resources res;

    //alustetaan vakio asetuksilla
    public KaavaFactory(Context con, Resources reso)
    {
        AjLatexMath.init(con);
        res = reso;
    }

    //alustetaan määritellyllä teksti koolla
    public KaavaFactory(Context con, Resources reso ,int size )
    {
        this(con, reso);
        textSize = size;
    }

    //luodaan kuva default arvoilla
    public BitmapDrawable getBmD(String value)
    {
        return getBmD(value,textSize);
    }

    //luodaan kuva määritellyllä tekstikoolla
    public BitmapDrawable getBmD(String value, int tSize)
    {
        TeXFormula formula = new TeXFormula(value);

        TeXIcon icon = formula.new TeXIconBuilder()
                .setStyle(TeXConstants.STYLE_DISPLAY)
                .setSize(tSize).build();
        Bitmap image = Bitmap.createBitmap(icon.getIconWidth(), icon.getIconHeight(), Bitmap.Config.ARGB_8888);
        Canvas g2 = new Canvas(image);
        g2.drawColor(Color.TRANSPARENT);
        icon.paintIcon(g2, 0, 0);

        return new BitmapDrawable(res , image);
    }


}
