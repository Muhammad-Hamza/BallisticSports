package com.example.sportsballistics.ui.dashboard.athletes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.appInterface.OnItemClickListener
import com.example.sportsballistics.data.local.AthletesModel
import com.example.sportsballistics.data.remote.athletes.Service
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.ListitemAthletesInfoBinding

class AthletesAdapter(var list: List<Service>, val mListener: OnItemClickListener) :
    ListAdapter<Service, AthletesAdapter.ViewHolder>(DiffCallback()) {

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


    class ViewHolder(binding: ListitemAthletesInfoBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: ListitemAthletesInfoBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListitemAthletesInfoBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvHeading.setText(list.get(position).name)
        holder.binding.tvPercentage.setText(list.get(position).percent.toString() + " %")
        holder.binding.tvView.setOnClickListener {
            mListener.onViewClick(position, list.get(position))
        }
        val content = "AVERAGE: " + list.get(position).average + " SUM: " + list.get(position).sum
        holder.binding.tvDescription.setText(content)
    }
}