package br.com.henriquecocito.lunchtime.view.fragments;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.adapter.BaseAdapter;
import br.com.henriquecocito.lunchtime.databinding.FragmentListBinding;
import br.com.henriquecocito.lunchtime.utils.BaseFragment;
import br.com.henriquecocito.lunchtime.viewmodel.PlaceViewModel;

/**
 * Created by HenriqueCocito on 22/03/17.
 */

public class ListFragment extends Fragment implements BaseFragment {

    FragmentListBinding mView;
    BaseAdapter baseAdapter;
    PlaceViewModel mPlaceViewModel = new PlaceViewModel();

    @Override
    public String getFragmentTitle() {
        return LunchTimeApplication.CONTEXT.getString(R.string.action_list);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        return mView.list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        baseAdapter = new BaseAdapter(getActivity(), new ArrayList<String>());

        mPlaceViewModel.getPlaces(new Location("gps"));

        mView.list.setAdapter(baseAdapter);
    }
}
