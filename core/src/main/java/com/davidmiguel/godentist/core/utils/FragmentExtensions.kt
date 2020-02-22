package com.davidmiguel.godentist.core.utils

import android.view.View
import android.view.ViewGroup
import com.davidmiguel.godentist.core.base.BaseFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis

fun BaseFragment.showSnackbar(msg: Int, view: View? = null, duration: Int = Snackbar.LENGTH_SHORT) {
    showSnackbar(getString(msg), view, duration)
}

fun BaseFragment.showSnackbar(
    msg: String,
    view: View? = null,
    duration: Int = Snackbar.LENGTH_SHORT
) {
    Snackbar.make(view ?: getActivityRootView(this), msg, duration).show()
}

private fun getActivityRootView(fragment: BaseFragment): View {
    return (fragment.activity!!.findViewById(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
}

fun BaseFragment.setupFadeThroughTransition() {
    enterTransition = MaterialFadeThrough.create(requireContext())
    exitTransition = MaterialFadeThrough.create(requireContext())
}

fun BaseFragment.setupSharedAxisTransition() {
    enterTransition = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Z, true)
    exitTransition = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.Z, false)
}
