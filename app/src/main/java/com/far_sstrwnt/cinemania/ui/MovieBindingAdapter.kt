package com.far_sstrwnt.cinemania.ui

import android.widget.ImageView
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