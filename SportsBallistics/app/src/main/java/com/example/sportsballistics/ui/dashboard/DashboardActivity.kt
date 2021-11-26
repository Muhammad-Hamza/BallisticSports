package com.example.sportsballistics.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.sportsballistics.R
import com.example.sportsballistics.databinding.ActivityDashboardBinding
import com.example.sportsballistics.ui.dashboard.athletes.AthletesFragment
import com.example.sportsballistics.ui.dashboard.clubs.ClubFragment
import com.example.sportsballistics.ui.dashboard.clubs.ViewPagerAdapter
import com.example.sportsballistics.ui.dashboard.my_account.AccountFragment
import com.example.sportsballistics.ui.dashboard.trainer.TrainerFragment
import com.example.sportsballistics.ui.dashboard.users.UserFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener

class DashboardActivity : AppCompatActivity()
{
    lateinit var binding : ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        setCurrentFragment(ClubFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.club->setCurrentFragment(ClubFragment())
                R.id.trainer->setCurrentFragment(TrainerFragment())
                R.id.athlete->setCurrentFragment(AthletesFragment())
                R.id.users->setCurrentFragment(UserFragment())
                R.id.account->setCurrentFragment(AccountFragment())

            }
            true
        }
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}