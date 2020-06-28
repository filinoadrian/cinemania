package com.far_sstrwnt.cinemania.shared.data.mapper

import com.far_sstrwnt.cinemania.model.PeopleEntity
import com.far_sstrwnt.cinemania.shared.data.datasource.model.NetworkPeopleEntity

fun NetworkPeopleEntity.asDomainModel(): PeopleEntity {
    return PeopleEntity(
        id = id,
        popularity = popularity,
        knownForDepartment = knownForDepartment,
        birthday = birthday,
        deathday = deathday,
        name = name,
        gender = gender,
        biography = biography,
        placeOfBirth = placeOfBirth,
        profilePath = profilePath,
        adult = adult,
        imdbId = imdbId,
        homepage = homepage
    )
}