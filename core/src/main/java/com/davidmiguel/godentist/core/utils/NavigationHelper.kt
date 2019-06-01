package com.davidmiguel.godentist.core.utils

import android.content.Intent

private const val PACKAGE_NAME = "com.davidmiguel.godentist"

/**
 * Create an Intent with [Intent.ACTION_VIEW] to an [AddressableActivity].
 */
fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(PACKAGE_NAME, addressableActivity.className)
}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

object Activities {

    object Dashboard : AddressableActivity {
        override val className = "$PACKAGE_NAME.dashboard.DashboardActivity"
    }

    object Auth : AddressableActivity {
        override val className = "$PACKAGE_NAME.auth.AuthActivity"
    }
}