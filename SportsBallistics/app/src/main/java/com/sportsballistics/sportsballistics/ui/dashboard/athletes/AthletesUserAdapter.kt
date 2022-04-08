package com.sportsballistics.sportsballistics.ui.dashboard.athletes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sportsballistics.sportsballistics.appInterface.OnItemClickListener
import com.sportsballistics.sportsballistics.data.remote.generic.UserModel
import com.sportsballistics.sportsballistics.databinding.*
import com.sportsballistics.sportsballistics.utils.AppConstant

class AthletesUserAdapter(val userRole: String, val mListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<UserModel>? = null
    fun loadData(list: ArrayList<UserModel>) {
        this.list = list;
        notifyDataSetChanged()
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
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    class ViewHolder(binding: AthletesListItemBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: AthletesListItemBinding

        init {
            this.binding = binding
        }
    }

    class ViewHolderClub(binding: AthletesListItemClubBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: AthletesListItemClubBinding

        init {
            this.binding = binding
        }
    }

    class ViewHolderTrainer(binding: AthletesListItemTrainerBinding) :
        RecyclerView.ViewHolder(binding.getRoot()) {
        var binding: AthletesListItemTrainerBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (userRole != null) {
            if (userRole.equals(AppConstant.ROLE_CLUB_PORTAL) || userRole.equals(AppConstant.ROLE_SUPER_PORTAL)) {
                return ViewHolderClub(
                    AthletesListItemClubBinding.inflate(LayoutInflater.from(parent.context))
                )
            } else if (userRole.equals(AppConstant.ROLE_TRAINER_PORTAL)) {
                return ViewHolderTrainer(
                    AthletesListItemTrainerBinding.inflate(LayoutInflater.from(parent.context))
                )
            } else {
                return ViewHolder(
                    AthletesListItemBinding.inflate(LayoutInflater.from(parent.context))
                )
            }
        }
        return ViewHolder(
            AthletesListItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    fun getItem(position: Int): UserModel {
        return list!!.get(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model: UserModel = getItem(position)
        if (holder is ViewHolder) {
            holder.binding.txtClubName.setText(model.fullname)
            holder.binding.txtSerialNo.setText((position + 1).toString())
            holder.binding.imgViewClub.setOnClickListener {
                mListener.onViewClick(position, model)
            }
            holder.binding.imgEdit.setOnClickListener {
                mListener.onEditClick(position, model)
            }
            holder.binding.txtAction.setOnClickListener {
                mListener.onDeleteClick(position, model.id)
            }
            AppConstant.changeColor(holder.binding.txtSerialNo)
            AppConstant.changeColor(holder.binding.txtClubName)
        } else if (holder is ViewHolderClub) {
            holder.binding.txtClubName.setText(model.fullname)
            holder.binding.txtSerialNo.setText((position + 1).toString())
            holder.binding.imgViewClub.setOnClickListener {
                mListener.onViewClick(position, model)
            }
            holder.binding.tvViewDashboard.setOnClickListener {
                mListener.onDashboardClick(position, model)
            }
            holder.binding.imgEdit.setOnClickListener {
                mListener.onEditClick(position, model)
            }
            holder.binding.txtAction.setOnClickListener {
                mListener.onDeleteClick(position, model.id)
            }
            AppConstant.changeColor(holder.binding.txtSerialNo)
            AppConstant.changeColor(holder.binding.txtClubName)
            AppConstant.changeColor(holder.binding.tvViewDashboard)
        } else if (holder is ViewHolderTrainer) {
            holder.binding.txtClubName.setText(model.fullname)
            holder.binding.txtSerialNo.setText((position + 1).toString())
            holder.binding.imgViewClub.setOnClickListener {
                mListener.onViewClick(position, model)
            }
            holder.binding.tvViewDashboard.setOnClickListener {
                mListener.onDashboardClick(position, model)
            }
            holder.binding.imgEdit.setOnClickListener {
                mListener.onEditClick(position, model)
            }
            holder.binding.txtAction.setOnClickListener {
                mListener.onDeleteClick(position, model.id)
            }
            AppConstant.changeColor(holder.binding.txtSerialNo)
            AppConstant.changeColor(holder.binding.txtClubName)
            AppConstant.changeColor(holder.binding.tvViewDashboard)
        }
    }

}