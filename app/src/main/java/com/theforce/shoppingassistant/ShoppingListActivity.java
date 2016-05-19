package com.theforce.shoppingassistant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingList shoppingList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        shoppingList = new ShoppingList();

        final ListView listview = (ListView) findViewById(R.id.listView);

        final ArrayList<String> list = new ArrayList<String>();
        list.add("+ Lägg till ny vara..");

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if(position ==0) {
                    newItem();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().hasExtra("shoppingList")){
            shoppingList = (ShoppingList) getIntent().getSerializableExtra("shoppingList");
        }
        shoppingList.print();
    }

    public void newItem(){
        Intent intent = new Intent(this, NewItemActivity.class);
        intent.putExtra("shoppingList", shoppingList);
        startActivity(intent);
    }

    public void startShopping(View view){
        Intent intent = new Intent(this, ShoppingActivity.class);
        startActivity(intent);
    }

}
