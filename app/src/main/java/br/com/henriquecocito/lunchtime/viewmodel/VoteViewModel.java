package br.com.henriquecocito.lunchtime.viewmodel;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import br.com.henriquecocito.lunchtime.model.Vote;

/**
 * Created by HenriqueCocito on 04/03/17.
 */

public class VoteViewModel {

    private VoteDataListener mDataListener;

    public interface VoteDataListener {
        void onVoteChanged();
        void onVoteError(Throwable error);
    }

    public void setDataListener(VoteDataListener dataListener) {
        this.mDataListener = dataListener;
    }

    public void vote(String placeId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference collection = database.getReference("votes");

        Vote vote = new Vote();
        vote.setUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        vote.setPlaceId(placeId);
        vote.setCreated(new Date());

        collection
                .child(String.format("%s-%s", placeId, FirebaseAuth.getInstance().getCurrentUser().getUid()))
                .setValue(vote)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDataListener.onVoteChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDataListener.onVoteError(e);
                    }
                });

    }
}
