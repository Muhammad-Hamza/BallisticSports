package com.sportsballistics.sportsballistics.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

class AppFunctions {
    companion object {
        fun getSpannableText(content: String, keyStr: String, value: String): Spanned? {
            val html = content.replace(keyStr, value)
//            return html;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
                // we are using this flag to give a consistent behaviour
                return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(html);
            }
        }  fun getSpannableText(content: String): Spanned? {
//            return html;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
                // we are using this flag to give a consistent behaviour
                return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(content);
            }
        }
    }
}