package com.theforce.shoppingassistant;

import android.app.Activity;
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
        mp = MediaPlayer.create(getApplicationContext(), R.raw.check);

        // Listan
        items = new ArrayList<>();
        items.add(new Item("Apple", "Fruit and Vegetables"));
        items.add(new Item("Banana", "Fruit and Vegetables"));
        items.add(new Item("Salad", "Fruit and Vegetables"));
        items.add(new Item("Tomato", "Fruit and Vegetables"));
        items.add(new Item("Cucumber", "Fruit and Vegetables"));
        items.add(new Item("Mango", "Fruit and Vegetables"));
        items.add(new Item("Pineapple", "Fruit and Vegetables"));
        items.add(new Item("Carrot", "Fruit and Vegetables"));
        items.add(new Item("Onion", "Fruit and Vegetables"));
        items.add(new Item("Garlic", "Fruit and Vegetables"));
        items.add(new Item("Potatos", "Fruit and Vegetables"));
        items.add(new Item("Avocado", "Fruit and Vegetables"));
        items.add(new Item("Pear", "Fruit and Vegetables"));
        items.add(new Item("Apricot", "Fruit and Vegetables"));
        items.add(new Item("Asparagus", "Fruit and Vegetables"));
        items.add(new Item("Aubergine", "Fruit and Vegetables"));
        items.add(new Item("Milk", "Dairy"));
        items.add(new Item("Egg", "Dairy"));
        items.add(new Item("Butter", "Dairy"));
        items.add(new Item("Cheese", "Dairy"));
        items.add(new Item("Yogurt", "Dairy"));
        items.add(new Item("Cream cheese", "Dairy"));
        items.add(new Item("Kvarg", "Dairy"));
        items.add(new Item("Cream", "Dairy"));
        items.add(new Item("Cottage Cheese", "Dairy"));
        items.add(new Item("Bagel", "Bread"));
        items.add(new Item("Tortilla", "Bread"));
        items.add(new Item("Muffin", "Bakery"));
        items.add(new Item("Beef", "Charcuterie"));
        items.add(new Item("Ground Beef", "Charcuterie"));
        items.add(new Item("Lard", "Charcuterie"));
        items.add(new Item("Bacon", "Charcuterie"));
        items.add(new Item("Pork", "Charcuterie"));
        items.add(new Item("Ham", "Charcuterie"));
        items.add(new Item("Smoked Ham", "Charcuterie"));
        items.add(new Item("Bratwurst", "Charcuterie"));
        items.add(new Item("Chorizo", "Charcuterie"));
        items.add(new Item("Hot dog", "Charcuterie"));
        items.add(new Item("Blood sausage", "Charcuterie"));
        items.add(new Item("Salami", "Charcuterie"));
        items.add(new Item("Meat ball", "Charcuterie"));
        items.add(new Item("Rohwurst", "Charcuterie"));
        items.add(new Item("Pasta", "Dry"));
        items.add(new Item("Sugar", "Dry"));
        items.add(new Item("Cereal", "Dry"));
        items.add(new Item("Rice", "Dry"));
        items.add(new Item("Coffee", "Dry", "7340083409721"));
        items.add(new Item("Dressing", "Sauces", "7311440052157"));

        // Adapter
        adapter = new ItemsAdapter(this, R.layout.list_item, items);
        listView.setAdapter(adapter);

        // Vald vara
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                goBack(adapter.getFilteredList().get(position));
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goBack(Item item){
        item.print();
        Intent returnIntent = new Intent();
        returnIntent.putExtra("itemName", item.getName());
        returnIntent.putExtra("itemCategory", item.getCategory());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == 0){
            if(resultCode == RESULT_OK){
                mp.start();
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                showAlertAdded(searchItem(contents));
            } else if(resultCode == RESULT_CANCELED){ // Handle cancel
                Log.i("xZing", "Cancelled");
            }
        }
    }

    public void showAlertAdded(Item item){

        if(item == null){
            finish();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.alert_added_layout, null);
        dialog.setView(dialogLayout);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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

        final Item backItem = item;
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                goBack(backItem);
            }
        }, 3500); // after 2 second (or 2000 miliseconds), the task will be active.

    }

    private Item searchItem(String barcode){
        System.out.println(barcode);
        for(Item item : items){
            if(item.getBarcode() != null) {
                if (item.getBarcode().equals(barcode)) {
                     return item;
                }
            }
        }
        return null;
    }

}
