package br.com.henriquecocito.lunchtime;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class LunchTimeApplication extends Application{

    public static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        CONTEXT = getApplicationContext();
    }
}
