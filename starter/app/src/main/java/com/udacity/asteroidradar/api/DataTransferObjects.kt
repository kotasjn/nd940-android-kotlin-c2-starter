package com.udacity.asteroidradar.api

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.domain.PictureOfDay

/**
 * Daily picture that NASA chooses
 */
@JsonClass(generateAdapter = true)
data class PictureOfDayDto(
    val media_type: String,
    val title: String,
    val url: String
)

/**
 * Convert DTO to domain model
 */
fun PictureOfDayDto.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        mediaType = media_type,
        title = title,
        url = url
    )
}

