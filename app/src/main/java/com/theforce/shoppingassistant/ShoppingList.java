package com.theforce.shoppingassistant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Micke on 2016-04-24.
 */
public class ShoppingList implements Serializable{

    private int ID;
    private String name;
    private ArrayList<String> list;

    public ShoppingList() {
        list = new ArrayList<String>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addEntry(String entry) {
        list.add(entry);
    }

    public String getEntry(int index){
        return list.get(index);
    }

    public void removeEntry(int index){
        list.remove(index);
    }

    public void print(){
        if(list.isEmpty()){
            System.out.println("Shoppinglistan är tom.");
        } else {
            for (String item : list) {
                System.out.println(item);
            }
        }
    }

}
