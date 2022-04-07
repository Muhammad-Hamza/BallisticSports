package com.sportsballistics.sportsballistics.ui.login

import android.app.Application
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.api.ApiInterface
import com.sportsballistics.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.sportsballistics.sportsballistics.data.listeners.Listeners
import com.sportsballistics.sportsballistics.data.remote.login.UserResponse
import com.sportsballistics.sportsballistics.databinding.LoginBinding
import com.sportsballistics.sportsballistics.data.api.ApiClient
import com.sportsballistics.sportsballistics.utils.AppUtils.Companion.showToast
import retrofit2.Call
import retrofit2.Response
import java.io.IOException
import retrofit2.Callback

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var mErrorListener: Listeners.DialogInteractionListener

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }

    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

    fun login(
        context: Context,
        email: String,
        password: String,
        mListener: onSignInCompleteListener
    ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        Log.d(TAG, "===============LOGGING===============")
        var call = apiService.login(email, password)

        call?.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try {
                    val userResponse = response.body()
                    if (userResponse != null) mListener.onSignInComplete(userResponse)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException) {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                } else {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

    interface onSignInCompleteListener {
        fun onSignInComplete(userResponse: UserResponse)
    }

    public fun validate(email: String, password: String, binding: LoginBinding): Boolean {
        var isValidated = true
        if (isValidEmail(email)) {
            isValidated = true
        } else {
            showToast(binding.root.context.getString(R.string.err_email_incorrect))
            isValidated = false
            return isValidated
        }
        if (password.isEmpty()) {
            showToast(binding.root.context.getString(R.string.err_insert_pass))
            isValidated = false
            return isValidated
        } else {
            isValidated = true
        }
        return isValidated
    }

    open fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
