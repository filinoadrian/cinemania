package com.far_sstrwnt.cinemania.ui.common

import com.far_sstrwnt.cinemania.model.VideoEntity

interface VideoActionsHandler {

    fun playVideo(video: VideoEntity)
}