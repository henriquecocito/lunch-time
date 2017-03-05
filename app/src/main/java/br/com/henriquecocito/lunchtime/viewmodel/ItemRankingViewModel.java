package br.com.henriquecocito.lunchtime.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import br.com.henriquecocito.lunchtime.model.Place;
import br.com.henriquecocito.lunchtime.model.Vote;

/**
 * Created by HenriqueCocito on 04/03/17.
 */

public class ItemRankingViewModel extends BaseObservable {
    private Context mContext;
    private Vote mVote;

    public ItemRankingViewModel(Context context, Vote vote) {
        this.mContext = context;
        this.mVote = vote;
    }

    public void onClick(View v) {
        int i = 0;
    }
}
