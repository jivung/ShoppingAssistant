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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShoppingActivity extends AppCompatActivity {

    // CALLES SKIT

    ListView lv;
    static final int check = 1111;
    android.speech.tts.TextToSpeech t1;
    private Sensor mySensor;
    private SensorManager mSensorManager;
    private MediaPlayer mySound2;
    private long lastUpdate;

    private float last_x = 0;
    private float last_y = 0;
    private float last_z = 0;
    ArrayList<String> shoppingList = null;
    private ShakeEventListener mSensorListener;
    private ArrayList<String> stringList;
    private MediaButtonIntentReceiver r;
    private OnShakeListener mShakeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        final ListView listview = (ListView) findViewById(R.id.listView);
        String[] values = new String[] {
                "Ost",
                "Ägg",
                "Bröd",
                "Mjöl"
        };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                if(position ==0) {

                }
            }
        });

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


        shoppingList = new ArrayList<>();
        shoppingList.add(0, "Milk");
        shoppingList.add(1, "Potatoes");
        shoppingList.add(2, "Cottage cheese");
        shoppingList.add(3, "Cheese");
        CharSequence vara11 = shoppingList.get(0).toString();
        CharSequence vara12 = shoppingList.get(1).toString();
        CharSequence vara13 = shoppingList.get(2).toString();
        CharSequence vara14 = shoppingList.get(3).toString();
        //vara1.setText(vara11);
        //vara2.setText(vara12);
        //vara3.setText(vara13);
        //vara4.setText(vara14);

    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

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

}
