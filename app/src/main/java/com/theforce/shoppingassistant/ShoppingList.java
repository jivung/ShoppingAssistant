package com.theforce.shoppingassistant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Micke on 2016-04-24.
 */
public class ShoppingList<Item> extends ArrayList<Item> implements Serializable{

    public ShoppingList(){
        super();
    }

    public void print(){
        if(isEmpty()){
            System.out.println("Shoppinglistan är tom.");
        } else {
            System.out.println("TODO SKRIV UT");
        }
    }

}
