package com.example.sportsballistics.ui.dashboard.clubs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R

class ClubListAdapter(
    val context: Context?, var cutPieceList: List<String?>?,
    val mListener: OnItemClickListener
                     ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    interface OnItemClickListener {
        fun onClick(cutPieceAdapter: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClubListViewHolder(
            LayoutInflater.from(context).inflate(R.layout.club_list_item, parent, false)
                                 )
    }


    fun setData(cutPieceList: MutableList<String?>?) {
        this.cutPieceList = cutPieceList
    }

    override fun getItemCount(): Int {
        return cutPieceList!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as ClubListViewHolder
        val category = cutPieceList?.get(position)


    }

    class ClubListViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    }
}
