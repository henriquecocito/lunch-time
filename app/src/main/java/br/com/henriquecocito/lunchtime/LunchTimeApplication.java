package br.com.henriquecocito.lunchtime;

import android.*;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.params.Face;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.henriquecocito.lunchtime.view.activities.MainActivity;
import io.fabric.sdk.android.Fabric;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class LunchTimeApplication extends Application{

    public static Context CONTEXT;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static Location mLocation = new Location(LocationManager.GPS_PROVIDER);

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Fabric.with(this, new Crashlytics());

        CONTEXT = getApplicationContext();
    }

    public static void hideBars(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public static void loadLocation(Activity activity) {

        if (ActivityCompat.checkSelfPermission(CONTEXT, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(CONTEXT, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MainActivity.REQUEST_PERMISSIONS
            );
        }

        LocationManager mLocationManager = (LocationManager) CONTEXT.getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = mLocationManager.getBestProvider(criteria, false);
        mLocation = mLocationManager.getLastKnownLocation(bestProvider);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
            }
        });
    }

    public static Location getLocation() {
        return mLocation;
    }
}
