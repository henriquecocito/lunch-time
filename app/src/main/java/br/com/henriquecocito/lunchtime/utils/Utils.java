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
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Place;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hrcocito on 03/04/17.
 */

public class Utils {

    public static final String PREF_KEY = LunchTimeApplication.CONTEXT.getString(R.string.app_name);
    public static final String PREF_SORT_KEY = "sort";
    public static final String PREF_RADIUS_KEY = "radius";

    @BindingAdapter("app:fontFamily")
    public static void setFont(TextView textView, String fontFamily) {
        Typeface custom_font = Typeface.createFromAsset(LunchTimeApplication.CONTEXT.getAssets(), String.format("fonts/%s.ttf", fontFamily));
        textView.setTypeface(custom_font);
    }

    @BindingAdapter({"app:url", "app:placeholder"})
    public static void loadImage(final ImageView view, String url, Drawable error) {
        if (!url.isEmpty()) {
            Picasso.with(view.getContext())
                    .load(url)
                    .error(error)
                    .into(view, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            view.setPadding(10, 10, 10, 10);
                        }
                    });
        } else {
            view.setPadding(10, 10, 10, 10);
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

    public static Observable<List<Place>> getNearByPlaces(final Location location) {

        final Context context = LunchTimeApplication.CONTEXT;

        return Observable.create(new Observable.OnSubscribe<List<Place>>() {

            @Override
            public void call(final Subscriber<? super List<Place>> subscriber) {

                // Get shared preferences
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

                HashMap<String, String> params = new HashMap<>();
                params.put("key", context.getString(R.string.googleApiKey));
                params.put("sensor", "true");
                params.put("radius", sharedPreferences.getString(context.getString(R.string.pref_key_radius), context.getString(R.string.pref_default_radius)));
                params.put("types", "restaurant");
                params.put("location", String.format("%s,%s", location.getLatitude(), location.getLongitude()));

                APIClient
                        .getInstance()
                        .getPlaces(params)
                        .flatMap(new Func1<LinkedTreeMap<String, Object>, Observable<List<Place>>>() {
                            @Override
                            public Observable<List<Place>> call(LinkedTreeMap<String, Object> object) {
                                Gson gson = new Gson();
                                JsonArray jsonArray = gson.toJsonTree(object.get("results")).getAsJsonArray();
                                Type listType = new TypeToken<List<Place>>() {}.getType();
                                return Observable.just((List<Place>) gson.fromJson(jsonArray, listType));
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<List<Place>>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(List<Place> places) {
                                subscriber.onNext(places);
                            }
                        });

            }
        });
    }

    public static void getPhotos(ArrayList<String> references) {
        final Context context = LunchTimeApplication.CONTEXT;

        HashMap<String, String> params = new HashMap<>();
        params.put("key", context.getString(R.string.googleApiKey));
        params.put("photoreference", references.get(0));

//        APIClient
//                .getInstance()
//                .getPlacePhoto(params)
//                .flatMap(new Func1<LinkedTreeMap<String, Object>, Observable<?>>() {
//
//                    @Override
//                    public Observable<?> call(LinkedTreeMap<String, Object> stringObjectLinkedTreeMap) {
//                        return null;
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe();

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
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, getPreference().getInt(PREF_RADIUS_KEY, 10), locationListener);
            }
        });
    }
}
