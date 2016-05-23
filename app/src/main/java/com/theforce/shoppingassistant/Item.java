package com.theforce.shoppingassistant;

import java.io.Serializable;

public class Item implements Serializable, Cloneable {

    String name;
    String category;
    String barcode = null;
    boolean isChecked;

    public Item(String name, String category, String barcode){
        this.name = name;
        this.category = category;
        this.barcode = barcode;
        isChecked = false;
    }

    public Item(String name, String category){
        this.name = name;
        this.category = category;
        isChecked = false;
    }

    public Item(Item item){
        this.name = item.getName();
        this.category = item.getCategory();
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getBarcode(){
        return barcode;
    }

    public boolean isChecked(){
        return isChecked;
    }

    public void setChecked(boolean isChecked){
        this.isChecked = isChecked;
    }

    public void print(){
        System.out.println(name + " - " + category);
    }


}
