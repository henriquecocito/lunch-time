package br.com.henriquecocito.lunchtime.view.activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.adapter.ViewPagerAdapter;
import br.com.henriquecocito.lunchtime.databinding.ActivityMainBinding;
import br.com.henriquecocito.lunchtime.databinding.ContentMainBinding;
import br.com.henriquecocito.lunchtime.databinding.NavHeaderMainBinding;
import br.com.henriquecocito.lunchtime.utils.BaseFragment;
import br.com.henriquecocito.lunchtime.view.fragments.MapFragment;
import br.com.henriquecocito.lunchtime.view.fragments.PlacesFragment;
import br.com.henriquecocito.lunchtime.viewmodel.UserViewModel;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding mView;
    ContentMainBinding mContentBinding;
    ArrayList<BaseFragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupNavigationDrawer(mView.drawerLayout, toolbar);

        mFragments = new ArrayList<>();
        mFragments.add(new MapFragment());
        mFragments.add(new PlacesFragment());

        mContentBinding = DataBindingUtil.bind(mView.drawerLayout.findViewById(R.id.rootView));
        setupPager(mContentBinding.pager, mContentBinding.tabbar);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        MapFragment mapFragment = (MapFragment) mFragments.get(0);
        mapFragment.setupGoogleMaps();
    }

    @Override
    public void onBackPressed() {
        if (mView.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mView.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_restaurants:
                intent = new Intent(MainActivity.this, RankingActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        mView.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MapFragment.REQUEST_PERMISSIONS:
                if(grantResults.length < 1 || grantResults[0] < 0 || grantResults[1] < 0) {
                    Snackbar
                            .make(mView.getRoot(), R.string.error_permission_location, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Add permissions", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    Intent settingsIntent = new Intent();
                                    settingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    settingsIntent.addCategory(Intent.CATEGORY_DEFAULT);
                                    settingsIntent.setData(Uri.parse("package:" + getPackageName()));
                                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    startActivity(settingsIntent);
                                }
                            })
                            .show();
                }
                break;
            default:
                break;
        }
    }

    private void setupNavigationDrawer(DrawerLayout drawerLayout, Toolbar toolbar) {
        NavHeaderMainBinding headerBinding = DataBindingUtil.bind(mView.navView.getHeaderView(0));
        headerBinding.setUserViewModel(new UserViewModel());

        mView.navView.setNavigationItemSelectedListener(this);
        mView.navView.setCheckedItem(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupPager(ViewPager viewPager, TabLayout tabLayout) {
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setupWithViewPager(viewPager);
    }
}
