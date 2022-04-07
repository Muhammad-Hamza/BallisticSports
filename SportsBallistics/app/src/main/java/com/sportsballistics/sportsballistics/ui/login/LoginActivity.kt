package com.sportsballistics.sportsballistics.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.sportsballistics.sportsballistics.AppSystem
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.SharedPrefUtil
import com.sportsballistics.sportsballistics.data.api.URLIdentifiers
import com.sportsballistics.sportsballistics.data.listeners.Listeners
import com.sportsballistics.sportsballistics.data.remote.login.UserResponse
import com.sportsballistics.sportsballistics.databinding.LoginBinding
import com.sportsballistics.sportsballistics.ui.dashboard.DashboardActivity
import com.sportsballistics.sportsballistics.utils.AppConstant
import com.sportsballistics.sportsballistics.utils.AppUtils.Companion.showToast
import com.sportsballistics.sportsballistics.utils.launchActivityFinish
import java.lang.IllegalStateException
import java.lang.NullPointerException

class LoginActivity : AppCompatActivity()
{
    private lateinit var binding: LoginBinding
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.login);
        loadAssets()

        initViewModel()

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.startAnimation();
            performLogin()
        }
    }

    private fun performLogin()
    {
        if (viewModel.validate(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    binding
                              )
        )
        {
            viewModel.login(
                    applicationContext,
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(),
                    object : LoginViewModel.onSignInCompleteListener
                    {
                        override fun onSignInComplete(userResponse: UserResponse)
                        {
                            try
                            {
                                binding.btnLogin.revertAnimation()
                                if (userResponse != null)
                                {
                                    if (userResponse.isError!!)
                                    {
                                        showToast(userResponse.message!!)
                                    }
                                    else
                                    {
                                        AppSystem.getInstance().setCurrentUser(userResponse)
                                        binding.btnLogin.revertAnimation();

                                        Handler(Looper.getMainLooper()).postDelayed({
                                                                                        launchActivityFinish<DashboardActivity> {
                                                                                            this.putExtra(URLIdentifiers.USER_ROLE, userResponse.loggedIn?.roleId)
                                                                                        }
                                                                                        finish()
                                                                                    }, 200)
                                    }
                                }
                                else
                                {
                                    showToast(userResponse.message!!)
                                }
                            }
                            catch (ex: NullPointerException)
                            {
                            }
                            catch (ex: IllegalStateException)
                            {
                            }

                        }
                    })
        }
    }

    private fun initViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        viewModel.attachErrorListener(object : Listeners.DialogInteractionListener
                                      {
                                          override fun dismissDialog()
                                          {
                                          }

                                          override fun addDialog()
                                          {
                                          }

                                          override fun addErrorDialog()
                                          {
                                          }

                                          override fun addErrorDialog(msg: String?)
                                          {
                                              if (msg != null)
                                              {
                                              }
                                          }
                                      })
    }

    //TODO use this method temp for all asset loading
    fun loadAssets()
    {
        val sportsType = SharedPrefUtil.getInstance().sportsType
        when (sportsType)
        {
            AppConstant.BASEBALL   ->
            {
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
            AppConstant.VOLLEYBALL ->
            {
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
            AppConstant.TODDLER    ->
            {
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
            AppConstant.QB         ->
            {
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

        AppSystem.getInstance().setStatusColor(this)
    }

}