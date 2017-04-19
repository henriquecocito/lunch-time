package br.com.henriquecocito.lunchtime.view.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.adapter.MainAdapter;
import br.com.henriquecocito.lunchtime.databinding.FragmentListBinding;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.utils.APIClient;
import br.com.henriquecocito.lunchtime.utils.BaseFragment;
import br.com.henriquecocito.lunchtime.utils.Utils;
import br.com.henriquecocito.lunchtime.view.activities.MainActivity;
import br.com.henriquecocito.lunchtime.viewmodel.PlaceViewModel;
import rx.Subscriber;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by HenriqueCocito on 22/03/17.
 */

public class ListFragment extends Fragment implements BaseFragment, SwipeRefreshLayout.OnRefreshListener, PlaceViewModel.PlaceDataListener {

    private FragmentListBinding mView;
    private MainAdapter mainAdapter;
    private Location mLocation = new Location(LocationManager.GPS_PROVIDER);
    private List<Place> mPlaces = new ArrayList<>();
    private PlaceViewModel mPlaceViewModel;
    private AlertDialog.Builder mAlertDialog;

    @Override
    public String getFragmentTitle() {
        return LunchTimeApplication.CONTEXT.getString(R.string.action_list);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        mPlaceViewModel = new PlaceViewModel(getActivity(), this);
        mView.setPlaceViewModel(mPlaceViewModel);

        mainAdapter = new MainAdapter(getActivity(), mLocation, mPlaces);
        mView.list.setAdapter(mainAdapter);
        mView.list.setLayoutManager(new LinearLayoutManager(getActivity()));

        mView.refresh.setOnRefreshListener(this);
        onRefresh();

        setHasOptionsMenu(true);

        return mView.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_restaurants, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_filter:
                SharedPreferences preferences = getActivity().getSharedPreferences(Utils.PREF_KEY, Context.MODE_PRIVATE);
                CharSequence items[] = new CharSequence[] {"Name", "Distance", "Rating"};

                mAlertDialog = new AlertDialog.Builder(getActivity());
                mAlertDialog.setTitle("Sort by");
                mAlertDialog.setSingleChoiceItems(items, preferences.getInt(Utils.PREF_SORT_KEY, 0), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface d, int n) {
                        switch(n) {
                            case 0:
                                mPlaceViewModel.sortBy(PlaceViewModel.PLACE_SORT_NAME);
                                break;
                            case 1:
                                mPlaceViewModel.sortBy(PlaceViewModel.PLACE_SORT_DISTANCE);
                                break;
                            case 2:
                                mPlaceViewModel.sortBy(PlaceViewModel.PLACE_SORT_RATING);
                                break;
                            default:
                                break;
                        }
                        mAlertDialog = null;
                        d.dismiss();
                    }

                });
                mAlertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        mPlaceViewModel.getPlaces();
    }

    @Override
    public void onEmpty() {
        mView.refresh.setRefreshing(false);
    }

    @Override
    public void onLocalized(Location location) {
        mLocation.set(location);
        mainAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleted(List<Place> places) {
        mPlaces.clear();
        mPlaces.addAll(places);
        mainAdapter.notifyDataSetChanged();

        mView.refresh.setRefreshing(false);
    }

    @Override
    public void onError(Throwable error) {

        mView.refresh.setRefreshing(false);

        if(error.getMessage() == getString(R.string.error_permission_location)) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LunchTimeApplication.REQUEST_PERMISSIONS
            );
        } else{
            Snackbar
                    .make(getActivity().findViewById(R.id.rootView), error.getLocalizedMessage(), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.btn_try_again), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ListFragment.this.onRefresh();
                        }
                    })
                    .show();
        }
    }
}
