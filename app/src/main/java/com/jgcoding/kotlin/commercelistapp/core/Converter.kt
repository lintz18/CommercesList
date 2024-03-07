package com.jgcoding.kotlin.commercelistapp.core

object Converter {
    fun convertIntoKms(m: Double): String {
        val distance = m/1000
        return String.format("%.1f", distance)
    }
}