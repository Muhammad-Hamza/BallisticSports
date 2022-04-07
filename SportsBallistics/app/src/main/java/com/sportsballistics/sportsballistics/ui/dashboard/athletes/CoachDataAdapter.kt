package com.sportsballistics.sportsballistics.ui.dashboard.athletes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sportsballistics.sportsballistics.data.remote.athletes.AthleteDataModel
import com.sportsballistics.sportsballistics.databinding.ListitemLookupBinding

class CoachDataAdapter(val model: AthleteDataModel, adapterPosition: Int) :
        ListAdapter<String, CoachDataAdapter.ViewHolder>(DiffCallback())
{
    var parentPosition: Int

    init
    {
        parentPosition = adapterPosition
    }

    private class DiffCallback : DiffUtil.ItemCallback<String>()
    {
        override fun areItemsTheSame(
                oldItem: String,
                newItem: String
                                    ): Boolean
        {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(
                oldItem: String,
                newItem: String
                                       ): Boolean
        {
            return oldItem.equals(newItem)
        }
    }

    class ViewHolder(binding: ListitemLookupBinding) :
            RecyclerView.ViewHolder(binding.getRoot())
    {
        var binding: ListitemLookupBinding

        init
        {
            this.binding = binding
        }
    }

    override fun getItemCount(): Int
    {
        return model.nameArr.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        return ViewHolder(
                ListitemLookupBinding.inflate(LayoutInflater.from(parent.context))
                         )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        if (position == (model.nameArr.size - 1))
        {
            holder.binding.tvSeperator.visibility = View.GONE
        }
        else
        {
            holder.binding.tvSeperator.visibility = View.VISIBLE
        }
        if (model.nameArr != null)
            holder.binding.tvName.setText(model.nameArr.get(position))

        if (model.valueArr != null)
            holder.binding.tvValue.setText(model.valueArr.get(position))
    }
}