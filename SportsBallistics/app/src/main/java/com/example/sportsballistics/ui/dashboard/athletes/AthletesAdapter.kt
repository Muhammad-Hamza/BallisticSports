package com.example.sportsballistics.ui.dashboard.athletes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.appInterface.OnItemClickListener
import com.example.sportsballistics.data.local.AthletesModel
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.ListitemAthletesInfoBinding

class AthletesAdapter(var list: ArrayList<AthletesModel>, val mListener: OnItemClickListener) :
    ListAdapter<AthletesModel, AthletesAdapter.ViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<AthletesModel>() {
        override fun areItemsTheSame(
            oldItem: AthletesModel,
            newItem: AthletesModel
        ): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AthletesModel,
            newItem: AthletesModel
        ): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.average.equals(newItem.average) &&
                    oldItem.heading.equals(newItem.heading) &&
                    oldItem.percentage.equals(newItem.percentage) &&
                    oldItem.sum.equals(newItem.sum)
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
        holder.binding.tvHeading.setText(list.get(position).heading)
        holder.binding.tvPercentage.setText(list.get(position).percentage + " %")
        holder.binding.tvView.setOnClickListener {
            mListener.onViewClick(position, list.get(position))
        }
        val content = "AVERAGE: " + list.get(position).average + " SUM: " + list.get(position).sum
        holder.binding.tvDescription.setText(content)
    }
}