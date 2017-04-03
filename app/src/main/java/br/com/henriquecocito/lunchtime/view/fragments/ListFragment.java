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
import br.com.henriquecocito.lunchtime.utils.BaseFragment;
import br.com.henriquecocito.lunchtime.view.activities.MainActivity;
import br.com.henriquecocito.lunchtime.viewmodel.PlaceViewModel;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by HenriqueCocito on 22/03/17.
 */

public class ListFragment extends Fragment implements BaseFragment, SwipeRefreshLayout.OnRefreshListener, PlaceViewModel.PlaceDataListener {

    FragmentListBinding mView;
    MainAdapter mainAdapter;
    List<Place> mPlaces = new ArrayList<>();
    PlaceViewModel mPlaceViewModel = new PlaceViewModel(this);

    @Override
    public String getFragmentTitle() {
        return LunchTimeApplication.CONTEXT.getString(R.string.action_list);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mView.refresh.setOnRefreshListener(this);

        mView.list.setLayoutManager(new LinearLayoutManager(getActivity()));

        mainAdapter = new MainAdapter(getActivity(), mPlaces);
        mView.list.setAdapter(mainAdapter);

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
        mPlaceViewModel.getPlaces(LunchTimeApplication.getLocation());
    }

    @Override
    public void onEmpty() {

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
