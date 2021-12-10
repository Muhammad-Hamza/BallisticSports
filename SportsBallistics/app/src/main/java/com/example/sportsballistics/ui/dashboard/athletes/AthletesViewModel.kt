package com.example.sportsballistics.ui.dashboard.athletes

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
import com.example.sportsballistics.data.remote.generic.GenericResponse
import com.example.sportsballistics.data.remote.generic.UserModel
import com.example.sportsballistics.ui.dashboard.create_athlete.CreateAthleteViewModel
import com.example.sportsballistics.ui.dashboard.dashboard.DashboardViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AthletesViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var mErrorListener: Listeners.DialogInteractionListener
    lateinit var categoryResponse: GenericResponse
    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }

    companion object {
        private val TAG = DashboardViewModel::class.java.simpleName
    }

    fun getContent(
        context: Context,
        content: String,
        searchKey: String,
        mListener: ContentFetchListener
    ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.getMainContent(10, content)
        call.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(
                call: Call<GenericResponse>,
                response: Response<GenericResponse>
            ) {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        categoryResponse = responseBody
                        mListener.onFetched(responseBody)
                    } else {
                        mErrorListener.addErrorDialog()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException) {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                } else {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

    fun getAthleteInfo(context: Context, userModel: UserModel, mListener: ContentFetchListener) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        apiService.getGenericDashboard(userModel.id).enqueue(object : Callback<DashboardModel> {
            override fun onResponse(
                call: Call<DashboardModel>,
                response: Response<DashboardModel>
            ) {
                if (response.body() != null) {
                    mListener.onFetched(response.body()!!)
                    mErrorListener.dismissDialog()
                } else {
                    mErrorListener.addErrorDialog()
                }
            }

            override fun onFailure(call: Call<DashboardModel>, t: Throwable) {
                mErrorListener.addErrorDialog()
            }

        })
    }

    fun deleteTrainer(context: Context, clubId: String, mListener: ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.deleteTrainer(clubId)
        call.enqueue(object : Callback<DashboardModel>
        {
            override fun onResponse(call: Call<DashboardModel>, response: Response<DashboardModel>)
            {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try
                {
                    val responseBody = response.body()
                    if (responseBody != null)
                    {
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

            override fun onFailure(call: Call<DashboardModel>, t: Throwable)
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


    interface ContentFetchListener {
        fun onFetched(anyObject: Any)
    }

}