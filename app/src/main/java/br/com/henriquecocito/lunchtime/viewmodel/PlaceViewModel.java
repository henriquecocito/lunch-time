package br.com.henriquecocito.lunchtime.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Map;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.model.Vote;
import br.com.henriquecocito.lunchtime.utils.APIClient;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class PlaceViewModel extends BaseObservable {

    public void getPlaces(Location location) {

        // Get application context
        Context context = LunchTimeApplication.CONTEXT;

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
                .flatMap(new Func1<LinkedTreeMap<String, Object>, Observable<Place>>() {
                    @Override
                    public Observable<Place> call(LinkedTreeMap<String, Object> object) {
                        Gson gson = new Gson();
                        JsonArray jsonArray = gson.toJsonTree(object.get("results")).getAsJsonArray();

                        return Observable.just(gson.fromJson(jsonArray, ));
                    }
                })
                .toList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Place>>() {
                    @Override
                    public void onCompleted() {
                        int i = 0;
                    }

                    @Override
                    public void onError(Throwable e) {
                        int i = 0;
                    }

                    @Override
                    public void onNext(List<Place> places) {
                        int i = 0;
                    }
                });
    }
}
