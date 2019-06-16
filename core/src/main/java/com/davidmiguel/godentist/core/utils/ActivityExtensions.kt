package com.davidmiguel.godentist.core.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.davidmiguel.godentist.core.base.BaseActivity
import com.google.android.material.snackbar.Snackbar
import android.view.ViewGroup
import com.davidmiguel.godentist.core.base.BaseFragment

fun BaseActivity.startActivity(
    act: AddressableActivity,
    bundle: Bundle? = null,
    clearTask: Boolean = false
) {
    val intent = intentTo(act)
    if (clearTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }
    startActivity(intent, bundle)
}

fun BaseActivity.showSnackbar(msg: Int, view: View? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    showSnackbar(getString(msg), view, duration)
}

fun BaseActivity.showSnackbar(msg: String, view: View? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view ?: getActivityRootView(this), msg, duration).show()
}

private fun getActivityRootView(activity: Activity): View {
    return (activity.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
}
