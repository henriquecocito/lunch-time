package br.com.henriquecocito.lunchtime.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.model.Maps;
import br.com.henriquecocito.lunchtime.utils.BaseFragment;
import br.com.henriquecocito.lunchtime.view.activities.DetailActivity;
import br.com.henriquecocito.lunchtime.view.activities.MainActivity;
import br.com.henriquecocito.lunchtime.viewmodel.MapViewModel;

/**
 * Created by HenriqueCocito on 02/03/17.
 */

public class MapFragment extends SupportMapFragment implements BaseFragment, MapViewModel.MapDataListener {

    private MapViewModel mMapViewModel;
    private GoogleMap mGoogleMap;
    private Location mLocation;

    @Override
    public String getFragmentTitle() {
        return LunchTimeApplication.CONTEXT.getString(R.string.action_map);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        mMapViewModel = new MapViewModel();
        mMapViewModel.setDataListener(this);

        mLocation = getLastKnownLocation();

//        setupGoogleMaps();
    }

    @Override
    public void onMapChanged(ArrayList<Maps> maps) {
        for(Maps map : maps) {
            pinMap(map);
        }
    }

    @Override
    public void onMapError(Throwable error) {
        Snackbar.make(getView(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMapLoaded(final ArrayList<Maps> maps) {

        // Zoom camera
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), 17));

        // Set click on place information
        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                for (Maps result : maps) {
                    if(result.getId() == marker.getTag()) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("placeid", result.getPlaceId());
                        intent.putExtra("reference", result.getReference());
                        getActivity().startActivity(intent);
                        break;
                    }
                }
            }
        });
    }

    public void setupGoogleMaps() {

        getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(final GoogleMap googleMap) {

                mGoogleMap = googleMap;
                mGoogleMap.clear();

                // Request Permission
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MainActivity.REQUEST_PERMISSIONS
                    );
                    return;
                }
                mGoogleMap.setMyLocationEnabled(true);

                if(mLocation != null) {
                    mMapViewModel.getNearbyPlaces(mLocation);
                } else {
                    Snackbar.make(getView(), "Can't get current position", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void pinMap(Maps map) {

        // Check permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MainActivity.REQUEST_PERMISSIONS
            );
            return;
        }

        // Get coordinates
        LinkedTreeMap<String, Double> coordinates = (LinkedTreeMap<String, Double>) map.getGeometry().get("location");

        // Create pin
        final MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(coordinates.get("lat"), coordinates.get("lng")));
        markerOptions.title(map.getName());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        // Pin it on map
        Marker marker = mGoogleMap.addMarker(markerOptions);
        marker.setTag(map.getId());
    }

    @Nullable
    private Location getLastKnownLocation() {

        // Check permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
         && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    MainActivity.REQUEST_PERMISSIONS
            );
            return null;
        }

        // Get list of location providers
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location bestLocation = null;

        // Get the best location provided
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
