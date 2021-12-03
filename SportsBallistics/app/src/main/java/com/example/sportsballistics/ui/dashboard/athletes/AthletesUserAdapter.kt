package com.example.sportsballistics.ui.dashboard.athletes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.appInterface.OnItemClickListener
import com.example.sportsballistics.data.local.AthletesModel
import com.example.sportsballistics.data.local.LookupModel
import com.example.sportsballistics.data.remote.generic.UserModel
import com.example.sportsballistics.databinding.AthletesListItemBinding
import com.example.sportsballistics.databinding.ClubListItemBinding
import com.example.sportsballistics.databinding.ListitemAthletesInfoBinding

class AthletesUserAdapter(val mListener: OnItemClickListener) :
    ListAdapter<UserModel, AthletesUserAdapter.ViewHolder>(DiffCallback()) {

    fun loadData(list: ArrayList<UserModel>) {
        submitList(list)
    }

    private class DiffCallback : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(
            oldItem: UserModel,
            newItem: UserModel
        ): Boolean {
            return oldItem.id.equals(newItem.id)
        }

        override fun areContentsTheSame(
            oldItem: UserModel,
            newItem: UserModel
        ): Boolean {
            return oldItem.id.equals(newItem.id) &&
                    oldItem.fullname.equals(newItem.fullname)
        }
    }

    class ViewHolder(binding: AthletesListItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: AthletesListItemBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AthletesListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: UserModel = getItem(position)
        holder.binding.txtClubName.setText(model.fullname)

        holder.binding.txtSerialNo.setText((position + 1).toString())
        holder.binding.imgViewClub.setOnClickListener {
            mListener.onViewClick(position, model)
        }
        holder.binding.imgEdit.setOnClickListener {
            mListener.onEditClick(position, model)
        }
        holder.binding.txtAction.setOnClickListener {
            mListener.onDeleteClick(position, model)
        }
    }

}