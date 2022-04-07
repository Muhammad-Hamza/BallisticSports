package com.sportsballistics.sportsballistics.ui.dashboard.club

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.api.ApiInterface
import com.sportsballistics.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.sportsballistics.sportsballistics.data.listeners.Listeners
import com.sportsballistics.sportsballistics.data.remote.club.ClubResponse
import com.sportsballistics.sportsballistics.data.api.ApiClient
import com.sportsballistics.sportsballistics.data.remote.DashboardModel
import com.sportsballistics.sportsballistics.data.remote.dashboard.DashboardResponse
import com.sportsballistics.sportsballistics.ui.dashboard.athletes.AthletesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ClubListViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var mErrorListener: Listeners.NewDialogInteractionListener

    lateinit var categoryResponse: ClubResponse
    lateinit var dashboardResponse: DashboardResponse
    fun attachErrorListener(mErrorListener: Listeners.NewDialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }

    companion object {
        private val TAG = ClubListViewModel::class.java.simpleName
    }

    fun getContent(
        context: Context,
        content: String,
        searchKey: String,
        mListener: ContentFetchListener
    ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.getContent(10, content, searchKey)
        call.enqueue(object : Callback<ClubResponse> {
            override fun onResponse(call: Call<ClubResponse>, response: Response<ClubResponse>) {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try {
                    val responseBody = response.body()
                    if (responseBody != null && responseBody.content != null && responseBody.content.users != null && responseBody.content.users.size > 0) {
                        categoryResponse = responseBody
                        mListener.onFetched(responseBody)
                    } else {
                        mErrorListener.addErrorDialog()
                        mErrorListener.makeListEmpty()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ClubResponse>, t: Throwable) {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException) {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                } else {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

    fun getDashboard(context: Context, mListener: DashboardFetchListener) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.dashboard
        call.enqueue(object : Callback<DashboardResponse> {
            override fun onResponse(
                call: Call<DashboardResponse>,
                response: Response<DashboardResponse>
            ) {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        dashboardResponse = responseBody
                        mListener.onFetched(responseBody)
                    } else {
                        mErrorListener.addErrorDialog()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException) {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                } else {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

    fun deleteTrainer(
        context: Context,
        clubId: String,
        mListener: AthletesViewModel.ContentFetchListener
    ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.deleteClub(clubId)
        call.enqueue(object : Callback<DashboardModel> {
            override fun onResponse(
                call: Call<DashboardModel>,
                response: Response<DashboardModel>
            ) {
                Log.d(AthletesViewModel.TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        mListener.onFetched(responseBody)
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
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }


    interface ContentFetchListener {
        fun onFetched(content: ClubResponse)
    }

    interface DashboardFetchListener {
        fun onFetched(content: DashboardResponse)
    }

}