package com.sportsballistics.sportsballistics.data.remote

import com.google.gson.annotations.SerializedName

data class AccountResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("is_error")
	val isError: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
