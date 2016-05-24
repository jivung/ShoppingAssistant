package com.theforce.shoppingassistant;

import android.annotation.TargetApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class ShoppingActivity extends AppCompatActivity implements SensorEventListener, ConnectionCallbacks, OnConnectionFailedListener {

    private ArrayList<Item> items;
    private ItemsAdapter adapter;
    ListView listView;

    static final int check = 1111;
    android.speech.tts.TextToSpeech t1;
    private Sensor mySensor;
    private SensorManager mSensorManager;
    private long lastUpdate;
    Item currentItem;
    TextView currentItemName;
    TextView currentItemCategory;

    private float last_x = 0;
    private float last_y = 0;
    private float last_z = 0;
    private ShakeEventListener mSensorListener;

    // Taavets
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    GeomagneticField geoField;
    private Location location;
    LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    double myLong = 0;
    double myLat = 0;

    // Japan
    double endLat = 36;
    double endLng = 138;

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

        // GPS
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        image = (ImageView) findViewById(R.id.arrow);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Media button
        //IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        //MediaButtonIntentReceiver mediaButtonIntentReceiver = new MediaButtonIntentReceiver();
        //((AudioManager) getSystemService(AUDIO_SERVICE)).registerMediaButtonEventReceiver(new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName()));
        //registerReceiver(mediaButtonIntentReceiver, filter);
        //filter.setPriority(10000);

        // TextToSpeech
        t1 = new android.speech.tts.TextToSpeech(getApplicationContext(), new android.speech.tts.TextToSpeech.OnInitListener() {
            public void onInit(int status) {
                if (status != android.speech.tts.TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        final AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        // Shake
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorListener = new ShakeEventListener();
        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {
            public void onShake() {
                if(am.isWiredHeadsetOn()){
                    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Enter your command");
                    startActivityForResult(i, check);
                } else{
                    itemChecked(null);
                }
            }
        });

    }

    public void itemChecked(View view){
        if(items.size() == 0 || items.get(0).isChecked){
            finishedShopping();
        } else {
            currentItem.setChecked(true);
            adapter.add(currentItem);
            currentItem = adapter.getItem(0);
            currentItemName.setText(currentItem.getName());
            currentItemCategory.setText(currentItem.getCategory());
            adapter.remove(adapter.getItem(0));
        }
    }

    private void finishedShopping(){
        final Intent intent = new Intent(this, MainActivity.class);
        new android.app.AlertDialog.Builder(this)
                .setMessage("Shopping done!")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(intent);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public interface OnShakeListener
    {
        public void onShake();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == check && resultCode == RESULT_OK) {
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            StringBuilder sb = new StringBuilder();
            String searchString = currentItem.getName().toLowerCase() + " checked";
            if(results.contains(searchString)){
                sb.append(currentItem.getName().toLowerCase());
                sb.append(" checked.");
                if(!items.isEmpty() && !items.get(0).isChecked){
                    sb.append("Next item: ");
                    sb.append(items.get(0).getName().toLowerCase());
                } else{
                    sb.append("No more items. Shopping done.");
                }
                itemChecked(null);
            } else {
                sb.append("I did not quite get that.");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsGreater21(sb.toString());
            } else {
                ttsUnder20(sb.toString());
            }
        }
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
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
        sensorManager.unregisterListener(this);
    }

    // Uppdaterar location
    @SuppressWarnings("MissingPermission")
    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            geoField = new GeomagneticField(
                    Double.valueOf(location.getLatitude()).floatValue(),
                    Double.valueOf(location.getLongitude()).floatValue(),
                    Double.valueOf(location.getAltitude()).floatValue(),
                    System.currentTimeMillis()
            );
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            myLong = location.getLongitude();
            myLat = location.getLatitude();
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    // Initierar location objekt och geoField objekt när GPS kopplingen skapas. Permissions kan man ignorera.
    @SuppressWarnings("MissingPermission")
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            myLong = mLastLocation.getLatitude();
            myLat = mLastLocation.getLongitude();
        }
        if (geoField == null) {
            geoField = new GeomagneticField(
                    Double.valueOf(mLastLocation.getLatitude()).floatValue(),
                    Double.valueOf(mLastLocation.getLongitude()).floatValue(),
                    Double.valueOf(mLastLocation.getAltitude()).floatValue(),
                    System.currentTimeMillis()
            );
        }
    }

    // Räknar ut relativa heading till en punkt på Jorden. Animerar pilen.
    public void onSensorChanged(SensorEvent event) {
        double[] cords = getCurrentItemLocation();
        endLat = cords[0];
        endLng = cords[1];
        float heading = Math.round(event.values[0]);
        heading += geoField.getDeclination();
        heading = (heading + 360) % 360;
        heading -= bearing(myLat, myLong, endLat, endLng);
        RotateAnimation rAnimation = new RotateAnimation(currentDegree, -heading, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rAnimation.setDuration(200);
        rAnimation.setFillAfter(true);
        image.startAnimation(rAnimation);
        currentDegree = -heading;
    }

    // Räknar ut bearing
    protected double bearing(double myLong, double myLat, double endLat, double endLng) {
        double longitude2 = endLng;
        double latitude2 = Math.toRadians(endLat);
        double longDiff = Math.toRadians(longitude2 - myLong);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(myLat) * Math.sin(latitude2) - Math.sin(myLat) * Math.cos(latitude2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

    private double[] getCurrentItemLocation(){
        double[] cords = new double[2];
        if(currentItem.getCategory().equals("Fruit and Vegetables")){
            // Köpenhamn
            cords[0] = 55.658996;
            cords[1] = 12.483215;
        } else if(currentItem.getCategory().equals("Dairy")){
            // Ängelholm
            cords[0] = 56.231139;
            cords[1] = 12.906189;
        } else if(currentItem.getCategory().equals("Charcuterie")){
            // Kristianstad
            cords[0] = 55.998381;
            cords[1] = 14.153137;
        } else{
            // Ystad
            cords[0] = 55.453941;
            cords[1] = 13.752136;
        }
        return cords;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    public void onConnectionSuspended(int var1) {}
    public void onConnectionFailed(@NonNull ConnectionResult var1) {}

}
