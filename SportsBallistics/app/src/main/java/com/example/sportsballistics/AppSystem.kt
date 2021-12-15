package com.example.sportsballistics

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.sportsballistics.data.SharedPrefUtil
import com.example.sportsballistics.data.remote.login.UserResponse
import com.google.gson.Gson
import okhttp3.Cookie
import android.app.Activity
import android.view.Window

import androidx.core.content.ContextCompat

import android.view.WindowManager
import com.example.sportsballistics.utils.AppConstant

class AppSystem : Application()
{

    val TAG = AppSystem::class.java.name

    private var prefs: SharedPreferences? = null
    private val USER = "user"
    private val PREFERENCES_KEY = "com.example.sportsballistics"
    private val IS_VERIFIED = "is_verified"
    var cookies: List<Cookie> = arrayListOf()

    private lateinit var currentUser: UserResponse
    fun setCurrentUser(user: UserResponse)
    {
        SharedPrefUtil.getInstance().saveUser(user)
    }

    fun logoutUser()
    {
        SharedPrefUtil.getInstance().logout()
    }

    fun getCurrentUser(): UserResponse?
    {
        return SharedPrefUtil.getInstance().user
    }

    override fun onCreate()
    {
        super.onCreate()
        context = applicationContext
    }

    companion object
    {
        /*
        Volatile instance to make singleton
        thread safe
        */
        @Volatile
        private var sSoleInstance: AppSystem? = null

        lateinit var context: Context

        fun getInstance(): AppSystem
        {

            //Double check locking pattern
            if (sSoleInstance == null)
            {

                //Check for the first time
                synchronized(AppSystem::class.java) {   //Check for the second time.
                    //if there is no instance available... create new one
                    if (sSoleInstance == null)
                    {
                        sSoleInstance = AppSystem()
                    }
                }
            }

            return sSoleInstance!!
        }
    }

    fun getUser(): UserResponse?
    {
        return Gson().fromJson(getPrefs()?.getString(USER, ""), UserResponse::class.java)
    }

    fun saveUser(user: UserResponse?)
    {
        getPrefs().edit().putString(USER, Gson().toJson(user)).apply()
    }

    private fun getPrefs(): SharedPreferences
    {
        if (prefs == null)
        {
            prefs = context?.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        }
        return prefs!!
    }

    fun saveVerificationStatus(isVerified: Boolean)
    {
        getPrefs().edit().putBoolean(IS_VERIFIED, isVerified).apply()
    }

    fun isVerified(): Boolean
    {
        return getPrefs().getBoolean(IS_VERIFIED, false)
    }

    fun setStatusColor(activity: Activity)
    {
        val window: Window = activity.getWindow()

// clear FLAG_TRANSLUCENT_STATUS flag:

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

// finally change the color

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(activity, getColor()))
    }

    fun getColor(): Int
    {
        when (SharedPrefUtil.getInstance().sportsType)
        {
//            AppConstant.TODDLER ->
//            {
//                return R.color.colorTodd
//            }
            AppConstant.BASEBALL ->
            {
                return R.color.colorBB
            }
            AppConstant.VOLLEYBALL ->
            {
                return R.color.colorVB
            }
            AppConstant.QB ->
            {
                return R.color.colorQB
            }
            else ->
            {
                return R.color.colorTodd
            }
        }
    }
}