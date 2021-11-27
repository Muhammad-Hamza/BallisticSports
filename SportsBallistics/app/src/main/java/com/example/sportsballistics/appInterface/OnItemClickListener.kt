package com.example.sportsballistics.appInterface

import com.example.sportsballistics.data.remote.club.UsersItem

interface OnItemClickListener {
    fun onEditClick(adapterType: Int, anyData: Any)
    fun onViewClick(adapterType: Int, anyData: Any)
    fun onDeleteClick(adapterType: Int, anyData: Any)
}