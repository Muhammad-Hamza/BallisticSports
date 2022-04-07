package com.sportsballistics.sportsballistics.ui.dashboard

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.sportsballistics.sportsballistics.AppSystem
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.SharedPrefUtil
import com.sportsballistics.sportsballistics.databinding.ActivityDashboardBinding
import com.sportsballistics.sportsballistics.ui.SelectionActivity
import com.sportsballistics.sportsballistics.ui.dashboard.athletes.AthletesViewModel.Companion.TAG
import com.sportsballistics.sportsballistics.ui.login.LoginActivity
import com.sportsballistics.sportsballistics.utils.AppConstant
import com.sportsballistics.sportsballistics.utils.AppStr
import com.sportsballistics.sportsballistics.utils.AppUtils.Companion.showToast
import com.sportsballistics.sportsballistics.utils.launchActivityFinish

class DashboardActivity : AppCompatActivity() {
    lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppSystem.getInstance().setStatusColor(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.bottomNavigationView.setItemIconTintList(null);
        loadMenu()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.flFragment) as NavHostFragment?
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment!!.navController
        )

    }

    fun loadMenu() {
        if (AppSystem.getInstance().getCurrentUser() != null && AppSystem.getInstance()
                .getCurrentUser()!!.loggedIn != null
        ) {
            val sportsType = SharedPrefUtil.getInstance().sportsType

            when (AppSystem.getInstance().getCurrentUser()!!.loggedIn!!.roleId) {
                AppConstant.ROLE_SUPER_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()

                    when (sportsType) {
                        AppConstant.BASEBALL -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_admin_menubb)
                        }
                        AppConstant.VOLLEYBALL -> {
                            Log.d(TAG, "")
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_admin_menuvb)
                        }
                        AppConstant.TODDLER -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_admin_menu)
                        }
                        AppConstant.QB -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_admin_menuqb)

                        }
                    }
                }
                AppConstant.ROLE_ATHLETES_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()

                    when (sportsType) {
                        AppConstant.BASEBALL -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_athletes_menubb)
                        }
                        AppConstant.VOLLEYBALL -> {
                            Log.d(TAG, "")
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_athletes_menuvb)
                        }
                        AppConstant.TODDLER -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_athletes_menu)
                        }
                        AppConstant.QB -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_athletes_menuqb)

                        }
                    }

                }
                AppConstant.ROLE_CLUB_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()

                    when (sportsType) {
                        AppConstant.BASEBALL -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_club_menubb)
                        }
                        AppConstant.VOLLEYBALL -> {
                            Log.d(TAG, "")
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_club_menuvb)
                        }
                        AppConstant.TODDLER -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_club_menu)
                        }
                        AppConstant.QB -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_club_menuqb)

                        }
                    }
                }
                AppConstant.ROLE_TRAINER_PORTAL -> {
                    binding.bottomNavigationView.menu.clear()

                    when (sportsType) {
                        AppConstant.BASEBALL -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_trainer_menubb)
                        }
                        AppConstant.VOLLEYBALL -> {

                            Log.d(TAG, "")
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_trainer_menuvb)
                        }
                        AppConstant.TODDLER -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_trainer_menu)
                        }
                        AppConstant.QB -> {
                            binding.bottomNavigationView.inflateMenu(R.menu.bottom_trainer_menuqb)

                        }
                    }
                }
            }
        } else {
            showToast(AppStr.userNotFoundContent)
            launchActivityFinish<LoginActivity> {}
        }

        when (SharedPrefUtil.getInstance().sportsType) {
            AppConstant.BASEBALL -> {
                binding.bottomNavigationView.itemTextColor =
                    ContextCompat.getColorStateList(applicationContext, R.color.selectorbb)
            }
            AppConstant.VOLLEYBALL -> {
                Log.d(TAG, "")
                binding.bottomNavigationView.itemTextColor =
                    ContextCompat.getColorStateList(applicationContext, R.color.selectorvb)
            }
            AppConstant.TODDLER -> {
                binding.bottomNavigationView.itemTextColor =
                    ContextCompat.getColorStateList(applicationContext, R.color.selector)
            }
            AppConstant.QB -> {
                binding.bottomNavigationView.itemTextColor =
                    ContextCompat.getColorStateList(applicationContext, R.color.selectorqb)
            }
        }
    }

    fun logoutFromUser() {
        launchActivityFinish<SelectionActivity> { }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}