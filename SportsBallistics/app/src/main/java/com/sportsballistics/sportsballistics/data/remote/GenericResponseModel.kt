package com.sportsballistics.sportsballistics.data.remote

import com.google.gson.annotations.SerializedName

data class GenericResponseModel(
    @field:SerializedName("is_error")
    val isError: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)