package com.example.sportsballistics.data.remote.service

import com.example.sportsballistics.data.remote.athletes.AthleteDataModel
import com.google.gson.annotations.SerializedName

data class ServiceResponseModel(
    @field:SerializedName("is_error")
    val isError: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: AthleteDataModel? = null
)