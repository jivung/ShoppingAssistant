package com.theforce.shoppingassistant;

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
        items.add(new Item("+ Add new item..", ""));
        items.add(new Item("Milk", "Dairy"));
        items.add(new Item("Cheese", "Dairy"));
        items.add(new Item("Apples", "Fruit"));
        //items.add(new Item("Bananas", "Fruit"));
        //items.add(new Item("Chicken", "Meat"));
        //items.add(new Item("Egg", "Dairy"));

        // Adapter
        adapter = new ItemsAdapter(this, R.layout.list_item, items);
        listView.setAdapter(adapter);

        // Lägg till ny vara
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if(position ==0) {
                    newItem();
                }
            }
        });

    }

    protected void onResume() {
        super.onResume();
        if(getIntent().hasExtra("newItem")){
            Item newItem = (Item) getIntent().getSerializableExtra("newItem");
            items.add(newItem);
            adapter.notifyDataSetChanged();
        }
        for(Item item : items){
            item.print();
        }
    }

    public void newItem(){
        Intent intent = new Intent(this, NewItemActivity.class);
        startActivity(intent);
    }

    public void startShopping(View view){
        Intent intent = new Intent(this, ShoppingActivity.class);
        Bundle extra = new Bundle();
        items.remove(0);
        extra.putSerializable("items", items);
        intent.putExtra("extra", extra);
        startActivity(intent);
    }

}
