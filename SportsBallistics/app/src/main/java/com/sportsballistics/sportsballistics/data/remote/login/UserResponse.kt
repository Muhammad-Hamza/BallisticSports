package com.sportsballistics.sportsballistics.data.remote.login

import com.google.gson.annotations.SerializedName


data class UserResponse(

	@field:SerializedName("logged_in")
	val loggedIn: LoggedIn? = null,

	@field:SerializedName("athlete_count")
	val athleteCount: Int? = null,

	@field:SerializedName("is_error")
	val isError: Boolean? = null,

	@field:SerializedName("ca_count")
	val caCount: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("trainer_count")
	val trainerCount: Int? = null
)

data class LoggedIn(

	@field:SerializedName("profile_image")
	val profileImage: String? = null,

	@field:SerializedName("role_id")
	val roleId: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("club_id")
	val clubId: Any? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("profile_image_thumb")
	val profileImageThumb: String? = null
)
