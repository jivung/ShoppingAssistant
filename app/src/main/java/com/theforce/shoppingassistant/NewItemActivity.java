package com.theforce.shoppingassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NewItemActivity extends AppCompatActivity {

    private ArrayList<Item> items;
    private ItemsAdapter adapter;
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
        mp = MediaPlayer.create(getApplicationContext(), R.raw.check);

        // Listan
        items = new ArrayList<>();
        items.add(new Item("Milk", "Dairy"));
        items.add(new Item("Cheese", "Dairy"));
        items.add(new Item("Apples", "Fruit"));
        items.add(new Item("Bananas", "Fruit"));
        items.add(new Item("Chicken", "Meat"));
        items.add(new Item("Egg", "Dairy"));

        // Adapter
        adapter = new ItemsAdapter(this, R.layout.list_item, items);
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

    private void addItem(Item item){
        Intent intent = new Intent(this, ShoppingListActivity.class);
        intent.putExtra("newItem", item);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                mp.start();
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                System.out.println(dtb.getNameFromBarcode(contents)); //Skicka dtb.getNameFromBarcode(contents) till Mickes klass
                showAlertAdded(dtb.getNameFromBarcode(contents));
            } else if(resultCode == RESULT_CANCELED){ // Handle cancel
                Log.i("xZing", "Cancelled");
            }
        }
    }

    public void showAlertAdded(String productName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_added_layout, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setTitle("Succcesfully added "+ productName);
        dialog.show();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                ImageView image = (ImageView) dialog.findViewById(R.id.addedAlertImage);
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.drawable.iconv2);
                float imageWidthInPX = (float)image.getWidth();

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                        Math.round(imageWidthInPX * (float)icon.getHeight() / (float)icon.getWidth()));
                image.setLayoutParams(layoutParams);


            }
        });

        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 1500); // after 2 second (or 2000 miliseconds), the task will be active.


    }


}
