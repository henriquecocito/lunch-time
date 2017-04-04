package br.com.henriquecocito.lunchtime.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.location.Location;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
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
import br.com.henriquecocito.lunchtime.utils.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class PlaceViewModel extends BaseObservable {

    private Activity mActivity;
    private PlaceDataListener mDataListener;
    private ArrayList<Place> mPlaces = new ArrayList<>();
    private boolean mLoading = false;

    public PlaceViewModel(Activity activity, PlaceDataListener dataListener) {
        this.mActivity = activity;
        this.mDataListener = dataListener;
    }

    public interface PlaceDataListener {
        void onEmpty();
        void onLocalized(Location location);
        void onCompleted(List<Place> places);
        void onError(Throwable error);
    }

    public void setLoading(boolean loading) {
        this.mLoading = loading;
        notifyPropertyChanged(BR.loading);
    }

    @Bindable
    public boolean isLoading() {
        return mLoading;
    }

    public void getPlaces() {
        setLoading(true);
        Utils
                .getLocation(mActivity)
                .subscribe(new Subscriber<Location>() {
                    @Override
                    public void onCompleted() {
                        setLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        int i = 0;
                    }

                    @Override
                    public void onNext(Location location) {
                        mDataListener.onLocalized(location);
                        getNearByPlaces(location);
                    }
                });
    }

    public void getNearByPlaces(Location location) {

        setLoading(true);

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
                        setLoading(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDataListener.onError(e);
                        setLoading(false);
                    }

                    @Override
                    public void onNext(List<Place> places) {
                        mPlaces.addAll(places);
                    }
                });
    }
}
