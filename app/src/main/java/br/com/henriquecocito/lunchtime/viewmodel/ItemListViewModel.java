package br.com.henriquecocito.lunchtime.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.location.Location;
import android.net.Uri;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Place;

/**
 * Created by hrcocito on 29/03/17.
 */

public class ItemListViewModel extends BaseObservable {

    private Location mCurrentLocation;
    private Place mPlace;

    public ItemListViewModel(Location location, Place place) {
        this.mCurrentLocation = location;
        this.mPlace = place;
    }

    @Bindable
    public String getName() {
        return mPlace.getName();
    }

    @Bindable
    public float getRating() {
        return (float) mPlace.getRating();
    }

    @Bindable
    public String getVicinity() {
        return mPlace.getVicinity();
    }

    @Bindable
    public String getPicture() {
        if(mPlace.getPhotos().size() > 0) {
            return new Uri.Builder()
                    .scheme("https")
                    .authority("maps.googleapis.com")
                    .appendPath("maps")
                    .appendPath("api")
                    .appendPath("place")
                    .appendPath("photo")
                    .appendQueryParameter("key", LunchTimeApplication.CONTEXT.getString(R.string.googleApiKey))
                    .appendQueryParameter("photoreference", (String) mPlace.getPhotos().get(0).get("photo_reference"))
                    .appendQueryParameter("maxwidth", "1080")
                    .toString();
        }
        return "";
    }

    @Bindable
    public String getDistance() {
        return String.format("%d m", Math.round(mCurrentLocation.distanceTo(mPlace.getGeometry())));
    }
}
