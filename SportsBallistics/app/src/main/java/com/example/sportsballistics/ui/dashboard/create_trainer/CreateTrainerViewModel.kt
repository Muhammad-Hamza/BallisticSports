package com.example.sportsballistics.ui.dashboard.create_trainer

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.sportsballistics.data.api.ApiClient
import com.example.sportsballistics.data.api.ApiInterface
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.generic.UserModel
import com.example.sportsballistics.ui.dashboard.athletes.AthletesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateTrainerViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var mErrorListener: Listeners.DialogInteractionListener

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }

    fun getTrainerInfo(
        context: Context,
        id: String,
        mListener: ContentFetchListener
    ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        apiService.getGenericDashboard(id).enqueue(object : Callback<DashboardModel> {
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

    interface ContentFetchListener {
        fun onFetched(anyObject: Any)
    }

}