package br.com.henriquecocito.lunchtime.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Places;
import br.com.henriquecocito.lunchtime.model.Result;
import br.com.henriquecocito.lunchtime.utils.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HenriqueCocito on 02/03/17.
 */

public class MapFragment extends SupportMapFragment {

    public final static int REQUEST_PERMISSIONS = 2;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setupGoogleMaps();
    }

    public void setupGoogleMaps() {

        getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final GoogleMap googleMap) {

                // Request Permission
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            REQUEST_PERMISSIONS
                    );
                    return;
                }
                getNearbyPlaces(googleMap);
                googleMap.setMyLocationEnabled(true);
            }
        });
    }

    private void getNearbyPlaces(final GoogleMap googleMap) {

        Location location = getLastKnownLocation();

        if (location != null) {

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            HashMap<String, String> params = new HashMap<>();
            params.put("key", getString(R.string.apiKey));
            params.put("sensor", "true");
            params.put("radius", sharedPreferences.getString(getString(R.string.pref_key_radius), "200"));
            params.put("types", "restaurant");
            params.put("location", String.format("%s,%s", location.getLatitude(), location.getLongitude()));

            APIClient.getInstance().getPlaces(params).enqueue(new Callback<Places>() {

                @Override
                public void onResponse(Call<Places> call, Response<Places> response) {
                    try {

                        googleMap.clear();

                        for (int i = 0; i < response.body().getResults().size(); i++) {

                            Result result = response.body().getResults().get(i);

                            Double lat = result.getGeometry().getLocation().getLat();
                            Double lng = result.getGeometry().getLocation().getLng();

                            pinMap(new LatLng(lat, lng), result.getName(), googleMap);
                        }
                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Places> call, Throwable t) {
                    Snackbar.make(getView(), "Can't get nearby restaurants", Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Snackbar.make(getView(), "Can't get current position", Snackbar.LENGTH_LONG).show();
        }
    }

    private void pinMap(LatLng latLng, String name, GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS
            );
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(name);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        googleMap.addMarker(markerOptions);
    }


    private Location getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS
            );
            return null;
        }

        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location bestLocation = null;
        for (String provider : locationManager.getProviders(true)) {

            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                continue;
            }
            if (bestLocation == null || location.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = location;
            }
        }
        return bestLocation;
    }
}
