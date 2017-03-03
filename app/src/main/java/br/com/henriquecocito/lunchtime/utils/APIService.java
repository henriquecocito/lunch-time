package br.com.henriquecocito.lunchtime.utils;

import java.util.Map;

import br.com.henriquecocito.lunchtime.model.Places;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public interface APIService {

    @GET("/maps/api/place/nearbysearch/json")
    Call<Places> getPlaces(@QueryMap Map<String, String> queryMap);
}
