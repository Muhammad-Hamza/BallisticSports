package com.sportsballistics.sportsballistics.data.remote

import com.sportsballistics.sportsballistics.data.remote.athletes.AthleteDataModel

data class DashboardModel(
    val is_error: Boolean,
    val message: String,
    val data: AthleteDataModel
)