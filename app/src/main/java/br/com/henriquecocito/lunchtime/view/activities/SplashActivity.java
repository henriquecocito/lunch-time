package br.com.henriquecocito.lunchtime.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.databinding.ActivitySplashBinding;
import br.com.henriquecocito.lunchtime.viewmodel.UserViewModel;

/**
 * Created by hrcocito on 14/03/17.
 */

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = DataBindingUtil.setContentView(SplashActivity.this, R.layout.activity_splash);

//        if(UserViewModel.isLoggedIn()) {
//            new Handler().postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                    finish();
//                }
//            }, 1000);
//            return;
//        }
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, mView.ivLogo, "profile");
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class), options.toBundle());
//                finish();
//            }
//        }, 2500);
    }
}
