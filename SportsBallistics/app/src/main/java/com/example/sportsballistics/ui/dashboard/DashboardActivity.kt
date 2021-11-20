package com.example.sportsballistics.ui.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.sportsballistics.R
import com.example.sportsballistics.databinding.ActivityDashboardBinding
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
        setupTab()
    }

    private fun setupTab()
    {
        binding.tabsLayout.addTab(binding.tabsLayout.newTab())
        binding.tabsLayout.addTab(binding.tabsLayout.newTab())
        binding.tabsLayout.addTab(binding.tabsLayout.newTab())
        binding.tabsLayout.addTab(binding.tabsLayout.newTab())
        binding.tabsLayout.addTab(binding.tabsLayout.newTab())
        val adapter = ViewPagerAdapter(this, supportFragmentManager, binding.tabsLayout.tabCount)
        binding.viewPager.adapter = adapter
        binding.tabsLayout.getTabAt(0)?.setCustomView(R.layout.club_tab)
        binding.tabsLayout.getTabAt(1)?.setCustomView(R.layout.trainer_tab)
        binding.tabsLayout.getTabAt(2)?.setCustomView(R.layout.athletes_tab)
        binding.tabsLayout.getTabAt(3)?.setCustomView(R.layout.user_tab)
        binding.tabsLayout.getTabAt(4)?.setCustomView(R.layout.my_account)
        binding.tabsLayout.setSelectedTabIndicatorHeight(0)
        binding.viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding.tabsLayout))
        binding.tabsLayout.addOnTabSelectedListener(object :
                OnTabSelectedListener
        {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
            }

            override fun onTabUnselected(tab: TabLayout.Tab)
            {
            }

            override fun onTabReselected(tab: TabLayout.Tab)
            {
            }
        })
    }

}