package br.com.henriquecocito.lunchtime.utils;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import br.com.henriquecocito.lunchtime.LunchTimeApplication;

/**
 * Created by hrcocito on 03/04/17.
 */

public class Utils {

    @BindingAdapter("app:fontFamily")
    public static void setFont(TextView textView, String fontFamily) {
        Typeface custom_font = Typeface.createFromAsset(LunchTimeApplication.CONTEXT.getAssets(),  String.format("fonts/%s.ttf", fontFamily));
        textView.setTypeface(custom_font);
    }

    @BindingAdapter({"app:url", "app:placeholder"})
    public static void loadImage(ImageView view, String url, Drawable error) {
        if(!url.isEmpty()) {
            Picasso.with(view.getContext())
                    .load(url)
                    .error(error)
                    .into(view);
        } else {
            view.setImageDrawable(error);
        }
    }
}
