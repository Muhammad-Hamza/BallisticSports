package com.example.sportsballistics.ui.dashboard.athletes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.data.local.AthletesModel
import com.example.sportsballistics.data.local.LookupModel
import com.example.sportsballistics.data.remote.athletes.Service
import com.example.sportsballistics.databinding.ListitemAthletesInfoBinding
import com.example.sportsballistics.databinding.ListitemLookupBinding

class CoachDataAdapter(val list: List<Service>) :
    ListAdapter<Service, CoachDataAdapter.ViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<Service>() {
        override fun areItemsTheSame(
            oldItem: Service,
            newItem: Service
        ): Boolean {
            return oldItem.average == newItem.average &&
                    oldItem.percent == newItem.percent &&
                    oldItem.sum == newItem.sum &&
                    oldItem.name.equals(newItem.name)
        }

        override fun areContentsTheSame(
            oldItem: Service,
            newItem: Service
        ): Boolean {
            return oldItem.average == newItem.average &&
                    oldItem.percent == newItem.percent &&
                    oldItem.sum == newItem.sum &&
                    oldItem.name.equals(newItem.name)
        }
    }


    class ViewHolder(binding: ListitemLookupBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: ListitemLookupBinding

        init {
            this.binding = binding
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListitemLookupBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == (list.size - 1)) {
            holder.binding.tvSeperator.visibility = View.GONE
        } else {
            holder.binding.tvSeperator.visibility = View.VISIBLE
        }
        holder.binding.tvName.setText(list.get(position).name)
        holder.binding.tvValue.setText(list.get(position).average)
    }
}