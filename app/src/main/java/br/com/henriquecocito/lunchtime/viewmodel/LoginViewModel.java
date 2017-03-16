package br.com.henriquecocito.lunchtime.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.henriquecocito.lunchtime.R;

/**
 * Created by hrcocito on 06/03/17.
 */

public class LoginViewModel extends BaseObservable implements GoogleApiClient.OnConnectionFailedListener, OnFailureListener, OnCompleteListener {

    public static final int LOGIN_GOOGLE = 1;

    private static FirebaseAuth.AuthStateListener mAuthListener;

    private boolean mIsLoading = false;
    private boolean mIsEmail = false;
    private ObservableField<String> mUsername = new ObservableField<>();
    private ObservableField<String> mPassword = new ObservableField<>();

    private FragmentActivity mActivity;
    private LoginListener mDataListener;

    public interface LoginListener {
        void onLogin(Object object);
        void onLoginSuccess(FirebaseUser user);
        void onLoginError(Throwable error);
    }

    public LoginViewModel(FragmentActivity activity, LoginListener dataListener) {
        this.mActivity = activity;
        this.mDataListener = dataListener;
    }

    @Bindable
    public boolean isLoading() {
        return mIsLoading;
    }

    @Bindable
    public boolean isEmail() {
        return mIsEmail;
    }

    @Bindable
    public String getUsername() {
        return mUsername.get();
    }

    @Bindable
    public String getPassword() {
        return mPassword.get();
    }

    public void setLoading(boolean loading) {
        mIsLoading = loading;
        notifyChange();
    }

    public void setEmail(boolean email) {
        this.mIsEmail = email;
        notifyChange();
    }

    public void setUsername(String username) {
        this.mUsername.set(username);
        notifyChange();
    }

    public void setPassword(String password) {
        this.mPassword.set(password);
        notifyChange();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setLoading(false);
        mDataListener.onLoginError(new Throwable(connectionResult.getErrorMessage()));
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        setLoading(false);
        mDataListener.onLoginError(e);
    }

    @Override
    public void onComplete(@NonNull Task task) {
        setLoading(false);
        if(!task.isSuccessful()) {
            mDataListener.onLoginError(task.getException());
        }
    }

    public void setAuthListener() {
        if(mAuthListener == null) {

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    mIsLoading = false;

                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (user != null) {
                        mDataListener.onLoginSuccess(user);
                    }
                }
            };
        }

        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    public void removeAuthListener() {
        if(mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    public void firebaseAuthWithCredential(AuthCredential token) {
        FirebaseAuth
                .getInstance()
                .signInWithCredential(token)
                .addOnFailureListener(this)
                .addOnCompleteListener(this);
    }

    public void signInGoogle(View v) {

        setLoading(true);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage(mActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mDataListener.onLogin(googleApiClient);
    }

    public void signInEmail(View v) {
        setEmail(true);

        if(mUsername != null && mPassword != null) {

            setLoading(true);

            FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(mUsername, mPassword)
                    .addOnFailureListener(this)
                    .addOnCompleteListener(this);

            mDataListener.onLogin(null);
        }
    }

    public void signUpEmail(View v) {
        FirebaseAuth
                .getInstance()
                .createUserWithEmailAndPassword(mUsername, mPassword)
                .addOnFailureListener(this)
                .addOnCompleteListener(this);

        mDataListener.onLogin(null);
    }

    public void resetLogin(View v) {
        setEmail(false);
        setLoading(false);
    }
}
