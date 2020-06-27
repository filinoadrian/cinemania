package com.far_sstrwnt.cinemania.ui

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("app:poster")
fun setPoster(view: ImageView, posterUrl: String?) {
    if (!posterUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load("https://image.tmdb.org/t/p/w342${posterUrl}")
            .into(view)
    }
}

@BindingAdapter("app:backdrop")
fun setBackdrop(view: ImageView, backdropUrl: String?) {
    if (!backdropUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load("https://image.tmdb.org/t/p/w780${backdropUrl}")
            .into(view)
    }
}

@BindingAdapter("app:rating")
fun setRating(ratingBar: AppCompatRatingBar, rating: Float?) {
    if (rating != null) {
        ratingBar.rating = rating / 2
    } else {
        ratingBar.rating = 0f
    }
}