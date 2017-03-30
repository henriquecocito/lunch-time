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

}
