package com.example.sportsballistics.ui.dashboard

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sportsballistics.ui.dashboard.athletes.AthletesFragment
import com.example.sportsballistics.ui.dashboard.clubs.ClubFragment
import com.example.sportsballistics.ui.dashboard.my_account.AccountFragment
import com.example.sportsballistics.ui.dashboard.trainer.TrainerFragment
import com.example.sportsballistics.ui.dashboard.users.UserFragment

class ViewPagerAdapter(private val myContext: Context, fm: FragmentManager?, var totalTabs: Int) :
        FragmentPagerAdapter(fm!!)
{
    // this is for fragment tabs
    override fun getItem(position: Int): Fragment
    {
        return when (position)
        {
            0 -> ClubFragment()
            1 -> AthletesFragment()
            2 -> TrainerFragment()
            3 -> UserFragment()
            4 -> AccountFragment()
            else -> ClubFragment()
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int
    {
        return totalTabs
    }
}