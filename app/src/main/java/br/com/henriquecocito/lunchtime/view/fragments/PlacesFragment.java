package br.com.henriquecocito.lunchtime.view.fragments;

import android.support.v4.app.Fragment;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.utils.BaseFragment;

/**
 * Created by HenriqueCocito on 22/03/17.
 */

public class PlacesFragment extends Fragment implements BaseFragment {

    @Override
    public String getFragmentTitle() {
        return LunchTimeApplication.CONTEXT.getString(R.string.action_list);
    }
}
