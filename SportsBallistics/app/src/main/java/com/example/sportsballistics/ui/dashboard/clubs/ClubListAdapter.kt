package com.example.sportsballistics.ui.dashboard.clubs

import android.content.Context
import android.content.SyncAdapterType
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.ClubListItemBinding
import java.lang.String

class ClubListAdapter(val context: Context?, var users: List<UsersItem?>?, val mListener: OnItemClickListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    var adapterType : Int = -1
    lateinit var binding: ClubListItemBinding

    interface OnItemClickListener
    {
        fun onEditClick(adapterType:Int,user: UsersItem)
        fun onViewClick(adapterType:Int,user: UsersItem)
        fun onDeleteClick(adapterType:Int,user: UsersItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.club_list_item, parent, false)
        return ClubListViewHolder(binding)
    }

    fun setData(users: MutableList<UsersItem?>?)
    {
        this.users = users
    }

    override fun getItemCount(): Int
    {
        return if(users != null) return users!!.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
        with(holder as ClubListViewHolder){
            users?.get(position)?.let { this.bindData(it) }
            this.bindData(null)
        }
    }

    inner class ClubListViewHolder(binding: ClubListItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bindData(user: UsersItem?){
            binding.txtClubName.text = user?.name
            binding.txtSerialNo.text = user?.id

            binding.imgViewClub.setOnClickListener{
                mListener.onViewClick(adapterType,UsersItem("",""))
            }
        }

    }
}
