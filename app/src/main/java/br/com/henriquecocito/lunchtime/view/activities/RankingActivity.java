package br.com.henriquecocito.lunchtime.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.adapter.RankingAdapter;
import br.com.henriquecocito.lunchtime.databinding.ActivityRankingBinding;
import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.model.Vote;
import br.com.henriquecocito.lunchtime.viewmodel.VoteViewModel;
import rx.Observable;

/**
 * Created by HenriqueCocito on 04/03/17.
 */

public class RankingActivity extends AppCompatActivity implements VoteViewModel.VoteDataListener {

    ActivityRankingBinding mView;
    ArrayList<Vote> mVotes = new ArrayList<>();
    RankingAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new RankingAdapter(this, mVotes);

        mView = DataBindingUtil.setContentView(this, R.layout.activity_ranking);
        mView.list.setAdapter(mAdapter);

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy");

        VoteViewModel mViewModel = new VoteViewModel();
        mViewModel.setDataListener(this);
        mViewModel.loadVotes(df.format(new Date()));
    }

    @Override
    public void onVoteChanged(HashMap<String, Object> object) {
        Iterator it = object.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();

            HashMap<String, Object> placeMap = (HashMap<String, Object>) object.get(pair.getKey());
            Iterator it2 = placeMap.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair2 = (Map.Entry) it2.next();

                Gson gson = new Gson();
                JsonObject jsonObject = gson.toJsonTree(placeMap.get(pair2.getKey())).getAsJsonObject();
                mVotes.add(gson.fromJson(jsonObject, Vote.class));

                it2.remove();
            }
            it.remove();
        }
    }

    @Override
    public void onVoteError(Throwable error) {
        int i = 0;
    }

    @Override
    public void onVoteCompleted() {
        mAdapter.notifyDataSetChanged();
    }
}
