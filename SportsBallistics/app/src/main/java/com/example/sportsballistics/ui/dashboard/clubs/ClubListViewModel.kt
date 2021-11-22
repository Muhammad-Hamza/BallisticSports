package com.example.sportsballistics.ui.dashboard.clubs

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.ApiInterface
import com.example.sportsballistics.data.api.URLIdentifiers.CLUB_CONTENT
import com.example.sportsballistics.data.api.URLIdentifiers.USER_CONTENT
import com.example.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.techwireme.baladizabeha.data.api.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ClubListViewModel(application: Application) : AndroidViewModel(application)
{
    private lateinit var mErrorListener: Listeners.DialogInteractionListener
    lateinit var categoryResponse: ClubResponse
    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener)
    {
        this.mErrorListener = mErrorListener
    }

    companion object
    {
        private val TAG = ClubListViewModel::class.java.simpleName
    }

    fun getContent(context: Context,content:String,searchKey:String, mListener: ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.getClubList(10,content)
        call.enqueue(object : Callback<ClubResponse>
        {
            override fun onResponse(call: Call<ClubResponse>, response: Response<ClubResponse>)
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

            override fun onFailure(call: Call<ClubResponse>, t: Throwable)
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
        fun onFetched(content: ClubResponse)
    }

}