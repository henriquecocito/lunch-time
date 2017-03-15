package br.com.henriquecocito.lunchtime.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

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
        return mUser.getPhotoUrl().toString();
    }

    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(ImageView v, String url) {
        Picasso.with(v.getContext().getApplicationContext())
                .load(url)
                .into(v);
    }
}
