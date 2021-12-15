package com.example.sportsballistics.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.sportsballistics.AppSystem
import com.example.sportsballistics.R
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.api.URLIdentifiers
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.login.UserResponse
import com.example.sportsballistics.databinding.LoginBinding
import com.example.sportsballistics.ui.dashboard.DashboardActivity
import com.example.sportsballistics.utils.AppConstant
import com.example.sportsballistics.utils.launchActivity
import com.example.sportsballistics.utils.launchActivityFinish

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.login);
        loadAssets()

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

                                Handler(Looper.getMainLooper()).postDelayed({
                                    launchActivityFinish<DashboardActivity> {
                                        this.putExtra(
                                            URLIdentifiers.USER_ROLE,
                                            userResponse.loggedIn?.roleId
                                        )
                                    }
                                    finish()
                                }, 200)
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

    //TODO use this method temp for all asset loading
    fun loadAssets() {
        val sportsType = SharedPrefUtil.getInstance().sportsType
        when (sportsType) {
            AppConstant.BASEBALL -> {
                Glide.with(binding.root).load(R.drawable.bb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.bb_inner_logo).into(binding.imgLogo)
                binding.btnLogin.setBackgroundResource(R.drawable.btn_baseball)
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_username_icon,
                    R.color.colorBB,
                    binding.etEmail
                )
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_password_icon,
                    R.color.colorBB,
                    binding.etPassword
                )

            }
            AppConstant.VOLLEYBALL -> {
                Glide.with(binding.root).load(R.drawable.vb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.vb_inner_logo).into(binding.imgLogo)
                binding.btnLogin.setBackgroundResource(R.drawable.btn_volleyball)
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_username_icon,
                    R.color.colorVB,
                    binding.etEmail
                )
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_password_icon,
                    R.color.colorVB,
                    binding.etPassword
                )
            }
            AppConstant.TODDLER -> {
                Glide.with(binding.root).load(R.drawable.ic_toddler_login_bg)
                    .into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.ic_toddler_inner_logo)
                    .into(binding.imgLogo)
                binding.btnLogin.setBackgroundResource(R.drawable.btn_bg)
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_username_icon,
                    R.color.colorTodd,
                    binding.etEmail
                )
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_password_icon,
                    R.color.colorTodd,
                    binding.etPassword
                )
            }
            AppConstant.QB -> {
                Glide.with(binding.root).load(R.drawable.qb_login_bg).into(binding.ivBackground)
                Glide.with(binding.root).load(R.drawable.qb_inner_logo).into(binding.imgLogo)
                binding.btnLogin.setBackgroundResource(R.drawable.btn_qb)
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_username_icon,
                    R.color.colorQB,
                    binding.etEmail
                )
                AppConstant.getDrawableTintedColor(
                    R.drawable.ic_password_icon,
                    R.color.colorQB,
                    binding.etPassword
                )
            }
        }
    }

}