@file:Suppress("RedundantVisibilityModifier")

package com.davidmiguel.godentist.core.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

private fun getCurrencyFormat() = NumberFormat.getCurrencyInstance() as DecimalFormat

@JvmOverloads
public fun formatMoney(
    amount: Double,
    currency: Currency = Currency.getInstance("EUR"),
    numDecimals: Int = 2,
    signed: Boolean = false,
    roundingMode: RoundingMode = RoundingMode.HALF_UP
): String {
    return getCurrencyFormat().apply {
        this.currency = currency
        positivePrefix = if (signed) "$positivePrefix+" else positivePrefix.replace("+", "")
        minimumFractionDigits = numDecimals
        maximumFractionDigits = numDecimals
        this.roundingMode = roundingMode
    }.format(amount)
}
