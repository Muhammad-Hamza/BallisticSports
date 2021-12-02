package com.example.sportsballistics.data.remote.club

data class ClubResponse(val total: Int? = null, val start: Int? = null, val end: Int? = null, val content: Content? = null)

data class UsersItem(val fullname: String? = null,val role_name: String? = null, val id: String? = null,val name: String? = null)

data class Content(val headers: List<String?>? = null, val users: List<UsersItem?>? = null)

