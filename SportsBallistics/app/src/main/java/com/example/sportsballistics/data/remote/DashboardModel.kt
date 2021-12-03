package com.example.sportsballistics.data.remote

import com.example.sportsballistics.data.remote.athletes.AthleteDataModel

data class DashboardModel(
    val is_error: Boolean,
    val message: String,
    val data: AthleteDataModel
)