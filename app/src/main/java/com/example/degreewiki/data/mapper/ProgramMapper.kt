package com.example.degreewiki.data.mapper

import com.example.degreewiki.data.local.entity.ProgramEntity
import com.example.degreewiki.data.network.dto.ProgramDto
import com.example.degreewiki.domain.model.Program

fun ProgramDto.toEntity(): ProgramEntity {
    return ProgramEntity(
        id = id,
        slug = slug,
        title = title,
        universityName = universityName,
        countryName = countryName,
        degreeLevel = degreeLevel,
        subject = subject,
        tuition = tuition,
        duration = duration,
        offlineSavedAt = System.currentTimeMillis()
    )
}

fun ProgramEntity.toDomain(): Program {
    return Program(
        id = id,
        slug = slug,
        title = title,
        universityName = universityName,
        countryName = countryName,
        degreeLevel = degreeLevel,
        subject = subject,
        tuition = tuition,
        duration = duration
    )
}
