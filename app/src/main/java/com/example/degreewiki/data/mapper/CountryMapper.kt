package com.example.degreewiki.data.mapper

import com.example.degreewiki.data.local.entity.CountryEntity
import com.example.degreewiki.data.network.dto.CountryDto
import com.example.degreewiki.domain.model.Country

fun CountryDto.toEntity(): CountryEntity {
    return CountryEntity(
        id = id,
        slug = slug,
        name = name,
        summary = summary,
        imageUrl = imageUrl,
        offlineSavedAt = System.currentTimeMillis()
    )
}

fun CountryEntity.toDomain(): Country {
    return Country(
        id = id,
        slug = slug,
        name = name,
        summary = summary,
        imageUrl = imageUrl
    )
}
