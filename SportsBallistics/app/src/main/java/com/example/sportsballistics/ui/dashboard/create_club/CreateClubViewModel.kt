package com.example.sportsballistics.ui.dashboard.create_club

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.sportsballistics.data.listeners.Listeners
import com.example.sportsballistics.ui.dashboard.club.ClubListViewModel

class CreateClubViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var mErrorListener: Listeners.DialogInteractionListener

    companion object {
        private val TAG = ClubListViewModel::class.java.simpleName
    }

    fun attachErrorListener(mErrorListener: Listeners.DialogInteractionListener) {
        this.mErrorListener = mErrorListener
    }
}