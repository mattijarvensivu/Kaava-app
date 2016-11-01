package com.Kaavapp.android;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by maza on 10/30/2016.
 */
public class HeaderClass {

boolean headerDerivointi, headerVektori, headerGeometria, headerTilasto,headerlogaritmi,
        headerintegrointi, headerAlgebra, headermaaritelty,headertrigono, headermeka, headerenergia  = false;



    public ArrayList<Tulos> SetHeader(HashMap<String, String> tmp, ArrayList<Tulos> pal){

        if(tmp.get("mainTag")!=null) {


            if (tmp.get("mainTag").compareTo("Derivointi") == 0) {
                if(headerDerivointi ==false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Derivointi");
                    pal.add(new headerTulos(temppi));
                   headerDerivointi=true;
                }

            } else if (tmp.get("mainTag").compareTo("Integrointi") == 0) {
                if(headerintegrointi == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Integrointi");
                    pal.add(new headerTulos(temppi));
                    headerintegrointi = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Vektori") == 0) {
                if(headerVektori == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Vektori");
                    pal.add(new headerTulos(temppi));
                    headerVektori = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Mekaniikka") == 0) {
                if(headermeka == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Mekaniikka");
                    pal.add(new headerTulos(temppi));
                    headermeka = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Geometria") == 0) {
                if(headerGeometria == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Geometria");
                    pal.add(new headerTulos(temppi));
                    headerGeometria = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Algebra") == 0) {
                if(headerAlgebra == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Algebra");
                    pal.add(new headerTulos(temppi));
                    headerAlgebra = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Tilastotiede") == 0) {
                if(headerTilasto == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Tilastotiede");
                    pal.add(new headerTulos(temppi));
                    headerTilasto = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Määritelty integraali") == 0) {
                if(headermaaritelty == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Määritelty Integraali");
                    pal.add(new headerTulos(temppi));
                    headermaaritelty = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Logaritmi") == 0) {
                if(headerlogaritmi == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Logaritmi");
                    pal.add(new headerTulos(temppi));
                    headerlogaritmi = true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Trigonometria") == 0) {
                if(headertrigono == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Trigonometria");
                    pal.add(new headerTulos(temppi));
                    headertrigono= true;
                    Log.d("joku", "Paavo on gay");
                }
            }
            else if (tmp.get("mainTag").compareTo("Energia") == 0) {
                if(headerenergia == false) {
                    HashMap<String, String> temppi = new HashMap<>();
                    temppi.put("nimi", "Energia");
                    pal.add(new headerTulos(temppi));
                    headerenergia = true;
                    Log.d("joku", "Paavo on gay");
                }
            }

        }
return pal;
    }
}
