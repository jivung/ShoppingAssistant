package com.theforce.shoppingassistant;

import java.io.Serializable;

public class Item implements Serializable {

    private String name;
    private String category;

    public Item(String name, String category){
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void print(){
        System.out.println(name + " - " + category);
    }

}
