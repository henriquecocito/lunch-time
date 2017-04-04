package br.com.henriquecocito.lunchtime.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.*;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import rx.Observable;
import rx.Subscriber;

import static br.com.henriquecocito.lunchtime.LunchTimeApplication.REQUEST_PERMISSIONS;

/**
 * Created by hrcocito on 03/04/17.
 */

public class Utils {

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

    public static Observable<Location> getLocation(final Activity activity) {

        return Observable.create(new Observable.OnSubscribe<Location>() {
            @Override
            public void call(final Subscriber<? super Location> subscriber) {
                LocationManager mLocationManager = (LocationManager) LunchTimeApplication.CONTEXT.getSystemService(LunchTimeApplication.CONTEXT.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 10, new android.location.LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        subscriber.onNext(location);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {
                    }

                    @Override
                    public void onProviderEnabled(String s) {
                    }

                    @Override
                    public void onProviderDisabled(String s) {
                        subscriber.onError(new Throwable(s));
                    }
                });
            }
        });
    }
}
