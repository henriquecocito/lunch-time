package br.com.henriquecocito.lunchtime.view.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

        return mView.list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.onRefresh();
    }

    @Override
    public void onRefresh() {
        mPlaceViewModel.getPlaces();
    }

    @Override
    public void onEmpty() {

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
    }

    @Override
    public void onError(Throwable error) {
        Snackbar.make(mView.getRoot(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG).setAction(getString(R.string.btn_try_again), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListFragment.this.onRefresh();
            }
        });
    }
}
