package com.example.sportsballistics.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.databinding.ActivityDashboardBinding
import com.example.sportsballistics.ui.dashboard.athletes.AthletesViewModel.Companion.TAG
import com.example.sportsballistics.ui.login.LoginActivity
import com.example.sportsballistics.utils.AppConstant
import com.example.sportsballistics.utils.launchActivityFinish


class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.bottomNavigationView.setItemIconTintList(null);

        if (AppSystem.getInstance().getCurrentUser() != null && AppSystem.getInstance()
                .getCurrentUser()!!.loggedIn != null
        ) {
            when (AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId) {
                AppConstant.ROLE_SUPER_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_admin_menu)
                }
                AppConstant.ROLE_ATHLETES_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_athletes_menu)
                }
                AppConstant.ROLE_CLUB_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_club_menu)
                }
                AppConstant.ROLE_TRAINER_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()
                    binding.bottomNavigationView.inflateMenu(R.menu.bottom_trainer_menu)
                }
            }
        } else {
            Toast.makeText(
                this,
                "User not found\n please login with your credentials",
                Toast.LENGTH_SHORT
            ).show()
            launchActivityFinish<LoginActivity> {
            }

        }
        loadAssets()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.flFragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment!!.navController
        )

    }
    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType
        val menu: Menu = binding.bottomNavigationView.getMenu()
        when (sportsType) {
            AppConstant.BASEBALL -> {


            }
            AppConstant.VOLLEYBALL -> {
                Log.d(TAG,"")
                when (AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId) {
                    AppConstant.ROLE_SUPER_PORTAL -> {
                        menu.getItem(0).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector_vb))
                        menu.getItem(1).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                        menu.getItem(2).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                        menu.getItem(3).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                        menu.getItem(4).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                    }
                    AppConstant.ROLE_ATHLETES_PORTAL -> {
                        binding.bottomNavigationView.menu.clear()
                        binding.bottomNavigationView.inflateMenu(R.menu.bottom_athletes_menu)
                    }
                    AppConstant.ROLE_CLUB_PORTAL -> {
                        binding.bottomNavigationView.menu.clear()
                        binding.bottomNavigationView.inflateMenu(R.menu.bottom_club_menu)
                    }
                    AppConstant.ROLE_TRAINER_PORTAL -> {
                        binding.bottomNavigationView.menu.clear()
                        binding.bottomNavigationView.inflateMenu(R.menu.bottom_trainer_menu)
                    }
                }
            }
            AppConstant.TODDLER -> {
//                menu.findItem(R.id.clubFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector_))
//                menu.findItem(R.id.trainerFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
//                menu.findItem(R.id.athletesFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
//                menu.findItem(R.id.userFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
//                menu.findItem(R.id.accountFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
//                menu.findItem(R.id.dashboardFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
            }
            AppConstant.QB -> {
                menu.findItem(R.id.clubFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector_qb))
                menu.findItem(R.id.trainerFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                menu.findItem(R.id.athletesFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                menu.findItem(R.id.userFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                menu.findItem(R.id.accountFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
                menu.findItem(R.id.dashboardFragment).setIcon(ContextCompat.getDrawable(applicationContext,R.drawable.tab_club_selector))
            }
        }
    }
    fun logoutFromUser() {
        launchActivityFinish<LoginActivity> { }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }


}