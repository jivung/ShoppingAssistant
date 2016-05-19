package com.theforce.shoppingassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingList shoppingList;
    private ItemsAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // Variablar
        final ListView listView = (ListView) findViewById(R.id.listView);

        // TEST
        shoppingList = new ShoppingList<>();
        shoppingList.add(new Item("Cheese", "Diery"));
        shoppingList.add(new Item("Milk", "Diery"));
        shoppingList.add(new Item("Egg", "Diery"));
        shoppingList.add(new Item("Cucumber", "Vegetables"));

        // Adapter
        adapter = new ItemsAdapter(this, shoppingList);
        listView.setAdapter(adapter);

        // Lägg till ny vara
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if(position ==0) {
                    newItem();
                }
            }
        });

    }

    protected void onStart() {
        super.onStart();
        if(getIntent().hasExtra("newItem")){
            Item newItem = (Item) getIntent().getSerializableExtra("newItem");
            shoppingList.add(newItem);
        }
    }

    public void newItem(){
        Intent intent = new Intent(this, NewItemActivity.class);
        startActivity(intent);
    }

    public void startShopping(View view){
        Intent intent = new Intent(this, ShoppingActivity.class);
        startActivity(intent);
    }

}
