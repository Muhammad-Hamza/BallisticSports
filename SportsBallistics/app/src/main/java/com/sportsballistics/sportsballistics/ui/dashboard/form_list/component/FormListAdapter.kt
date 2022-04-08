package com.sportsballistics.sportsballistics.ui.dashboard.form_list.component

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.remote.service.ServicesItem
import com.sportsballistics.sportsballistics.databinding.ListitemFormInfoBinding
import com.sportsballistics.sportsballistics.utils.AppConstant

class FormListAdapter(
    val context: Context,
    val services: List<ServicesItem?>?,
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
        fun bindData(position: Int, service: ServicesItem?) {
            binding.tvContent.setText(service?.name)
            AppConstant.changeColor(binding.tvContent)
            AppConstant.changeColor(binding.tvEdit)
            binding.tvEdit.setOnClickListener {
                if (service != null)
                {
                    listener.onEditClick(position, service)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as FormListViewHolder) {
            services?.get(position).let { this.bindData(position, it) }
        }
    }

    override fun getItemCount(): Int {
        return services?.size
                ?: 0
    }
}