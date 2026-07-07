package com.example.degreewiki.data.mapper

import com.example.degreewiki.data.local.entity.UniversityEntity
import com.example.degreewiki.data.network.dto.UniversityDto
import com.example.degreewiki.domain.model.University

fun UniversityDto.toEntity(): UniversityEntity {
    return UniversityEntity(
        id = id,
        slug = slug,
        name = name,
        countryId = countryId,
        city = city,
        logoUrl = logoUrl,
        overview = overview,
        offlineSavedAt = System.currentTimeMillis()
    )
}

fun UniversityEntity.toDomain(): University {
    return University(
        id = id,
        slug = slug,
        name = name,
        countryId = countryId,
        city = city,
        logoUrl = logoUrl,
        overview = overview
    )
}
