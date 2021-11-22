package com.example.sportsballistics.ui.dashboard.clubs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.ClubListItemBinding

class ClubListAdapter(val context: Context?, var users: List<UsersItem?>?, val mListener: OnItemClickListener) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    lateinit var binding: ClubListItemBinding

    interface OnItemClickListener
    {
        fun onClick(user: UsersItem)
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
        return 10
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
    {
//        val viewHolder = holder as ClubListViewHolder
//        val user = users?.get(position)
//        if (user != null)
//        {
//            viewHolder.bindData(binding,user)
//        }
    }

    class ClubListViewHolder(binding: ClubListItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bindData(binding: ClubListItemBinding,user: UsersItem){
            binding.txtClubName.text = user.name
            binding.txtSerialNo.text = user.id
        }
    }
}
