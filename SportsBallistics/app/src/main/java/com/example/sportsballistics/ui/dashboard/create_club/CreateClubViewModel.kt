package com.example.sportsballistics.ui.dashboard.create_club

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.ApiClient
import com.example.sportsballistics.data.api.ApiInterface
import com.example.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.data.remote.dashboard.DashboardResponse
import com.example.sportsballistics.ui.dashboard.club.ClubListViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CreateClubViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var mErrorListener: Listeners.DialogInteractionListener
    lateinit var dashboardResponse: DashboardModel

    companion object {
        private val TAG = ClubListViewModel::class.java.simpleName
    }

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }
    interface ContentFetchListener {
        fun onSuccess(content: DashboardModel)
        fun onError(t: Throwable)
    }

    fun addClub(
        context: Context,
        name:String,address:String,state:String,zipcode:Int,limit:Int,email:String,city:String,password:String,status:String,
        mListener: ContentFetchListener
                  ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.addClub(name,address,state,city,password,status,email,zipcode,limit)
        call.enqueue(object : Callback<DashboardModel>
        {
            override fun onResponse(call: Call<DashboardModel>, response: Response<DashboardModel>) {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        dashboardResponse = responseBody
                        mListener.onSuccess(responseBody)
                    } else {
                        mErrorListener.addErrorDialog()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<DashboardModel>, t: Throwable) {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException) {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                } else {
                    mListener.onError(t)
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

}