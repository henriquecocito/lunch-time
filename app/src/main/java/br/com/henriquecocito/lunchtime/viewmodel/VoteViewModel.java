package br.com.henriquecocito.lunchtime.viewmodel;

import android.databinding.BaseObservable;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import br.com.henriquecocito.lunchtime.model.Vote;

/**
 * Created by HenriqueCocito on 04/03/17.
 */

public class VoteViewModel extends BaseObservable{

    private FirebaseDatabase mDatabase;
    private DatabaseReference mCollection;

    private long mVotes = 0;

    private VoteDataListener mDataListener;

    public interface VoteDataListener {
        void onVoteChanged(HashMap<String, Object> object);
        void onVoteError(Throwable error);
        void onVoteCompleted();
    }

    public void setDataListener(VoteDataListener dataListener) {
        this.mDataListener = dataListener;
    }

    public VoteViewModel() {
        mDatabase = FirebaseDatabase.getInstance();
        mCollection = mDatabase.getReference("votes");
    }

    public void setVotes(long votes) {
        this.mVotes = votes;
    }

    public String getVotes() {
        return String.valueOf(this.mVotes);
    }

    public void vote(String placeId, String placeName) {

        Vote vote = new Vote();
        vote.setUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        vote.setPlaceId(placeId);
        vote.setPlaceName(placeName);
        vote.setCreated(new Date());

        mCollection
                .child(vote.getCreated())
                .child(placeId)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(vote)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mDataListener.onVoteCompleted();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mDataListener.onVoteError(e);
                    }
                });

    }

    public void loadVotes(String date) {

        mCollection
                .child(date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mDataListener.onVoteChanged((HashMap<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        mDataListener.onVoteError(new Throwable(databaseError.getMessage()));
                    }
                });
    }
}
