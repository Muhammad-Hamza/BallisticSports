package com.example.sportsballistics.ui.dashboard.club_admin

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.example.sportsballistics.R
import com.example.sportsballistics.data.api.ApiClient
import com.example.sportsballistics.data.api.ApiInterface
import com.example.sportsballistics.data.api.network_interceptor.NoConnectivityException
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.data.remote.AthleteResponse
import com.example.sportsballistics.data.remote.DashboardModel
import com.example.sportsballistics.data.remote.club.ClubResponse
import com.example.sportsballistics.ui.dashboard.club.ClubListViewModel
import com.example.sportsballistics.ui.dashboard.create_athlete.CreateAthleteViewModel
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import okhttp3.RequestBody

import okhttp3.MultipartBody
import java.io.File
import java.lang.IllegalStateException

class CreateClubAdminViewModel(application: Application) : AndroidViewModel(application)
{
    private lateinit var mErrorListener: Listeners.DialogInteractionListener
    lateinit var dashboardResponse: DashboardModel
    lateinit var viewAthleteResp: AthleteResponse

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener)
    {
        this.mErrorListener = mErrorListener
    }

    companion object
    {
        private val TAG = CreateClubAdminViewModel::class.java.simpleName
    }

    fun getAthleteInfo(context: Context, id: String, mListener: ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        apiService.getGenericDashboard(id).enqueue(object : Callback<DashboardModel>
        {
            override fun onResponse(call: Call<DashboardModel>, response: Response<DashboardModel>)
            {
                if (response.body() != null)
                {
                    mListener.onFetched(response.body()!!)
                    mErrorListener.dismissDialog()
                }
                else
                {
                    mErrorListener.addErrorDialog()
                }
            }

            override fun onFailure(call: Call<DashboardModel>, t: Throwable)
            {
                mErrorListener.addErrorDialog()
            }

        })
    }
    /*
      Will be used for athletes and trainer both
       */
    fun addAthelete(context: Context, name: String, address: String, state: String, zipcode: Int, city: String, status: String, contact_no: String, age: String, grade: String, password: String, package_type: String, club_name: String, role_id: String, email: String, mListener: CreateAthleteViewModel.ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.addTrainer(email, name, contact_no, age, state, zipcode, city, status, address, grade, password, package_type, club_name, role_id)
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
                        dashboardResponse = responseBody
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
                mListener.onFetched(t)

            }
        })
    }



    /*
    Will be used for athletes and trainer both
     */
    fun editAthlete(context: Context, userId: String, name: String, address: String, state: String, zipcode: Int, city: String, status: String, contact_no: String, age: String, grade: String, password: String?, package_type: String, club_name: String, role_id: String, email: String, mListener: ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.editTrainer(userId, email, name, contact_no, age, state, zipcode, city, status, address, grade, password, package_type, club_name, role_id)
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
                        dashboardResponse = responseBody
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
                mListener.onFetched(t)

            }
        })
    }

    fun viewAthlete(context: Context, clubId: String, mListener: ContentFetchListener)
    {
        mErrorListener.addDialog()
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.viewTrainer(clubId)
        call.enqueue(object : Callback<AthleteResponse>
        {
            override fun onResponse(call: Call<AthleteResponse>, response: Response<AthleteResponse>)
            {
                Log.d(TAG, response.raw().toString())
                mErrorListener.dismissDialog()
                try
                {
                    val responseBody = response.body()
                    if (responseBody != null)
                    {
                        viewAthleteResp = responseBody
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

            override fun onFailure(call: Call<AthleteResponse>, t: Throwable)
            {
                mErrorListener.dismissDialog()
                if (t is NoConnectivityException)
                {
                    mErrorListener.addErrorDialog(context.getString(R.string.txt_network_error))
                }
                else
                {
                    mListener.onError(t)
                    mErrorListener.addErrorDialog()
                }
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
                    mListener.onError(t)
                    mErrorListener.addErrorDialog()
                }
            }
        })
    }

    fun getClubListFromServer(context: Context, content: String, mListener: ContentFetchListener)
    {
        val apiService = ApiClient.client(context).create(ApiInterface::class.java)
        val call = apiService.getContent(100, content, "")
        call.enqueue(object : Callback<ClubResponse>
        {
            override fun onResponse(call: Call<ClubResponse>, response: Response<ClubResponse>)
            {
                Log.d(TAG, response.raw().toString())
                try
                {
                    val responseBody = response.body()
                    if (responseBody != null)
                    {
                        mListener.onFetched(responseBody)
                    }
                    else
                    {
                        mListener.onError(IllegalStateException())
                    }
                } catch (e: IOException)
                {
                    mListener.onError(e)
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<ClubResponse>, t: Throwable)
            {
                mListener.onError(t)
            }
        })
    }

    interface ContentFetchListener
    {
        fun onFetched(anyObject: Any)
        fun onError(t: Throwable)
    }
}