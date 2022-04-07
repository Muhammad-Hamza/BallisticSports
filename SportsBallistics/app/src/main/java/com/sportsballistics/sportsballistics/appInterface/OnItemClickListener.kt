package com.sportsballistics.sportsballistics.appInterface

interface OnItemClickListener {
    fun onEditClick(adapterType: Int, anyData: Any)
    fun onViewClick(adapterType: Int, anyData: Any)
    fun onDeleteClick(adapterType: Int,userId:String)
    fun onDashboardClick(adapterType: Int, anyData: Any)
}