package com.theforce.shoppingassistant;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Micke on 2016-04-24.
 */
public class ShoppingList<Item> implements Serializable{

    private  ArrayList<Item> list;

    public ShoppingList(){
        super();
        list = new ArrayList<>();
    }

    public void print(){
        if(list.isEmpty()){
            System.out.println("Shoppinglistan är tom.");
        } else {
            System.out.println("TODO SKRIV UT");
        }
    }

    public void add(Item item){
        list.add(item);
    }

    public ArrayList<Item> getList(){
        return list;
    }

}
