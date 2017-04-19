package br.com.henriquecocito.lunchtime.utils;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class APIClient {

    public static APIService mService;

    public APIClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        mService = retrofit.create(APIService.class);
    }

    public static APIService getInstance() {

        if (mService == null) {
            new APIClient();
        }

        return mService;
    }
}