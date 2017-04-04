package br.com.henriquecocito.lunchtime;

import android.*;
import android.app.Activity;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.params.Face;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.firebase.analytics.FirebaseAnalytics;

import br.com.henriquecocito.lunchtime.view.activities.MainActivity;
import io.fabric.sdk.android.Fabric;
import rx.Observable;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class LunchTimeApplication extends Application {

    public final static int REQUEST_PERMISSIONS = 2;

    public static Context CONTEXT;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Fabric.with(this, new Crashlytics());

        CONTEXT = getApplicationContext();
    }

    public static void hideBars(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
