package br.com.henriquecocito.lunchtime.view.activities;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.databinding.ActivityPlaceDetailBinding;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.viewmodel.PlaceViewModel;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class PlaceDetailActivity extends AppCompatActivity implements PlaceViewModel.PlaceDataListener {

    ActivityPlaceDetailBinding mView;
    PlaceViewModel mPlaceViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = DataBindingUtil.setContentView(this, R.layout.activity_place_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPlaceViewModel = new PlaceViewModel();
        mPlaceViewModel.setDataListener(this);
        mPlaceViewModel.loadPlaces(getIntent().getExtras().getString("placeid"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.nav_vote:
                mPlaceViewModel.vote();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlaceChanged(Place place) {

    }

    @Override
    public void onPlaceError(Throwable error) {
        Log.e("onPlace", error.getLocalizedMessage());
        Snackbar.make(mView.getRoot(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onPlaceLoaded() {

        mView.setPlace(mPlaceViewModel);
        getSupportActionBar().setTitle(mPlaceViewModel.getName());
    }

    @Override
    public void onVoteChanged() {
        Snackbar.make(mView.getRoot(), "Your vote have been sent successfully", Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                finish();
            }
        }).show();
    }

    @Override
    public void onVoteError(Throwable error) {
        Log.e("onVote", error.getLocalizedMessage());
        Snackbar.make(mView.getRoot(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(ImageView v, String url) {
        Picasso.with(v.getContext().getApplicationContext())
                .load(url)
                .into(v);
    }
}
