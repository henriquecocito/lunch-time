package br.com.henriquecocito.lunchtime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenriqueCocito on 03/03/17.
 */
public class Place {
    @SerializedName("adr_address")
    @Expose
    private String adrAddress;

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("international_phone_number")
    @Expose
    private String internationalPhoneNumber;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("photos")
    @Expose
    private List<Photos> photos = new ArrayList<>();

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("scope")
    @Expose
    private String scope;

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("utc_offset")
    @Expose
    private Integer utcOffset;

    @SerializedName("vicinity")
    @Expose
    private String vicinity;

    @SerializedName("website")
    @Expose
    private String website;

    public String getAdrAddress() {
        return adrAddress;
    }

    public void setAdrAddress(String adrAddress) {
        this.adrAddress = adrAddress;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInternationalPhoneNumber() {
        return internationalPhoneNumber;
    }

    public void setInternationalPhoneNumber(String internationalPhoneNumber) {
        this.internationalPhoneNumber = internationalPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photos> getPhotos() {
        return this.photos;
    }

    public void setPhoto(List<Photos> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUtcOffset() {
        return utcOffset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utcOffset = utcOffset;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}