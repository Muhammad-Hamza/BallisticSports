package com.sportsballistics.sportsballistics.ui.dashboard.users

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.remote.club.UsersItem
import com.sportsballistics.sportsballistics.databinding.UserListItemBinding
import com.sportsballistics.sportsballistics.utils.AppConstant

class UserAdapter(
    val context: Context?,
    var users: List<UsersItem?>?,
    val mListener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var adapterType: Int = -1
    lateinit var binding: UserListItemBinding

    interface OnItemClickListener {
        fun onEditClick(adapterType: Int, user: UsersItem)
        fun onViewClick(adapterType: Int, user: UsersItem)
        fun onDeleteClick(adapterType: Int, user: UsersItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.user_list_item, parent, false)
        return UserListViewHolder(binding)
    }

    fun setData(users: MutableList<UsersItem?>?) {
        this.users = users
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return if (users != null) return users!!.size else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is UserListViewHolder) {
            users?.get(position).let {
                holder.bindData(users!!.get(position), (position + 1))
            }
        }
    }

    inner class UserListViewHolder(binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(user: UsersItem?, position: Int) {
            binding.txtClubName.text = user?.fullname
            binding.txtSerialNo.text = position.toString()
            binding.txtRoleType.text = user?.role_name

            binding.imgViewClub.setOnClickListener {
                mListener.onViewClick(adapterType, user!!)
            }
            binding.imgEdit.setOnClickListener {
                mListener.onEditClick(adapterType, user!!)
            }

            binding.txtAction.setOnClickListener {
                mListener.onDeleteClick(adapterType, user!!)
            }
            AppConstant.changeColor(binding.txtSerialNo)
            AppConstant.changeColor(binding.txtClubName)
            AppConstant.changeColor(binding.txtRoleType)
        }
    }
}
