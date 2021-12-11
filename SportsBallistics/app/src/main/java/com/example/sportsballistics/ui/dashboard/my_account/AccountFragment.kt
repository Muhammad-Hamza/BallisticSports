package com.example.sportsballistics.ui.dashboard.my_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation
import com.example.sportsballistics.R


class AccountFragment : Fragment()
{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_account, container, false)

        view.findViewById<AppCompatImageView>(R.id.backClubList).setOnClickListener {
            Navigation.findNavController(view).navigateUp()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}