package com.theforce.shoppingassistant;


import android.app.Activity;
import android.hardware.SensorEventListener;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

public class Compass extends Activity implements SensorEventListener, ConnectionCallbacks, OnConnectionFailedListener {
    private ImageView image;
    private float currentDegree = 0f;
    private SensorManager sensorManager;
    private float declination;
    GeomagneticField geoField;
    private Location location;
    LocationManager locationManager;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    double myLong = 0;
    double myLat = 0;

    double endLat = 36;
    double endLng = 138;


    // Uppdaterar location

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

        @Override
        public void onProviderDisabled(String provider) {


        }

        @Override
        public void onProviderEnabled(String provider) {


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {


        }
    };

    // Connectar till GPS.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        image = (ImageView) findViewById(R.id.imageViewCompass);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);


    }


    public void onConnectionSuspended(int var1) {

    }

    // Initierar location objekt och geoField objekt när GPS kopplingen skapas.

    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
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

    public void onConnectionFailed(@NonNull ConnectionResult var1) {


    }

    // Räknar ut relativa heading till en punkt på Jorden. Animerar pilen.

    @Override
    public void onSensorChanged(SensorEvent event) {
        float heading = Math.round(event.values[0]);


        // float R[] = new float[9];
        // float I[] = new float[9];
        // SensorManager.getRotationMatrix(R, I,event.values, event.values);
        // float orientation[] = new float[3];
        // SensorManager.getOrientation(I, orientation);
        //  float azimuth = Math.round(event.values[0]);
        // azimuth = (float) Math.toDegrees(orientation[0]);

        //heading += geoField.getDeclination();
        heading = (heading + 360) % 360;
        heading -= bearing(myLat, myLong, endLat, endLng);

        RotateAnimation rAnimation = new RotateAnimation(currentDegree, -heading,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rAnimation.setDuration(200);
        rAnimation.setFillAfter(true);
        image.startAnimation(rAnimation);
        currentDegree = -heading;


    }

    // Räknar ut bearing

    protected double bearing(double myLong, double myLat, double endLat, double endLng) {
        //  myLong = location.getLongitude();
        double longitude2 = endLng;
        //  myLat = Math.toRadians(location.getLatitude());
        double latitude2 = Math.toRadians(endLat);
        double longDiff = Math.toRadians(longitude2 - myLong);
        double y = Math.sin(longDiff) * Math.cos(latitude2);
        double x = Math.cos(myLat) * Math.sin(latitude2) - Math.sin(myLat) * Math.cos(latitude2) * Math.cos(longDiff);

        return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
    }

}
