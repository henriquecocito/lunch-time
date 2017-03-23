package br.com.henriquecocito.lunchtime.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import br.com.henriquecocito.lunchtime.R;

/**
 * Created by HenriqueCocito on 02/03/17.
 */

public class UserViewModel extends BaseObservable {

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    public static boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public String getName() {
        return mUser.getDisplayName();
    }

    public String getEmail() {
        return mUser.getEmail();
    }

    public String getPictureURL() {
        if(mUser.getPhotoUrl() != null) {
            return mUser.getPhotoUrl().toString();
        }
        return null;
    }

    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(final ImageView v, String url) {
        Picasso.with(v.getContext())
                .load(url)
                .placeholder(R.drawable.ic_logo)
                .into(v, new Callback() {
                    @Override
                    public void onSuccess() {
                        v.setPadding(0, 0, 0, 0);
                    }

                    @Override
                    public void onError() {

                    }
                });
    }
}
