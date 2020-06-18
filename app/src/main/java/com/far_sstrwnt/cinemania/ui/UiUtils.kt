package com.far_sstrwnt.cinemania.ui

import android.view.View

fun toVisibility(constraint: Boolean): Int = if (constraint) {
    View.VISIBLE
} else {
    View.GONE
}