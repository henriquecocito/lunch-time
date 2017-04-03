package br.com.henriquecocito.lunchtime.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Place;
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

    private PlaceDataListener mDataListener;
    private ArrayList<Place> mPlaces = new ArrayList<>();

    public PlaceViewModel(PlaceDataListener dataListener) {
        this.mDataListener = dataListener;
    }

    public interface PlaceDataListener {
        void onEmpty();
        void onCompleted(List<Place> places);
        void onError(Throwable error);
    }

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
                .flatMap(new Func1<LinkedTreeMap<String, Object>, Observable<List<Place>>>() {
                    @Override
                    public Observable<List<Place>> call(LinkedTreeMap<String, Object> object) {
                        Gson gson = new Gson();
                        JsonArray jsonArray = gson.toJsonTree(object.get("results")).getAsJsonArray();
                        Type listType = new TypeToken<List<Place>>() {}.getType();
                        return Observable.just((List<Place>) gson.fromJson(jsonArray, listType));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Place>>() {
                    @Override
                    public void onCompleted() {
                        if(mPlaces.size() == 0) {
                            mDataListener.onEmpty();
                        }
                        mDataListener.onCompleted(mPlaces);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDataListener.onError(e);
                    }

                    @Override
                    public void onNext(List<Place> places) {
                        mPlaces.addAll(places);
                    }
                });
    }
}
