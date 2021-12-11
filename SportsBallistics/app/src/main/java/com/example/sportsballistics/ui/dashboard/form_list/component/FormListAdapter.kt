package com.example.sportsballistics.ui.dashboard.form_list.component

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.remote.athletes.Service
import com.example.sportsballistics.data.remote.club.UsersItem
import com.example.sportsballistics.databinding.ClubListItemBinding
import com.example.sportsballistics.databinding.ListitemFormInfoBinding
import com.example.sportsballistics.ui.dashboard.dashboard.ClubListAdapter

class FormListAdapter(
    val context: Context,
    val services: List<Service>,
    val listener: OnItemClickListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onEditClick(adapterType: Int, anyData: Any)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListitemFormInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.listitem_form_info, parent, false)
        return FormListViewHolder(binding)
    }

    inner class FormListViewHolder(val binding: ListitemFormInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int, service: Service) {
            binding.tvContent.setText(service.name)
            binding.tvEdit.setOnClickListener {
                listener.onEditClick(position, service)
            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as FormListViewHolder) {
            services.get(position).let { this.bindData(position, it) }
        }
    }

    override fun getItemCount(): Int {
        return services.size
    }
}