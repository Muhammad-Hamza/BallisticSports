package com.example.sportsballistics.data.remote.user


data class UserResponse(
	val total: Int? = null,
	val start: Int? = null,
	val end: Int? = null,
	val content: Content? = null
)

data class UsersItem(
	val roleName: String? = null,
	val id: String? = null,
	val fullname: String? = null
)

data class Content(
	val headers: List<String?>? = null,
	val users: List<UsersItem?>? = null
)
