package com.example.retailcloudassessmentproject.manager

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object Utils {

    /**
     * Converts a numeric amount into a formatted currency string.
     *
     * @param amount The numeric amount to be formatted.
     * @return A string representing the numeric amount in currency format, e.g., "$1,234.56".
     */
    fun toCurrencyString(amount: Double?): String {
        if (amount == null) return ""
        val locale = Locale("en", "IN")
        val numberFormat = NumberFormat.getCurrencyInstance(locale)

        return try {
            val currency = Currency.getInstance("INR")
            numberFormat.currency = currency
            numberFormat.format(amount)
        } catch (e: IllegalArgumentException) {
            numberFormat.format(amount)
        }
    }

}