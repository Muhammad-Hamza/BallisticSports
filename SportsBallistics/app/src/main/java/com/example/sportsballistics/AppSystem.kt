package com.example.sportsballistics

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.sportsballistics.data.remote.login.UserResponse
import com.google.gson.Gson

class AppSystem : Application()
{
    val TAG = AppSystem::class.java.name
    private var prefs: SharedPreferences? = null
    private val USER = "user"
    private val PREFERENCES_KEY = "com.example.sportsballistics"
    private val IS_VERIFIED = "is_verified"

    companion object{
        /*
        Volatile instance to make singleton
        thread safe
        */
        @Volatile
        private var sSoleInstance: AppSystem? = null

        fun getInstance(): AppSystem {

            //Double check locking pattern
            if (sSoleInstance == null) {

                //Check for the first time
                synchronized(AppSystem::class.java) {   //Check for the second time.
                    //if there is no instance available... create new one
                    if (sSoleInstance == null) {
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
            prefs = applicationContext.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
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
}