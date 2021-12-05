package com.example.sportsballistics.ui.dashboard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.databinding.ActivityDashboardBinding
import com.example.sportsballistics.ui.dashboard.athletes.AthletesFragment
import com.example.sportsballistics.ui.dashboard.club.ClubFragment
import com.example.sportsballistics.ui.dashboard.dashboard.DashboardFragment
import com.example.sportsballistics.ui.dashboard.my_account.AccountFragment
import com.example.sportsballistics.ui.dashboard.trainer.TrainerFragment
import com.example.sportsballistics.ui.dashboard.users.UserFragment
import com.example.sportsballistics.utils.AppConstant


class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
//        setCurrentFragment(DashboardFragment())


        if (AppSystem.getInstance().getCurrentUser() != null) {
            when (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId) {
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
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.flFragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment!!.navController
        )

/*        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
//            if (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId != null) {
//                when (AppSystem.getInstance().getCurrentUser().loggedIn!!.roleId) {
//                    AppConstant.ROLE_SUPER_PORTAL -> {
//                        when (it.itemId) {//Working Fine
//                            R.id.dashboard -> setCurrentFragment(DashboardFragment())
//                            R.id.club -> setCurrentFragment(ClubFragment())
//                            R.id.trainer -> setCurrentFragment(TrainerFragment())
//                            R.id.athlete -> setCurrentFragment(AthletesFragment())
//                            R.id.users -> setCurrentFragment(UserFragment())
//                            R.id.account -> setCurrentFragment(AccountFragment())
//                        }
//                        true
//                    }
//                    AppConstant.ROLE_ATHLETES_PORTAL -> { //Working Fine.
//                        when (it.itemId) {
//                            R.id.dashboard -> setCurrentFragment(DashboardFragment())
//                            R.id.trainer -> setCurrentFragment(TrainerFragment())
//                            R.id.athlete -> setCurrentFragment(AthletesFragment())
//                            R.id.users -> setCurrentFragment(UserFragment())
//                            R.id.account -> setCurrentFragment(AccountFragment())
//                        }
//                        true
//                    }
//                    AppConstant.ROLE_CLUB_PORTAL -> {//Working Fine
//                        when (it.itemId) {
//                            R.id.dashboard -> setCurrentFragment(DashboardFragment())
//                            R.id.trainer -> setCurrentFragment(TrainerFragment())
//                            R.id.athlete -> setCurrentFragment(AthletesFragment())
//                            R.id.users -> setCurrentFragment(UserFragment())
//                            R.id.account -> setCurrentFragment(AccountFragment())
//                        }
//                        true
//                    }
//                    AppConstant.ROLE_TRAINER_PORTAL -> {
//                        when (it.itemId) {
//                            R.id.dashboard -> setCurrentFragment(DashboardFragment())
//                            R.id.athlete -> setCurrentFragment(AthletesFragment())
//                            R.id.account -> setCurrentFragment(AccountFragment())
//                        }
//                        true
//                    }

//                }
//            }
            true

        }*/
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
}