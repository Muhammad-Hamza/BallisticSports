package com.example.sportsballistics.data.remote.generic

import com.example.sportsballistics.data.remote.club.UsersItem

data class MainContentModel(
    val headers: ArrayList<String?>,
    val users: ArrayList<UserModel>
)