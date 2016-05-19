package com.theforce.shoppingassistant;

import java.util.ArrayList;

/**
 * Created by Lisa on 2016-05-18.
 */
public class BarcodeTranslationDatabase {
    private ArrayList<SearchObject> database;

    public BarcodeTranslationDatabase() {
        database = new ArrayList<SearchObject>();
        database.add(0, new SearchObject("Bulgur", "7340083436680"));
        //Lägg till fler objekt
    }

    public String getNameFromBarcode(String barcode){
        String name = null;
        for( SearchObject object : database){
            name = object.getName(barcode);
        }
        if(name != null){
            return "Namnet på produkten är: " + name;
        } else {
            return "Produkt med nummer "+ barcode + " - hittades ej i databasen";
        }
    }

    public void addBarcode(String barcode, String name){
        database.add(database.size(), new SearchObject(name, barcode));
    }
}

class SearchObject{
    private String name;
    private String number;

    public SearchObject(String name, String number){
        this.name = name;
        this.number = number;
    }

    public String getName(String number){
        if(number.equals(this.number)){
            return name;
        }
        return null;
    }
}

