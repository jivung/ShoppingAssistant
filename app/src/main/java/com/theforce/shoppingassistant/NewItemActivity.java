package com.theforce.shoppingassistant;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class NewItemActivity extends AppCompatActivity {

    private ArrayList<Item> items;
    private ItemsAdapter adapter;
    private EditText searchField;

    private BarcodeTranslationDatabase dtb;
    private MediaPlayer mp;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // Toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        searchField = (EditText) findViewById(R.id.search_field);
        final ListView listView = (ListView) findViewById(R.id.items_list);

        // Barcode
        dtb = new BarcodeTranslationDatabase();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);

        // Listan
        items = new ArrayList<>();
        items.add(new Item("Milk", "Dairy"));
        items.add(new Item("Cheese", "Dairy"));
        items.add(new Item("Apples", "Fruit"));
        items.add(new Item("Bananas", "Fruit"));
        items.add(new Item("Chicken", "Meat"));
        items.add(new Item("Egg", "Dairy"));

        // Adapter
        adapter = new ItemsAdapter(this, items);
        listView.setAdapter(adapter);

        // Vald vara
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                addItem(adapter.getFilteredList().get(position));
            }
        });

        // Sök
        searchField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                NewItemActivity.this.adapter.getFilter().filter(cs);
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            public void afterTextChanged(Editable arg0) {}
        });

    }

    private void addItem(Item item){
        Intent intent = new Intent(this, ShoppingListActivity.class);
        intent.putExtra("newItem", item);
        startActivity(intent);
    }

    public void scanNow(View view) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 0);
        Log.d("test", "button works!");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                mp.start();
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                System.out.println(dtb.getNameFromBarcode(contents)); //Skicka dtb.getNameFromBarcode(contents) till Mickes klass
            } else if(resultCode == RESULT_CANCELED){ // Handle cancel
                Log.i("xZing", "Cancelled");
            }
        }
    }

}
