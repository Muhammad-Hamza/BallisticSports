package com.example.sportsballistics.data.remote

import com.google.gson.annotations.SerializedName

data class ViewClubResponse(

	@field:SerializedName("club_data")
	val clubData: ClubData? = null,

	@field:SerializedName("is_error")
	val isError: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ClubData(

	@field:SerializedName("zipcode")
	val zipcode: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("updated_by")
	val updatedBy: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("state")
	val state: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("created_by")
	val createdBy: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
