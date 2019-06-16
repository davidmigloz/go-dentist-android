package com.davidmiguel.godentist.core.utils

import android.view.View
import android.view.ViewGroup
import com.davidmiguel.godentist.core.base.BaseFragment
import com.google.android.material.snackbar.Snackbar

fun BaseFragment.showSnackbar(msg: Int, view: View? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    showSnackbar(getString(msg), view, duration)
}

fun BaseFragment.showSnackbar(msg: String, view: View? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view ?: getActivityRootView(this), msg, duration).show()
}

private fun getActivityRootView(fragment: BaseFragment): View {
    return (fragment.activity!!.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
}
