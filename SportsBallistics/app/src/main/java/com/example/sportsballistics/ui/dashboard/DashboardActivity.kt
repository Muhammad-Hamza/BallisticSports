package com.example.sportsballistics.ui.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.databinding.ActivityDashboardBinding
import com.example.sportsballistics.ui.login.LoginActivity
import com.example.sportsballistics.utils.AppConstant
import com.example.sportsballistics.utils.launchActivityFinish


class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
//        setCurrentFragment(DashboardFragment())


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