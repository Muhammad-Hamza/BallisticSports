package com.example.sportsballistics.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.login.UserResponse
import com.example.sportsballistics.databinding.LoginBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.utils.launchActivity
import com.example.sportsballistics.utils.launchActivityFinish

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.login);

        initViewModel()

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.startAnimation();
            performLogin()
        }
    }

    private fun performLogin() {
        if (viewModel.validate(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding
            )
        ) {
            viewModel.login(
                applicationContext,
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                object : LoginViewModel.onSignInCompleteListener {
                    override fun onSignInComplete(userResponse: UserResponse) {
                        binding.btnLogin.revertAnimation()
                        if (userResponse != null) {
                            if (userResponse.isError!!) {
                                Toast.makeText(
                                    applicationContext!!,
                                    userResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                AppSystem.getInstance().setCurrentUser(userResponse)
                                binding.btnLogin.revertAnimation();

                                Handler(Looper.getMainLooper()).postDelayed( {
                                    launchActivityFinish<DashboardActivity> {
                                        this.putExtra(URLIdentifiers.USER_ROLE, userResponse.loggedIn?.roleId)
                                    }
                                },200)
                            }
                        } else {
                            Toast.makeText(
                                applicationContext!!,
                                userResponse.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                })
        }

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener {
            override fun dismissDialog() {
            }

            override fun addDialog() {
            }

            override fun addErrorDialog() {
            }

            override fun addErrorDialog(msg: String?) {
                if (msg != null) {
                }
            }
        })
    }

}