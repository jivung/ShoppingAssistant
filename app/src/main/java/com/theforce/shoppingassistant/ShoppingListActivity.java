package com.theforce.shoppingassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingList shoppingList;
    private ArrayList<Item> items;
    private ItemsAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // Variablar
        final ListView listView = (ListView) findViewById(R.id.listView);

        // TEST
        shoppingList = new ShoppingList<>();
        items = shoppingList.getList();
        items.add(new Item("Milk", "Dairy"));
        /*items.add(new Item("Cheese", "Dairy"));
        items.add(new Item("Apples", "Fruit"));
        items.add(new Item("Bananas", "Fruit"));
        items.add(new Item("Chicken", "Meat"));
        items.add(new Item("Egg", "Dairy"));
        items.add(new Item("Bananas", "Fruit"));
        items.add(new Item("Chicken", "Meat"));
        items.add(new Item("Egg", "Dairy"));*/

        // Adapter
        adapter = new ItemsAdapter(this, R.layout.list_item, items);
        listView.setAdapter(adapter);

    }

    public void newItem(View view){
        Intent intent = new Intent(this, NewItemActivity.class);
        startActivityForResult(intent, 1);
    }

    public void startShopping(View view){
        Intent intent = new Intent(this, ShoppingActivity.class);
        Bundle extra = new Bundle();
        extra.putSerializable("items", items);
        intent.putExtra("extra", extra);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String itemName = data.getStringExtra("itemName");
                String itemCategory = data.getStringExtra("itemCategory");
                Item item = new Item(itemName, itemCategory);
                items.add(item);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }



}
