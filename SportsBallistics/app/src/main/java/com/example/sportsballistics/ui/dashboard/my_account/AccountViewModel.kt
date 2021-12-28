package com.example.sportsballistics.ui.dashboard.my_account

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.ApiClient
import com.example.sportsballistics.data.api.ApiInterface
import com.example.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.AccountResponse
import com.example.sportsballistics.data.remote.dashboard.DashboardResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AccountViewModel(application: Application) : AndroidViewModel(application)
{
    private lateinit var mErrorListener: Listeners.DialogInteractionListener

    lateinit var categoryResponse: AccountResponse
    lateinit var dashboardResponse: DashboardResponse
    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener)
    {
        this.mErrorListener = mErrorListener
    }

    companion object
    {
        private val TAG = AccountViewModel::class.java.simpleName
    }

    fun getSettings(context: Context,slug:String, mListener: ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.getSetting(slug)
        call.enqueue(object : Callback<AccountResponse>
        {
            override fun onResponse(call: Call<AccountResponse>, response: Response<AccountResponse>)
            {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try
                {
                    val responseBody = response.body()
                    if (responseBody != null)
                    {
                        categoryResponse = responseBody
                        mListener.onFetched(responseBody)
                    }
                    else
                    {
                        mErrorListener.addErrorDialog()
                    }
                } catch (e: IOException)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<AccountResponse>, t: Throwable)
            {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException)
                {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                }
                else
                {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

    fun changePassword(context: Context,oldPass:String,newPass:String,confirmPass:String, mListener: ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.changePass(oldPass,newPass,confirmPass)
        call.enqueue(object : Callback<AccountResponse>
        {
            override fun onResponse(call: Call<AccountResponse>, response: Response<AccountResponse>)
            {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try
                {
                    val responseBody = response.body()
                    if (responseBody != null)
                    {
                        categoryResponse = responseBody
                        mListener.onFetched(responseBody)
                    }
                    else
                    {
                        mErrorListener.addErrorDialog()
                    }
                } catch (e: IOException)
                {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<AccountResponse>, t: Throwable)
            {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException)
                {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                }
                else
                {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }
    interface ContentFetchListener
    {
        fun onFetched(content: AccountResponse)
    }

}
