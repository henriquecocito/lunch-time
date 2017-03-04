package br.com.henriquecocito.lunchtime.viewmodel;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
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

    private Place mPlace;
    private PlaceDataListener mDataListener;

    public interface PlaceDataListener {
        void onPlaceChanged(Place place);
        void onPlaceError(Throwable error);
        void onPlaceLoaded();
    }

    public void setDataListener(PlaceDataListener dataListener){
        this.mDataListener = dataListener;
    }

    public void setPlace(Place place) {
        this.mPlace = place;
    }

    // Fields to Data Binding
    public String getPlaceId() {
        return mPlace.getPlaceId();
    }

    public String getAddress() {
        return mPlace.getFormattedAddress();
    }

    public String getName() {
        return mPlace.getName();
    }

    public String getWebsite() {
        return mPlace.getWebsite();
    }

    public String getPhone() {
        return mPlace.getInternationalPhoneNumber();
    }

    public String getPhoto() {
        if(mPlace.getPhotos().size() > 0) {
            StringBuffer sb = new StringBuffer("https://maps.googleapis.com/maps/api/place/photo?");
            sb.append(String.format("photoreference=%s", mPlace.getPhotos().get(0).getPhotoReference()));
            sb.append(String.format("&key=%s", LunchTimeApplication.CONTEXT.getString(R.string.googleApiKey)));
            sb.append("&maxwidth=1280");

            return sb.toString();
        }
        return null;
    }

    // API method
    public void loadPlaces(String placeId) {

        HashMap<String, String> params = new HashMap<>();
        params.put("key", LunchTimeApplication.CONTEXT.getString(R.string.googleApiKey));
        params.put("placeid", placeId);

        APIClient
                .getInstance()
                .getPlaceDetail(params)
                .flatMap(new Func1<LinkedTreeMap<String, Object>, Observable<Place>>() {
                    @Override
                    public Observable<Place> call(LinkedTreeMap<String, Object> object) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.toJsonTree(object.get("result")).getAsJsonObject();
                        return Observable.just(gson.fromJson(jsonObject, Place.class));
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Place>() {
                    @Override
                    public void onCompleted() {
                        mDataListener.onPlaceLoaded();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mDataListener.onPlaceError(e);
                    }

                    @Override
                    public void onNext(Place place) {
                        setPlace(place);
                        mDataListener.onPlaceChanged(place);
                    }
                });
    }
}
