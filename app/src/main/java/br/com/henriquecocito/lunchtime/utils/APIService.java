package br.com.henriquecocito.lunchtime.utils;

import android.graphics.Bitmap;

import com.google.gson.internal.LinkedTreeMap;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public interface APIService {

    @GET("maps/api/place/nearbysearch/json")
    Observable<LinkedTreeMap<String, Object>> getPlaces(@QueryMap Map<String, String> queryMap);

    @GET("maps/api/place/details/json")
    Observable<LinkedTreeMap<String, Object>> getPlaceDetail(@QueryMap Map<String, String> queryMap);
}
