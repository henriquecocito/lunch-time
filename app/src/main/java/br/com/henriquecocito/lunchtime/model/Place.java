package br.com.henriquecocito.lunchtime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenriqueCocito on 03/03/17.
 */
public class Place {

    @Expose
    @SerializedName("place_id")
    private String mId;

    @Expose
    @SerializedName("name")
    private String mName;

    @Expose
    @SerializedName("geometry")
    private JSONObject mGeometry;

    @Expose
    @SerializedName("photos")
    private ArrayList<JSONObject> mPhotos;

    @Expose
    @SerializedName("rating")
    private double mRating;

    @Expose
    @SerializedName("types")
    private ArrayList<String> mTypes;

    @Expose
    @SerializedName("vicinity")
    private String mVicinity;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public JSONObject getGeometry() {
        return mGeometry;
    }

    public void setGeometry(JSONObject geometry) {
        this.mGeometry = geometry;
    }

    public ArrayList<JSONObject> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(ArrayList<JSONObject> photos) {
        this.mPhotos = photos;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        this.mRating = rating;
    }

    public ArrayList<String> getTypes() {
        return mTypes;
    }

    public void setTypes(ArrayList<String> types) {
        this.mTypes = types;
    }

    public String getVicinity() {
        return mVicinity;
    }

    public void setVicinity(String vicinity) {
        this.mVicinity = vicinity;
    }
}