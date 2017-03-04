package br.com.henriquecocito.lunchtime.view.activities;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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

    }

    @Override
    public void onPlaceLoaded() {

        mView.setPlace(mPlaceViewModel);
        getSupportActionBar().setTitle(mPlaceViewModel.getName());
    }


    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(ImageView v, String url) {
        Picasso.with(v.getContext().getApplicationContext())
                .load(url)
                .into(v);
    }
}
