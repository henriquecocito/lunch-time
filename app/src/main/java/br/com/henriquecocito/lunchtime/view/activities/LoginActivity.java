package br.com.henriquecocito.lunchtime.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;
import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.databinding.ActivityLoginBinding;
import br.com.henriquecocito.lunchtime.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements LoginViewModel.LoginListener{

    private ActivityLoginBinding mView;
    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginViewModel = new LoginViewModel(this, this);

        mView = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mView.setLoginViewModel(mLoginViewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        mLoginViewModel.setAuthListener();
    }

    @Override
    public void onStop() {
        super.onStop();
        mLoginViewModel.removeAuthListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case LoginViewModel.LOGIN_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    AuthCredential token = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    mLoginViewModel.firebaseAuthWithCredential(token);
                } else {
                    mLoginViewModel.resetLogin(null);
                    onLoginError(new Throwable(getString(R.string.error_authentication)));
                }
                break;
            case LoginViewModel.LOGIN_FACEBOOK:
                CallbackManager callbackManager = LoginViewModel.getFacebookCallbackManager();
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onLogin(Object object, int provider) {
        switch (provider) {
            case LoginViewModel.LOGIN_GOOGLE:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent((GoogleApiClient) object);
                startActivityForResult(signInIntent, LoginViewModel.LOGIN_GOOGLE);
                break;
            case LoginViewModel.LOGIN_FACEBOOK:

                LoginManager.getInstance().registerCallback((CallbackManager) object, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AuthCredential token = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                        mLoginViewModel.firebaseAuthWithCredential(token);
                    }

                    @Override
                    public void onCancel() {
                        onLoginError(new Throwable(getString(R.string.error_authentication)));
                    }

                    @Override
                    public void onError(FacebookException error) {
                        onLoginError(error);
                    }
                });
                break;
        }
    }

    @Override
    public void onLoginSuccess(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoginError(Throwable error) {
        Snackbar.make(mView.rootView, error.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
    }
}