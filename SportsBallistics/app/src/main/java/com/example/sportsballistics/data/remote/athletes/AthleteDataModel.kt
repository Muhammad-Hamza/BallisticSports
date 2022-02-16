package com.example.sportsballistics.data.remote.athletes

data class AthleteDataModel(
    val athletic_name: AthleticName,
    val profile_image: String,
    val profile_image_thumb: String,
    val clubname: String,
    val user_id: String,
    val services: List<Service>,
    val result: List<String>,
    val nameArr: List<String>,
    val valueArr: List<String>,
    val percentage: Double,
    val average: Double,
    val sum: Double,
    val title: String,
)

data class Service(
    val name: String,
    val average: Double,
    val sum: Double,
    val percent: Double,
    val slug: String,
    val slug_edit: String,
)

data class AthleticName(
    val fullname: String,
    val age: String,
    val grade: String,
    val id: String,
    val role_id: String,
    val created_by: String,
    val club_id: String
)

