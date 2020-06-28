package com.far_sstrwnt.cinemania.ui.common

import com.far_sstrwnt.cinemania.model.Entity

interface EventActions {
    fun openDetail(entity: Entity, id: String)
}