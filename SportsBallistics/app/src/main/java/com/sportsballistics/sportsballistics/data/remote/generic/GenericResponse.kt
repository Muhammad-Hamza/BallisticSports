package com.sportsballistics.sportsballistics.data.remote.generic

data class GenericResponse(
    val total: Int? = null,
    val start: Int? = null,
    val end: Int? = null,
    val content: MainContentModel? = null
)