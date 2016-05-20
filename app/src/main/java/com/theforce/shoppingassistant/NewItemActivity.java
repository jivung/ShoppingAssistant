package com.theforce.shoppingassistant;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class NewItemActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private EditText searchField;

    private BarcodeTranslationDatabase dtb;
    private MediaPlayer mp;
    private Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // Toolbar
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        searchField = (EditText) findViewById(R.id.search_field);
        final ListView listView = (ListView) findViewById(R.id.items_list);

        // Barcode
        dtb = new BarcodeTranslationDatabase();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);

        // Listan
        final ArrayList<String> list = new ArrayList<String>();
        list.add("Apples");
        list.add("Milk");
        list.add("Cheese");

        // Adapter
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        // Vald vara
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                addItem("Yeman");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_scan:
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("com.google.zxing.client.android.SCAN.SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
                Log.d("test", "Inne i switch");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addItem(String item){
        Intent intent = new Intent(this, ShoppingListActivity.class);
        intent.putExtra("newItem", new Item("Test", "Hej"));
        startActivity(intent);
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
