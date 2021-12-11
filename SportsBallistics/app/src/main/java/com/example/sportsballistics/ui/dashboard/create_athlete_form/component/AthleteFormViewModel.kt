package com.example.sportsballistics.ui.dashboard.create_athlete_form.component

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.ApiClient
import com.example.sportsballistics.data.api.ApiInterface
import com.example.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.form_service.FormServiceModel
import com.example.sportsballistics.data.remote.service.ServiceResponseModel
import com.example.sportsballistics.ui.dashboard.create_athlete.CreateAthleteViewModel
import com.example.sportsballistics.ui.dashboard.form_list.component.FormListViewModel
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

}
