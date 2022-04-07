package com.sportsballistics.sportsballistics.utils

import android.widget.Toast
import com.sportsballistics.sportsballistics.AppSystem

class AppUtils {
    companion object {
        fun showToast(content: String) {
            Toast.makeText(AppSystem.context, content, Toast.LENGTH_SHORT).show()
        }

        fun showToast(contentId: Int) {
            Toast.makeText(AppSystem.context, contentId, Toast.LENGTH_SHORT).show()
        }
    }
}