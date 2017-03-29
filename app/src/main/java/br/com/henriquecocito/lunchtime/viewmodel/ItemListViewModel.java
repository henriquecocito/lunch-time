package br.com.henriquecocito.lunchtime.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by hrcocito on 29/03/17.
 */

public class ItemListViewModel extends BaseObservable {

    private String mTitle;

    public ItemListViewModel(String title) {
        this.mTitle = title;
    }

    @Bindable
    public String getTitle() {
        return this.mTitle;
    }

    @BindingAdapter({"app:url", "app:placeholder"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        Picasso.with(view.getContext())
                .load(url)
                .error(error)
                .into(view);
    }
}
