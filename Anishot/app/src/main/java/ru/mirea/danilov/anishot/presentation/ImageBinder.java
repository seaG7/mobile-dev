package ru.mirea.danilov.anishot.presentation;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ru.mirea.danilov.anishot.R;

public final class ImageBinder {
    private ImageBinder() {
    }

    public static void load(ImageView view, String url) {
        Glide.with(view)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.bg_poster_placeholder)
                .error(R.drawable.bg_poster_placeholder)
                .into(view);
    }
}
