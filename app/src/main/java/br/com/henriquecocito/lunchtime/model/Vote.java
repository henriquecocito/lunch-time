package br.com.henriquecocito.lunchtime.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HenriqueCocito on 04/03/17.
 */

public class Vote {

    private String mUserId;
    private String mPlaceId;
    private String mCreated;

    public String getUserId() {
        return mUserId;
    }

    public void setUser(String userId) {
        this.mUserId = userId;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String placeId) {
        this.mPlaceId = placeId;
    }

    public String getCreated() {
        return this.mCreated;
    }

    public void setCreated(Date created) {
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        this.mCreated = df.format(created);
    }
}
