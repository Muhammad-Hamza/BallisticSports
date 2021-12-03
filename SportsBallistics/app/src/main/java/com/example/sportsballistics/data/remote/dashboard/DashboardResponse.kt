package com.example.sportsballistics.data.remote.dashboard

import com.google.gson.annotations.SerializedName

data class DashboardResponse(

	@field:SerializedName("logged_in")
	val loggedIn: LoggedIn? = null,

	@field:SerializedName("is_error")
	val isError: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LoggedIn(

	@field:SerializedName("athlete_count")
	val athleteCount: Int? = null,

	@field:SerializedName("ca_count")
	val caCount: Int? = null,

	@field:SerializedName("trainer_count")
	val trainerCount: Int? = null
)
