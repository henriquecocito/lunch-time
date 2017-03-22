package br.com.henriquecocito.lunchtime;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.hardware.camera2.params.Face;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.firebase.analytics.FirebaseAnalytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class LunchTimeApplication extends Application{

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
