package com.davidmiguel.godentist.core.utils

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.LocalDateTime
import java.util.*

class DateTimeUtilsTest {

    @Test
    fun formatDMYLocalDateTime() {
        assertThat(LocalDateTime.of(2019, 11, 22, 0, 0, 0).formatDMY()).isEqualTo("22/11/2019")
    }

    @Test
    fun formatDMYLong() {
        assertThat(Date(0).time.formatDMY()).isEqualTo("01/01/1970")
    }

    @Test
    fun isToday() {
        assertThat(Date().time.isToday()).isTrue()
        assertThat(Date(0).time.isToday()).isFalse()
    }
}