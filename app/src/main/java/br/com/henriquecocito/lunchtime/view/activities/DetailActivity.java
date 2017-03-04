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

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.databinding.ActivityPlaceDetailBinding;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.viewmodel.PlaceViewModel;
import br.com.henriquecocito.lunchtime.viewmodel.VoteViewModel;

/**
 * Created by HenriqueCocito on 03/03/17.
 */

public class DetailActivity extends AppCompatActivity implements PlaceViewModel.PlaceDataListener, VoteViewModel.VoteDataListener {

    ActivityPlaceDetailBinding mView;
    PlaceViewModel mPlaceViewModel;
    VoteViewModel mVoteViewModel;

    boolean userVoted = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = DataBindingUtil.setContentView(this, R.layout.activity_place_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPlaceViewModel = new PlaceViewModel();
        mPlaceViewModel.setDataListener(this);
        mPlaceViewModel.loadDetail(getIntent().getExtras().getString("placeid"));

        mVoteViewModel = new VoteViewModel();
        mVoteViewModel.setDataListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);

        if(!userVoted) {
            menu.findItem(R.id.nav_vote).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.nav_vote:
                mVoteViewModel.vote(mPlaceViewModel.getPlaceId());
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
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");
        mVoteViewModel.loadVotes(df.format(new Date()), mPlaceViewModel.getPlaceId());
        mView.setPlace(mPlaceViewModel);

        getSupportActionBar().setTitle(mPlaceViewModel.getName());
    }

    @Override
    public void onVoteChanged(HashMap<String, Object> object) {
        userVoted = false;

        if(object != null) {

            long votes = 0;

            Iterator it = object.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                if(pair.getKey().equals(mPlaceViewModel.getPlaceId())) {
                    votes++;
                }

                HashMap<String, Object> placeMap = (HashMap<String, Object>) object.get(pair.getKey());
                Iterator it2 = placeMap.entrySet().iterator();
                while (it2.hasNext()) {
                    Map.Entry pair2 = (Map.Entry)it2.next();
                    if(pair2.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        userVoted = true;
                        break;
                    }
                    it2.remove();
                }
                it.remove();
            }
            mVoteViewModel.setVotes(votes);
        }

        mView.setVote(mVoteViewModel);
        invalidateOptionsMenu();
    }

    @Override
    public void onVoteError(Throwable error) {
        Log.e("onVote", error.getLocalizedMessage());
        Snackbar.make(mView.getRoot(), error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onVoteCompleted() {
        userVoted = true;
        invalidateOptionsMenu();

        Snackbar.make(mView.getRoot(), "Your vote have been sent successfully", Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                super.onDismissed(snackbar, event);
                finish();
            }
        }).show();
    }

    @BindingAdapter("app:photoUrl")
    public static void setPhotoUrl(ImageView v, String url) {
        Picasso.with(v.getContext().getApplicationContext())
                .load(url)
                .placeholder(v.getDrawable())
                .into(v);
    }
}