package com.sportsballistics.sportsballistics.data.remote

import com.google.gson.annotations.SerializedName

data class AthleteResponse(

	@field:SerializedName("is_error")
	val isError: Boolean? = null,

	@field:SerializedName("user_data")
	val userData: UserData? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class UserData(

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("gender")
	val gender: Any? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("package_type")
	val packageType: Any? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("profile_image_thumb")
	val profileImageThumb: String? = null,

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("image_name")
	val imageName: String? = null,

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("profile_image")
	val profileImage: String? = null,

	@field:SerializedName("passport")
	val passport: Any? = null,

	@field:SerializedName("role_id")
	val roleId: String? = null,

	@field:SerializedName("contact_no")
	val contactNo: String? = null,

	@field:SerializedName("club_id")
	val clubId: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
