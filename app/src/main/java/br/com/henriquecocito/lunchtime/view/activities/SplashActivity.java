package br.com.henriquecocito.lunchtime.view.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

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

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (UserViewModel.isLoggedIn()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this, mView.ivLogo, "logo");
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class), options.toBundle());
                }
//                finish();
//                overridePendingTransition(0, 0);
            }
        }, 2000);


//        setExitSharedElementCallback(new SharedElementCallback() {
//
//            @Override
//            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
//                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
//
//            }
//        });
    }
}
