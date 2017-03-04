package br.com.henriquecocito.lunchtime.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Map;
import br.com.henriquecocito.lunchtime.utils.APIClient;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by HenriqueCocito on 04/03/17.
 */

public class MapViewModel {

    private ArrayList<Map> mPlaces = new ArrayList<>();
    private MapDataListener mDataListener;

    public interface MapDataListener {
        void onMapChanged(ArrayList<Map> maps);
        void onMapError(Throwable error);
        void onMapLoaded(ArrayList<Map> maps);
    }

    public void setDataListener(MapDataListener dataListener) {
        this.mDataListener = dataListener;
    }

    public void getNearbyPlaces(Location location) {

        // Get application context
        Context context = LunchTimeApplication.CONTEXT;

        // Get shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        HashMap<String, String> params = new HashMap<>();
        params.put("key", context.getString(R.string.googleApiKey));
        params.put("sensor", "true");
        params.put("radius", sharedPreferences.getString(context.getString(R.string.pref_key_radius), "200"));
        params.put("types", "restaurant");
        params.put("location", String.format("%s,%s", location.getLatitude(), location.getLongitude()));

        APIClient
                .getInstance()
                .getPlaces(params)
                .flatMap(new Func1<LinkedTreeMap<String, Object>, Observable<ArrayList<Map>>>() {

                    @Override
                    public Observable<ArrayList<Map>> call(LinkedTreeMap<String, Object> object) {

                        Gson gson = new Gson();
                        ArrayList<LinkedTreeMap<String, Object>> results = (ArrayList<LinkedTreeMap<String, Object>>) object.get("results");
                        mPlaces.clear();
                        for (int i = 0; i < results.size(); i++) {
                            JsonObject jsonObject = gson.toJsonTree(results.get(i)).getAsJsonObject();
                            mPlaces.add(gson.fromJson(jsonObject, Map.class));
                        }
                        return Observable.just(mPlaces);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Map>>() {
                    @Override
                    public void onCompleted() {
                        mDataListener.onMapLoaded(mPlaces);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDataListener.onMapError(e);
                    }

                    @Override
                    public void onNext(ArrayList<Map> maps) {
                        mDataListener.onMapChanged(maps);
                    }
                });
    }
}
