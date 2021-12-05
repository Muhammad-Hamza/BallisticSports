package com.example.sportsballistics.ui.dashboard.club

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.ClubListItemBinding

class ClubListAdapter(
    val context: Context?,
    var users: List<UsersItem?>?,
    val mListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapterType: Int = -1
    lateinit var binding: ClubListItemBinding

    interface OnItemClickListener {
        fun onEditClick(adapterType: Int, user: UsersItem)
        fun onViewClick(adapterType: Int, user: UsersItem)
        fun onDeleteClick(adapterType: Int, user: UsersItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.club_list_item, parent, false)
        return ClubListViewHolder(binding)
    }

    fun setData(users: MutableList<UsersItem?>?) {
        this.users = users
    }

    override fun getItemCount(): Int {
        return if (users != null) return users!!.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ClubListViewHolder) {
            users?.get(position).let {
                holder.bindData(users!!.get(position), (position + 1))
            }
        }
    }

    inner class ClubListViewHolder(binding: ClubListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: UsersItem?, position: Int) {
            binding.txtClubName.text = user?.name
            binding.txtSerialNo.text = position.toString()

            binding.imgViewClub.setOnClickListener {
                mListener.onViewClick(adapterType, user!!)
            }
            binding.imgEdit.setOnClickListener {
                mListener.onEditClick(adapterType, user!!)
            }

            binding.txtAction.setOnClickListener {
                mListener.onDeleteClick(adapterType, user!!)
            }
        }

    }
}
