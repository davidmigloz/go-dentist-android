package com.davidmiguel.godentist.testutils

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.davidmiguel.godentist.core.utils.Event
import com.google.common.truth.Truth.assertThat

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun LiveData<Event<String>>.assertEventTriggered(
    id: String
) {
    val value = getOrAwaitValue()
    assertThat(value.getContentIfNotHandled()).isEqualTo(id)
}

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
fun assertSnackbarMessage(snackbarLiveData: LiveData<Event<Int>>, messageId: Int) {
    val value: Event<Int> = snackbarLiveData.getOrAwaitValue()
    assertThat(value.getContentIfNotHandled()).isEqualTo(messageId)
}
