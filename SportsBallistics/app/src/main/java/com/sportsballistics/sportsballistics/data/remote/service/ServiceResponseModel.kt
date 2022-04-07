package com.sportsballistics.sportsballistics.data.remote.service

import com.google.gson.annotations.SerializedName

data class ServiceResponseModel(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("is_error")
	val isError: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class AthleticName(

	@field:SerializedName("image_name")
	val imageName: String? = null,

	@field:SerializedName("role_id")
	val roleId: String? = null,

	@field:SerializedName("grade")
	val grade: String? = null,

	@field:SerializedName("club_id")
	val clubId: String? = null,

	@field:SerializedName("fullname")
	val fullname: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("age")
	val age: String? = null
)

data class Data(

	@field:SerializedName("profile_image")
	val profileImage: String? = null,

	@field:SerializedName("clubname")
	val clubname: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("athletic_name")
	val athleticName: AthleticName? = null,

	@field:SerializedName("services")
	val services: List<ServicesItem?>? = null,

	@field:SerializedName("profile_image_thumb")
	val profileImageThumb: String? = null
)

data class ServicesItem(

	@field:SerializedName("average")
	val average: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("sum")
	val sum: String? = null,

	@field:SerializedName("percent")
	val percent: String? = null,

	@field:SerializedName("slug_edit")
	val slugEdit: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null
)
