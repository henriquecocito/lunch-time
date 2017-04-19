package br.com.henriquecocito.lunchtime.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by hrcocito on 03/04/17.
 */

public class Utils {

    public static final String PREF_KEY = LunchTimeApplication.CONTEXT.getString(R.string.app_name);

    @BindingAdapter("app:fontFamily")
    public static void setFont(TextView textView, String fontFamily) {
        Typeface custom_font = Typeface.createFromAsset(LunchTimeApplication.CONTEXT.getAssets(), String.format("fonts/%s.ttf", fontFamily));
        textView.setTypeface(custom_font);
    }

    @BindingAdapter({"app:url", "app:placeholder"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        if (!url.isEmpty()) {
            Picasso.with(view.getContext())
                    .load(url)
                    .error(error)
                    .into(view);
        } else {
            view.setImageDrawable(error);
        }
    }

    public static SharedPreferences getPreference() {
        return LunchTimeApplication.CONTEXT.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }

    public static void setPreference(String key, Object value) {
        SharedPreferences preferences = LunchTimeApplication.CONTEXT.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if(value.getClass().equals(Integer.class)) {
            editor.putInt(key, (Integer) value);
        } else if(value.getClass().equals(String.class)) {
            editor.putString(key, (String) value);
        } else if(value.getClass().equals(Float.class)) {
            editor.putFloat(key, (Float) value);
        }

        editor.commit();
    }

    public static Observable<Location> getLocation(final Activity activity) {

        return Observable.create(new Observable.OnSubscribe<Location>() {

            @Override
            public void call(final Subscriber<? super Location> subscriber) {
                final LocationManager mLocationManager = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    subscriber.onError(new Throwable(activity.getString(R.string.error_permission_location)));
                    return;
                }

                Location bestLocation = null;

                for (String provider : mLocationManager.getProviders(true)) {
                    Location location = mLocationManager.getLastKnownLocation(provider);
                    if (location == null) {
                        continue;
                    }
                    if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                        bestLocation = location;
                    }
                }

                subscriber.onNext(bestLocation);

                LocationListener locationListener = new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        subscriber.onNext(location);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                        int j = 0;
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                        subscriber.onError(null);
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        String error;
                        switch (s) {
                            case LocationManager.GPS_PROVIDER:
                                error = activity.getString(R.string.error_gps_disabled);
                                break;
                            case LocationManager.NETWORK_PROVIDER:
                                error = activity.getString(R.string.error_network_disabled);
                                break;
                            default:
                                error = activity.getString(R.string.error_permission_location);
                                break;
                        }
                        subscriber.onError(new Throwable(error));
                    }
                };

                mLocationManager.removeUpdates(locationListener);
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, locationListener);
            }
        });
    }
}
