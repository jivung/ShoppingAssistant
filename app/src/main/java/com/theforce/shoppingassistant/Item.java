package com.theforce.shoppingassistant;

/**
 * Created by Micke on 2016-04-24.
 */
public class Item {

    private String name;
    private int ID;

    public Item(String name, int ID){
        this.name = name;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

}
