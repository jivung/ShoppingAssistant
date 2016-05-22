package com.theforce.shoppingassistant;

import java.util.ArrayList;

/**
 * Created by Lisa on 2016-05-18.
 */
public class BarcodeTranslationDatabase {
    private ArrayList<SearchObject> database;

    public BarcodeTranslationDatabase() {
        database = new ArrayList<SearchObject>();
        addBarcode("Bulgur", "7340083436680");
        addBarcode("Aqua Nobel", "7350069768032");
        addBarcode("Frasvafflor", "7310470030319");
        //Lägg till fler objekt
    }

    public String getNameFromBarcode(String barcode){
        String name = null;
        for( SearchObject object : database){
            name = object.getName(barcode);
        }
        if(name != null){
            return name;
        } else {
            return "product with number " + barcode;
        }
    }

    public void addBarcode(String name, String barcode){
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

