package com.theforce.shoppingassistant;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShoppingActivity extends AppCompatActivity {

    private ShoppingList shoppingList;
    private ArrayList<Item> items;
    private ItemsAdapter adapter;

    ListView listView;
    static final int check = 1111;
    android.speech.tts.TextToSpeech t1;
    private Sensor mySensor;
    private SensorManager mSensorManager;
    private MediaPlayer mySound2;
    private long lastUpdate;
    Item currentItem;
    TextView currentItemName;
    TextView currentItemCategory;

    private float last_x = 0;
    private float last_y = 0;
    private float last_z = 0;
    private ShakeEventListener mSensorListener;
    private ArrayList<String> stringList;
    private MediaButtonIntentReceiver r;
    private OnShakeListener mShakeListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        currentItemName = (TextView) findViewById(R.id.currentItemName);
        currentItemCategory = (TextView) findViewById(R.id.currentItemCategory);
        listView = (ListView) findViewById(R.id.listView);

        Bundle extra = getIntent().getBundleExtra("extra");
        items = (ArrayList<Item>) extra.getSerializable("items");
        currentItem = items.get(0);
        currentItemName.setText(currentItem.getName());
        currentItemCategory.setText(currentItem.getCategory());
        items.remove(0);

        // Adapter
        adapter = new ItemsAdapter(this, R.layout.list_item_check, items);
        listView.setAdapter(adapter);

        // CALLES SKIT

        //Button b = (Button) findViewById(R.id.bVoice);
        //b.setOnClickListener(this);

        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        MediaButtonIntentReceiver r = new MediaButtonIntentReceiver();
        t1 = new android.speech.tts.TextToSpeech(getApplicationContext(), new android.speech.tts.TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != android.speech.tts.TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        ((AudioManager) getSystemService(AUDIO_SERVICE)).registerMediaButtonEventReceiver(
                new ComponentName(
                        getPackageName(),
                        MediaButtonIntentReceiver.class.getName()));

        registerReceiver(r, filter);
        filter.setPriority(10000);

        //Skapar en Sensor Manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {

                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter your command");
                startActivityForResult(i, check);
            }
        });

        //CheckBox vara1 = (CheckBox) findViewById(R.id.vara1Id);
        //CheckBox vara2 = (CheckBox) findViewById(R.id.vara2Id);
        //CheckBox vara3 = (CheckBox) findViewById(R.id.vara3Id);
        //CheckBox vara4 = (CheckBox) findViewById(R.id.vara4Id);


        ArrayList<String> callesList = new ArrayList<>();
        callesList.add(0, "Milk");
        callesList.add(1, "Potatoes");
        callesList.add(2, "Cottage cheese");
        callesList.add(3, "Cheese");
        CharSequence vara11 = callesList.get(0).toString();
        CharSequence vara12 = callesList.get(1).toString();
        CharSequence vara13 = callesList.get(2).toString();
        CharSequence vara14 = callesList.get(3).toString();
        //vara1.setText(vara11);
        //vara2.setText(vara12);
        //vara3.setText(vara13);
        //vara4.setText(vara14);

    }


    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        void onShake();
    }

    public void onClick(View v) {
        /*switch (v.getId()){

            case R.id.bVoice:
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter your command");
                startActivityForResult(i, check);

                break;


        }*/

    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(android.speech.tts.TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "");
        t1.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId = this.hashCode() + "";
        t1.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method sub
        if (requestCode == check && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //CheckBox vara1 = (CheckBox) findViewById(R.id.vara1Id);
            //CheckBox vara2 = (CheckBox) findViewById(R.id.vara2Id);
            //CheckBox vara3 = (CheckBox) findViewById(R.id.vara3Id);
            //CheckBox vara4 = (CheckBox) findViewById(R.id.vara4Id);



           /* if (results.contains(shoppingList.get(0).toString().toLowerCase() )) {
                vara1.setChecked(true);
                String toSpeak = shoppingList.get(0).toString().toLowerCase() + "checked. Next list_item on the list is" + shoppingList.get(1).toString().toLowerCase();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {
                    ttsUnder20(toSpeak);
                }

            } else if (results.contains(shoppingList.get(1).toString().toLowerCase())){
                vara2.setChecked(true);
                String toSpeak = shoppingList.get(1).toString().toLowerCase() + "checked. Next list_item on the list is" + shoppingList.get(2).toString().toLowerCase();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {
                    ttsUnder20(toSpeak);
                }


            } else if (results.contains("quit")) {

                String inga = "No more items";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(inga);
                } else {
                    ttsUnder20(inga);
                }
            }else if (results.contains(shoppingList.get(2).toString().toLowerCase())){
                vara3.setChecked(true);
                String toSpeak = shoppingList.get(2).toString().toLowerCase() + "checked. Next list_item on the list is" + shoppingList.get(3).toString().toLowerCase();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {

                    ttsUnder20(toSpeak);
                }
            }else if (results.contains(shoppingList.get(3).toString().toLowerCase())){
                vara4.setChecked(true);
                String toSpeak = shoppingList.get(3).toString().toLowerCase() + "checked. Grocerylist completed";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {

                    ttsUnder20(toSpeak);
                }


            } else if (results.contains(shoppingList.get(2).toString().toLowerCase())) {

                String toSpeak = "Latest grocery removed";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {
                    ttsUnder20(toSpeak);
                }
            }
            else if(results.contains("music")){
                mySound2.start();


            }*/


            /*else if(results.contains(shoppingList.get(0).toString().toLowerCase()) && vara1.isChecked()){
                String toSpeak = "Grocery already checked";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {
                    ttsUnder20(toSpeak);
                }

            }*/
            /*else{
                String toSpeak = "I did not quite get that";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(toSpeak);
                } else {
                    ttsUnder20(toSpeak);
                }


            }*/

        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);


    }

    public void itemChecked(View view){



        adapter.itemChecked(listView, currentItem);

        adapter.add(currentItem);

        items = adapter.getFilteredList();
        currentItem = items.get(0);
        currentItemName.setText(currentItem.getName());
        currentItemCategory.setText(currentItem.getCategory());

    }

}
