package com.example.sportsballistics.ui.dashboard.create_athlete_form.component

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsballistics.R
import com.example.sportsballistics.data.local.AthleteFormLocalModel
import com.example.sportsballistics.databinding.ListitemEditFormBinding

class AthleteFormAdapter(val context: Context, val list: List<AthleteFormLocalModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    public val paramMap = HashMap<String,String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListitemEditFormBinding =
            DataBindingUtil.inflate(inflater, R.layout.listitem_edit_form, parent, false)
        return EditFormViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as EditFormViewHolder) {
            list.get(position).let { this.bindData(position, it) }
        }
    }

    inner class EditFormViewHolder(val binding: ListitemEditFormBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(position: Int, atheletFormModel: AthleteFormLocalModel) {
            binding.tvTitle.setText(atheletFormModel.heading)
            if (!TextUtils.isEmpty(atheletFormModel.placeHolder)) {
                binding.etEditInfo.setText(atheletFormModel.placeHolder)
            }
            binding.etEditInfo.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    paramMap[list[position].heading!!] = s.toString()
                    list.get(position).data = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                }

            })
        }
    }

    fun allFieldsEdit(): Boolean {
        for (i in 0..(list.size - 1)) {
            if (TextUtils.isEmpty(list.get(i).data)) {
                showMessage("${list.get(i).heading} is required")
                return false;
            }
        }
        return true
    }

    private fun showMessage(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}