package com.nhatle.notificationkotlin

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Ultils {
    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat(DATE_FORMAT)
    fun currentDateTime(): String {
        val now = getCurrentDate()
        return formatter.format(now)
    }
    private fun getCurrentDate(): Date {
        return Date()
    }
}