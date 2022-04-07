package com.sportsballistics.sportsballistics.ui.dashboard.create_athlete_form.component

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.sportsballistics.sportsballistics.R
import com.sportsballistics.sportsballistics.data.api.ApiClient
import com.sportsballistics.sportsballistics.data.api.ApiInterface
import com.sportsballistics.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.sportsballistics.sportsballistics.data.listeners.Listeners
import com.sportsballistics.sportsballistics.data.remote.DashboardModel
import com.sportsballistics.sportsballistics.data.remote.form_service.FormServiceModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AthleteFormViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var mErrorListener: Listeners.DialogInteractionListener

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }

    companion object {
        private val TAG = AthleteFormViewModel::class.java.simpleName
    }

    interface ContentFetchListener {
        fun onFetched(anyObject: Any)
    }

    fun getServiceContent(
        context: Context,
        mListener: ContentFetchListener,
        athleteID: String,
        slug: String
    ) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call =
            apiService.getFormInfo(slug, athleteID)
        call.enqueue(object : Callback<FormServiceModel> {
            override fun onResponse(
                call: Call<FormServiceModel>,
                response: Response<FormServiceModel>
            ) {
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

            override fun onFailure(call: Call<FormServiceModel>, t: Throwable) {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException) {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                } else {
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

    fun submitForm(
        context: Context,
        mListener: ContentFetchListener,
        athleteID: String,
        paramMap:  HashMap<String,String>,
        slug: String) {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        paramMap.put("submitData","1")
        val call = apiService.submitSkillForm(slug, athleteID,paramMap)
        call.enqueue(object : Callback<DashboardModel> {
            override fun onResponse(
                call: Call<DashboardModel>,
                response: Response<DashboardModel>
            ) {
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

}
