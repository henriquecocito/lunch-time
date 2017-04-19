package br.com.henriquecocito.lunchtime.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import br.com.henriquecocito.lunchtime.BR;
import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.utils.APIClient;
import br.com.henriquecocito.lunchtime.utils.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class PlaceViewModel extends BaseObservable {

    public static final int PLACE_SORT_NAME = 0;
    public static final int PLACE_SORT_DISTANCE = 1;
    public static final int PLACE_SORT_RATING = 2;

    private Activity mActivity;
    private PlaceDataListener mDataListener;
    private ArrayList<Place> mPlaces = new ArrayList<>();
    private boolean mIsLoading = false;
    private boolean mIsError = false;
    private Location mLocation;

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

    @Bindable
    public boolean isLoading() {
        return mIsLoading;
    }

    @Bindable
    public boolean isError() {
        return mIsError;
    }

    private void showError() {
        mIsLoading = false;
        mIsError = true;
        notifyPropertyChanged(BR.loading);
        notifyPropertyChanged(BR.error);
    }

    private void showLoading() {
        mIsLoading = true;
        mIsError = false;
        notifyPropertyChanged(BR.loading);
        notifyPropertyChanged(BR.error);
    }

    private void showList() {
        mIsLoading = false;
        mIsError = false;
        notifyPropertyChanged(BR.loading);
        notifyPropertyChanged(BR.error);
    }

    public void getPlaces() {

        showLoading();

        Utils
                .getLocation(mActivity)
                .subscribe(new Subscriber<Location>() {
                    @Override
                    public void onCompleted() {
                        showList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showError();
                        mDataListener.onError(e);
                    }

                    @Override
                    public void onNext(Location location) {
                        if(location != null) {
                            mLocation = location;
                            mDataListener.onLocalized(location);
                            getNearByPlaces(location);
                        }
                    }
                });
    }

    public void getNearByPlaces(Location location) {

        showLoading();

        Utils
                .getNearByPlaces(location)
                .subscribe(new Subscriber<List<Place>>() {
                    @Override
                    public void onCompleted() {
                        if(mPlaces.size() == 0) {
                            mDataListener.onEmpty();
                        }
                        mDataListener.onCompleted(mPlaces);
                        showList();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDataListener.onError(e);
                        showError();
                    }

                    @Override
                    public void onNext(List<Place> places) {
                        if(places.size() > 0) {
                            mPlaces.clear();
                            mPlaces.addAll(places);

                            sortBy(Utils.getPreference().getInt(Utils.PREF_SORT_KEY, 0));
                        }
                    }
                });
    }

    public void sortBy(final int item) {

        Utils.setPreference(Utils.PREF_SORT_KEY, item);

        Collections.sort(mPlaces, new Comparator<Place>() {
            @Override
            public int compare(Place place, Place place2) {
                switch (item) {
                    case PLACE_SORT_NAME:
                        return place.getName().compareTo(place2.getName());
                    case PLACE_SORT_DISTANCE:
                        if(mLocation != null) {
                            return Float.compare(mLocation.distanceTo(place.getGeometry()), mLocation.distanceTo(place2.getGeometry()));
                        }
                        return 0;
                    case PLACE_SORT_RATING:
                        return Double.compare(place2.getRating(), place.getRating());
                    default:
                        return 0;
                }
            }
        });
        mDataListener.onCompleted(mPlaces);
    }
}
