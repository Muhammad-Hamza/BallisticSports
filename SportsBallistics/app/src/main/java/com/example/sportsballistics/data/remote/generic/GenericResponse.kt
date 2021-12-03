package com.example.sportsballistics.data.remote.generic

import com.example.sportsballistics.data.remote.club.Content

data class GenericResponse(
    val total: Int? = null,
    val start: Int? = null,
    val end: Int? = null,
    val content: MainContentModel? = null
)