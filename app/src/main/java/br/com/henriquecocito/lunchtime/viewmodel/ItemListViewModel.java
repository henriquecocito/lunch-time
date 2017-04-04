package br.com.henriquecocito.lunchtime.viewmodel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.google.gson.internal.LinkedHashTreeMap;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.utils.Utils;

import static android.content.Context.LOCATION_SERVICE;

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

    public String getPicture() {
        return "";
    }

    @Bindable
    public String getDistance() {
        return String.format("%d m", Math.round(mCurrentLocation.distanceTo(mPlace.getGeometry())));
    }
}
