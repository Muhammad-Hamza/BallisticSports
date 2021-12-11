package com.example.sportsballistics.ui.dashboard.create_club.component

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.local.StateModel

class StateAdapter(val list: List<StateModel>, context: Context) :
    ArrayAdapter<StateModel>(context, R.layout.listitem_dropdown) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val model: StateModel = list.get(position)
        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.listitem_dropdown, parent, false)
        }

        try {
            (view!!.findViewById<View>(R.id.tvContent) as TextView).setText(model.name)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view!!

    }

    override fun getItem(position: Int): StateModel? {
        return list.get(position)
    }

    override fun getCount(): Int {
        return list.size
    }

}