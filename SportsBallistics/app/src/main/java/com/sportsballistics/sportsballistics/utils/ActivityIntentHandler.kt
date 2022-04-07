package com.sportsballistics.sportsballistics.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sportsballistics.sportsballistics.R

inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    startActivityForResult(intent, requestCode, options)
    overridePendingTransition(R.anim.enter_from_right, R.anim.nothing)
}

inline fun <reified T : Any> Activity.launchActivityFinish(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
    startActivity(intent, options)
    finish()
}

inline fun <reified T : Any> Activity.launchActivityForResult(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
    startActivityForResult(intent, 0, options)
}
inline fun <reified T : Any> Activity.launchActivityFromRightForResult(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    overridePendingTransition(R.anim.enter_from_right, R.anim.nothing)
    startActivityForResult(intent, 0, options)
}

inline fun <reified T : Any> Activity.launchActivityTop(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
//    startActivity(intent, requestCode, options)
    startActivity(intent, options)
    overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
}

inline fun <reified T : Any> Activity.launchActivityTopForResult(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
//    startActivity(intent, requestCode, options)
    startActivityForResult(intent,0, options)
    overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    startActivity(intent, options)
//    startActivity(intent)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)