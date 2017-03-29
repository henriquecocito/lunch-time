package br.com.henriquecocito.lunchtime.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import br.com.henriquecocito.lunchtime.R;
import br.com.henriquecocito.lunchtime.databinding.ActivitySettingsBinding;

/**
 * Created by HenriqueCocito on 01/03/17.
 */

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = DataBindingUtil.setContentView(SettingsActivity.this, R.layout.activity_settings);

        setSupportActionBar(mView.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}